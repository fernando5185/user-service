# User Service API

API REST para la gestión de usuarios, desarrollada con Spring Boot. Utiliza PostgreSQL para la persistencia de datos, Keycloak como proveedor de identidad y Spring Security OAuth2 Resource Server para proteger los endpoints mediante tokens JWT.

El proyecto aplica separación de responsabilidades, validación de datos mediante DTOs, mapeo con MapStruct y manejo centralizado de errores mediante ProblemDetail.


## Características

- Operaciones CRUD para la gestión de usuarios.
- Identificadores UUID para los usuarios.
- Validación de solicitudes con DTOs y Bean Validation.
- Persistencia relacional con PostgreSQL y Spring Data JPA.
- Mapeo entre entidades y DTOs mediante MapStruct.
- Autenticación basada en JWT emitidos por Keycloak.
- Autorización mediante roles y autoridades de Spring Security.
- Manejo centralizado de errores HTTP con ProblemDetail.
- Actualización parcial de usuarios mediante PATCH.
- Restricciones de unicidad para los datos que lo requieren.
- Entorno de desarrollo local con Docker Compose.
- Pruebas automatizadas mediante las herramientas configuradas en el proyecto.

## Tecnologías
| Tecnologia | Próposito |
|---|---|
| Java | Lenguaje de programación |
| Spring Boot | Framework principal |
| Spring Web / Spring MVC | Desarrollo de endpoints REST |
| Spring Security | Seguridad y autorización |
| OAuth 2.0 Resource Server | Validación de tokens de acceso |
| JWT | Representación de tokens de acceso |
| Keycloak | Gestión de identidades, clientes y roles |
| Hibernate | Implementación ORM |
| PostgreSQL | Gestión de dependencias y construcción |
| Docker Docker Compose | Ejecución del entorno local |

## Versiones
| Componente | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |

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
La arquitectura separa las responsabilidades de autenticación, autorización, exposición de endpoints, lógica de negocio y persistencia.

Si deseas ejecutar la aplicación directamente desde tu máquina, también necesitarás Java 21 y Maven.

## Requisitos previos

Para ejecutar el entorno local necesitas:

- Docker Engine o Docker Desktop.
- Docker Compose.
- Git.
- Un cliente HTTP, por ejemplo curl, Postman o Insomnia.

Si vas a ejecutar la aplicación fuera de Docker, también necesitarás la versión de Java y la herramienta de construcción configuradas en el proyecto.

## Instalación y configuración

1. Clonar el repositorio

```bash
git clone https://github.com/fernando5185/user-service.git
cd user-service
```
Si trabajas en una rama específica, cambia a ella antes de iniciar el proyecto.

2. Revisar la configuración

- Comprueba los siguientes archivos, según estén presentes en el repositorio:
- docker-compose.yml: definición de los servicios y sus redes.
- Dockerfile: construcción de la imagen de la API.
- pom.xml: dependencias, plugins y configuración de Maven.
- src/main/resources/application.yml o application.properties: configuración de Spring Boot.
- Configuración de Keycloak: realm, clientes, roles y usuarios.
- Scripts de inicialización de PostgreSQL, si existen.

No asumas que las bases de datos o los roles se crean automáticamente: verifica los scripts y la configuración efectiva.

3. Iniciar el entorno

Desde el directorio que contiene el archivo `docker-compose.yml`:

```bash
docker compose up --build -d
```

Consulta los contendores de tu proyecto mostrando el estado actual:

```bash
docker compose ps
```

Consulta el estado, puertos, nombre, imagen y servicio asociado de los contendores:

```bash
docker compose ps -a
```

Consulta los logs de la aplicación:

```bash
docker compose logs -f app
```

Consulta los logs de Keycloak:

```bash
docker compose logs -f keycloak
```

4. Detener el entorno

Para detener los servicios:

```bash
docker compose down
```

Para detener los servicios y eliminar también el volumen de datos de PostgreSQL:

```bash
docker compose down -v
```

> **Precaución**: el segundo comando puede eliminar los datos persistidos de PostgreSQL y otros volúmenes declarados en Compose. Utilízalo únicamente cuando realmente quieras reiniciar el entorno y hayas respaldado los datos necesarios.

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

### Puertos locales

Según la configuración de Compose compartida:

| Servicio | Puerto del host |
|---|---:|
| API | `8081` |
| Keycloak | `8080` |
| PostgreSQL | `5432` |

Keycloak necesita una base de datos disponible y la base de datos `keycloak` debe existir. Si solo configuras `POSTGRES_DB=user_service`, comprueba que un script de inicialización cree también la base `keycloak` antes de que Keycloak intente conectarse.

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

## Consideraciones importantes:

- Dentro de Docker Compose, la API debe conectarse a PostgreSQL mediante el nombre del servicio, por ejemplo postgres, no mediante localhost.
- localhost dentro de un contenedor apunta al propio contenedor.
- host.docker.internal permite acceder desde el contenedor al host en entornos compatibles, pero su disponibilidad depende del sistema operativo y de la configuración de Docker.
- El valor de SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI debe coincidir exactamente con el claim iss del JWT.
- La URL de las claves públicas debe ser accesible desde la API.
- Si se configura explícitamente JWK_SET_URI, verifica que la configuración de seguridad mantenga también la validación del emisor.

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
| `GET` | `/api/v1/users/{id}` | `ADMIN` | Consultar un usuario por ID |
| `PATCH` | `/api/v1/users/{id}` | `ADMIN` | Actualizar un usuario |
| `DELETE` | `/api/v1/users/{id}` | `ADMIN` | Eliminar un usuario |

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
curl -X GET "http://localhost:8081/api/v1/users/7cbdb35f-a5cf-410f-abd8-15b4ab1ff6ac" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

### Ejemplo: actualizar un usuario

```bash
curl -X PATCH "http://localhost:8081/api/v1/users/7cbdb35f-a5cf-410f-abd8-15b4ab1ff6ac" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "nuevo-correo@example.com"
  }'
```

El ejemplo es ilustrativo: envía únicamente los campos admitidos por `UserUpdateDto` y que tu lógica de actualización permita modificar.

### Ejemplo: eliminar un usuario

```bash
curl -X DELETE "http://localhost:8081/api/v1/users/7cbdb35f-a5cf-410f-abd8-15b4ab1ff6ac" \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

La respuesta exitosa es `204 No Content`.

## Manejo de errores

La API centraliza errores MVC mediante `ResponseEntityExceptionHandler` y utiliza `ProblemDetail` para estructurar las respuestas. Los errores de autenticación y autorización que ocurren en la cadena de filtros de Spring Security se gestionan con `AuthenticationEntryPoint` y `AccessDeniedHandler`.

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

## Credits

Made with ❤️ by [fernando5185](https://github.com/fernando5185), licensed under [MIT](https://github.com/fernando5185/user-service/blob/feature-api/LICENSE).
