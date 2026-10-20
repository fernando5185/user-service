# User Service API

API REST para la gestión de usuarios, desarrollada con Spring Boot. Utiliza PostgreSQL para la persistencia, Keycloak como proveedor de identidad y Spring Security OAuth2 Resource Server para proteger los endpoints mediante tokens JWT.

> **Estado:** proyecto en desarrollo. Ajusta las versiones, variables de entorno y configuración de Keycloak a tu entorno antes de desplegarlo.

## Características

- Crear, consultar, actualizar y eliminar usuarios.
- Validación de solicitudes mediante DTOs y Bean Validation.
- Persistencia relacional con PostgreSQL y Spring Data JPA.
- Mapeo entre entidades y DTOs con MapStruct.
- Autenticación mediante JWT emitidos por Keycloak.
- Autorización basada en roles (`ROLE_ADMIN` y `ROLE_USER`).
- Manejo centralizado de errores con `ProblemDetail`.
- Respuestas HTTP consistentes para errores de validación y seguridad.
- Entorno local basado en Docker Compose.

## Tecnologías

- Java y Spring Boot
- Spring Web / Spring MVC
- Spring Security
- OAuth 2.0 Resource Server y JWT
- Spring Data JPA / Hibernate
- PostgreSQL
- Keycloak
- MapStruct
- Docker y Docker Compose

> Añade aquí las versiones exactas de Java, Spring Boot y las dependencias según tu `pom.xml` o `build.gradle`.

## Arquitectura general

```text
Cliente HTTP
    |
    | Authorization: Bearer <JWT>
    v
Spring Security / OAuth2 Resource Server
    |
    | Validación del JWT
    | Conversión de roles de Keycloak
    v
Controladores REST
    |
    v
Capa de servicio
    |
    +---- UserMapper (MapStruct)
    |
    v
Spring Data JPA
    |
    v
PostgreSQL

Keycloak: emite los JWT y administra las identidades y roles.
```

## Requisitos previos

Para ejecutar el entorno local necesitas:

- Docker Engine o Docker Desktop.
- Docker Compose.
- Git.
- Un cliente HTTP, por ejemplo curl, Postman o Insomnia.

Si vas a ejecutar la aplicación fuera de Docker, también necesitarás la versión de Java y la herramienta de construcción configuradas en el proyecto.

## Configuración

La aplicación requiere una base de datos PostgreSQL y un realm de Keycloak configurado para emitir tokens compatibles con el Resource Server.

### Variables de entorno

La configuración actual de Docker Compose utiliza valores de desarrollo similares a estos:

| Variable | Descripción | Ejemplo local |
|---|---|---|
| `SPRING_DATASOURCE_URL` | URL JDBC de la base de datos de la aplicación | `jdbc:postgresql://postgres:5432/user_service` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de PostgreSQL | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de PostgreSQL | `admin` |
| `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI` | Emisor esperado del JWT | `http://localhost:8080/realms/mi-realm` |
| `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI` | URL de las claves públicas del realm | `http://host.docker.internal:8080/realms/mi-realm/protocol/openid-connect/certs` |

**Importante:** los valores de ejemplo son para desarrollo local, no para producción. `localhost` dentro de un contenedor apunta a ese mismo contenedor. Verifica que la URL de emisor (`iss`) coincida exactamente con la que incluye el JWT de Keycloak. La URL de las claves públicas debe ser accesible desde el contenedor de la aplicación.

No publiques credenciales reales en Git. Para otros entornos, usa variables de entorno o un gestor de secretos.

## Ejecución con Docker Compose

Desde el directorio que contiene el archivo `compose.yaml` o `docker-compose.yml`:

```bash
docker compose up --build -d
```

Consulta el estado de los servicios:

```bash
docker compose ps
```

Consulta los logs de la aplicación:

```bash
docker compose logs -f app
```

Consulta los logs de Keycloak:

```bash
docker compose logs -f keycloak
```

Para detener los servicios:

```bash
docker compose down
```

Para detener los servicios y eliminar también el volumen de datos de PostgreSQL:

```bash
docker compose down -v
```

> **Precaución:** `docker compose down -v` elimina el volumen de la base de datos y, por tanto, puede borrar los datos persistidos.

### Puertos locales

Según la configuración de Compose compartida:

| Servicio | Puerto del host |
|---|---:|
| API | `8081` |
| Keycloak | `8080` |
| PostgreSQL | `5432` |

Keycloak necesita una base de datos disponible y la base de datos `keycloak` debe existir. Si solo configuras `POSTGRES_DB=user_service`, comprueba que un script de inicialización cree también la base `keycloak` antes de que Keycloak intente conectarse.

`depends_on` por sí solo no garantiza que PostgreSQL o Keycloak estén listos para aceptar conexiones. Para un arranque más fiable, configura health checks y estrategias de reintento.

## Configuración de Keycloak

Antes de probar los endpoints protegidos:

1. Inicia Keycloak.
2. Crea o importa el realm utilizado por la aplicación (por ejemplo, `mi-realm`).
3. Configura el cliente o los clientes necesarios para obtener tokens.
4. Crea los roles de realm que utiliza la API, por ejemplo `ADMIN` y `USER`.
5. Asigna los roles a los usuarios correspondientes.
6. Obtén un access token y verifica que incluya el claim `realm_access.roles`.
7. Confirma que el emisor (`iss`) del token coincide con `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI`.

El conversor personalizado transforma los roles del realm en autoridades de Spring Security:

- `ADMIN` → `ROLE_ADMIN`
- `USER` → `ROLE_USER`

Esto permite proteger métodos con expresiones como `@PreAuthorize("hasRole('ADMIN')")`.

> Los roles guardados en la tabla local `user_roles` no se incorporan automáticamente a los JWT. Define claramente si Keycloak es la fuente de autoridad de los permisos o si existe un mecanismo de sincronización con la base de datos de la aplicación.

## Endpoints

URL base local:

```text
http://localhost:8081/api/v1/users
```

Los endpoints requieren un Bearer token válido y los roles indicados, de acuerdo con la configuración de seguridad actual.

| Método | Ruta | Rol requerido | Descripción |
|---|---|---|---|
| `POST` | `/api/v1/users` | `ADMIN` | Crear un usuario |
| `GET` | `/api/v1/users` | `ADMIN` | Consultar todos los usuarios |
| `GET` | `/api/v1/users/{id}` | `ADMIN` o `USER` | Consultar un usuario por ID |
| `PATCH` | `/api/v1/users/{id}` | `ADMIN` | Actualizar un usuario |
| `DELETE` | `/api/v1/users/{id}` | `ADMIN` | Eliminar un usuario |

**Nota de autorización:** con la regla actual, cualquier usuario con `ROLE_USER` puede intentar consultar cualquier ID. Si los usuarios solo deben consultar su propio perfil, implementa una ruta como `/api/v1/users/me` y vincula la identidad local con el `sub` del JWT, o restringe la consulta por ID a administradores.

### Ejemplo: crear un usuario

El cuerpo exacto depende de los campos definidos en `UserRequestDto`. Ejemplo ilustrativo:

```bash
curl -X POST "http://localhost:8081/api/v1/users" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "usuario_demo",
    "email": "usuario@example.com"
  }'
```

Respuesta esperada: `201 Created`, con el DTO del usuario creado.

### Ejemplo: consultar usuarios

```bash
curl -X GET "http://localhost:8081/api/v1/users" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

Requiere `ROLE_ADMIN`.

### Ejemplo: consultar por ID

```bash
curl -X GET "http://localhost:8081/api/v1/users/1" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### Ejemplo: actualizar un usuario

```bash
curl -X PATCH "http://localhost:8081/api/v1/users/1" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nuevo-correo@example.com"
  }'
```

El ejemplo es ilustrativo: envía únicamente los campos admitidos por `UserUpdateDto` y que tu lógica de actualización permita modificar.

### Ejemplo: eliminar un usuario

```bash
curl -X DELETE "http://localhost:8081/api/v1/users/1" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

La respuesta exitosa es `204 No Content`.

## Manejo de errores

La API centraliza errores MVC mediante `ResponseEntityExceptionHandler` y utiliza `ProblemDetail` para estructurar las respuestas. Los errores de autenticación y autorización que ocurren en la cadena de filtros de Spring Security deben gestionarse con `AuthenticationEntryPoint` y `AccessDeniedHandler`.

Estados previstos:

| Estado | Significado |
|---|---|
| `400 Bad Request` | JSON inválido, parámetros incorrectos o errores de validación |
| `401 Unauthorized` | Falta autenticación o el token no es válido |
| `403 Forbidden` | El usuario autenticado no tiene permisos suficientes |
| `404 Not Found` | El usuario solicitado no existe |
| `406 Not Acceptable` | No se puede producir una representación aceptable |
| `409 Conflict` | Conflicto con un recurso existente, por ejemplo un correo duplicado |
| `415 Unsupported Media Type` | El tipo de contenido enviado no está soportado |

Ejemplo ilustrativo de respuesta:

```json
{
  "type": "about:blank",
  "title": "Validation failed",
  "status": 400,
  "detail": "One or more validation errors occurred.",
  "timestamp": "2026-01-01T12:00:00Z",
  "errors": {
    "email": [
      "El correo no tiene un formato válido."
    ]
  }
}
```

Los campos adicionales y los mensajes definitivos dependen de la implementación del handler y de las validaciones configuradas.

## Modelo de datos

### `User`

Representa un usuario de la aplicación e incluye, entre otros campos:

- `id`: identificador generado por la base de datos.
- `userName`: nombre de usuario único.
- `email`: correo único.
- `enabled`: indicador de habilitación.
- `roles`: relación muchos-a-muchos con `Role`.

### `Role`

Representa un rol de la aplicación. Se relaciona con usuarios mediante la tabla intermedia `user_roles`.

La gestión de los roles locales debe mantenerse diferenciada de los roles de realm emitidos en el JWT, salvo que se implemente una integración explícita entre ambos.

## Buenas prácticas de seguridad

- No utilizar contraseñas de ejemplo en producción.
- No subir tokens, contraseñas ni secretos al repositorio.
- Usar HTTPS en entornos desplegados.
- Validar el emisor y la firma de los JWT.
- Aplicar autorización tanto a nivel de endpoint como a nivel de recurso cuando sea necesario.
- Evitar devolver mensajes internos de excepciones al cliente.
- Mantener restricciones de unicidad en la base de datos y manejar los conflictos de persistencia.
- Revisar la configuración de CSRF si en el futuro se utilizan cookies para autenticar solicitudes.
- Limitar la exposición de los puertos de PostgreSQL y de la consola administrativa de Keycloak.

## Pruebas recomendadas

Antes de publicar una versión, comprueba al menos:

- Creación de usuario con datos válidos e inválidos.
- Rechazo de correos duplicados al crear y actualizar.
- Consulta de un usuario existente y respuesta `404` para uno inexistente.
- Actualización parcial con `PATCH`.
- Eliminación correcta y respuesta `204`.
- Peticiones sin token, con token inválido y con token vencido.
- Acceso con `ROLE_USER` a endpoints reservados para `ROLE_ADMIN`.
- Conversión de los roles de Keycloak a autoridades de Spring Security.
- Inicio de los contenedores con bases de datos disponibles y configuración JWT válida.

## Contribuciones

1. Crea un fork del repositorio.
2. Crea una rama para tu cambio.
3. Realiza los cambios y añade pruebas cuando corresponda.
4. Abre un Pull Request describiendo el propósito y el impacto de los cambios.

## Licencia