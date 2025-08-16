# Foro Alura – API REST (Spring Boot + MySQL + JWT)

API de foros para gestionar **tópicos**, **usuarios**, **categorías** y **respuestas**.
Incluye JPA/Hibernate, **migraciones Flyway**, validaciones y **autenticación JWT** con Spring Security.

---

## 🧰 Tech stack

* **Java 21**
* **Spring Boot** 4.x (Web, Data JPA, Validation, Security)
* **MySQL 8**
* **Flyway** (migraciones)
* **JJWT** (`io.jsonwebtoken`) para tokens
* **Maven** (con *Maven Wrapper*)

---

## 🚀 Puesta en marcha

### 1) Clonar

```bash
git clone https://github.com/oalvaro77/Foro_Alura.git
cd Foro_Alura
```

### 2) Configurar base de datos (MySQL)

Crea el schema `forohub` o deja que Flyway lo cree. Ajusta `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forohub?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true

spring.flyway.enabled=true

# JWT
app.jwt.secret=pon_un_secreto_largo_y_aleatorio
app.jwt.expiration-seconds=3600
```

### 3) Ejecutar

Con **Maven Wrapper** (no necesitas tener `mvn` instalado):

```bash
# Windows
.\mvnw spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Flyway aplicará automáticamente las migraciones `V1__init_forohub.sql` y `V2__tema_unico_titulo_contenido.sql`.

> ⚠️ Si aparece error de Flyway por *checksum mismatch* o migración fallida, en desarrollo lo más rápido es **borrar el schema** y ejecutar de nuevo (o usar `flyway repair` si lo tienes).

---

## 🗄️ Modelo de datos (resumen)

* **usuarios**: `id, nombre, email (único), password_hash (BCrypt), rol, creado_en`
* **categorias**: `id, nombre (único)`
* **temas**: `id, titulo, contenido, usuario_id, categoria_id, creado_en`

  * Regla: **único por (titulo, contenido)**
* **respuestas**: `id, contenido, tema_id, usuario_id, creado_en`

Migraciones en `src/main/resources/db/migration/`.

---

## 🔐 Autenticación (JWT)

### Login

```
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@dominio.com",
  "password": "su_password"
}
```

**200 OK**

```json
{ "token": "Bearer <jwt>", "expiraEn": 3600 }
```

Usa el token en cada request:

```
Authorization: Bearer <jwt>
```

### Usuario semilla (opcional)

Inserta un usuario con contraseña **BCrypt** (genera el hash con Spring/BCrypt):

```sql
INSERT INTO usuarios (nombre, email, password_hash, rol)
VALUES ('Admin', 'admin@forohub.dev', '$2a$10$<hash_bcrypt>', 'ADMIN');
```

---

## 📚 Endpoints principales

Base URL: `http://localhost:8080`

| Método | URI             | Descripción                          | Auth |
| -----: | --------------- | ------------------------------------ | :--: |
|   POST | `/topicos`      | Crea un tópico                       |   ✅  |
|    GET | `/topicos`      | Lista tópicos (filtros y paginación) |   ✅  |
|    GET | `/topicos/{id}` | Obtiene un tópico por id             |   ✅  |
|    PUT | `/topicos/{id}` | Actualiza un tópico                  |   ✅  |
| DELETE | `/topicos/{id}` | Elimina un tópico                    |   ✅  |
|   POST | `/auth/login`   | Autenticación, devuelve JWT          |   ❌  |

### Crear tópico

```
POST /topicos
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "titulo": "Cómo instalar MySQL",
  "mensaje": "¿Alguien me ayuda con el instalador en Windows?",
  "autorId": 1,
  "cursoId": 1
}
```

**201 Created**

```json
{
  "id": 1,
  "titulo": "Cómo instalar MySQL",
  "mensaje": "¿Alguien me ayuda con el instalador en Windows?",
  "autorId": 1,
  "cursoId": 1,
  "creadoEn": "2025-08-14T00:00:00Z"
}
```

### Listar (filtros + paginación)

```
GET /topicos?curso=Java&anio=2025&page=0&size=10&sort=creadoEn,ASC
Authorization: Bearer <jwt>
```

Devuelve un `Page<TopicoResponse>` (estructura estándar de Spring Data).

### Obtener por id

```
GET /topicos/1
Authorization: Bearer <jwt>
```

### Actualizar

```
PUT /topicos/1
Authorization: Bearer <jwt>
Content-Type: application/json

{
  "titulo": "Cómo instalar MySQL (actualizado)",
  "mensaje": "Probé con la versión 9.0 y funcionó.",
  "autorId": 1,
  "cursoId": 1
}
```

### Eliminar

```
DELETE /topicos/1
Authorization: Bearer <jwt>
```

**204 No Content**

---

## ✅ Reglas de negocio

* **Validación** con Bean Validation (`@NotBlank`, `@NotNull`, `@Valid`).
* **No se permiten duplicados** por `(titulo, mensaje)`.
* Listado con **paginación** y filtros por **curso (categoría)** y **año**.
* Respuestas coherentes:

  * `201 Created`, `200 OK`, `204 No Content`
  * `400 Bad Request` (datos inválidos)
  * `404 Not Found` (no existe)
  * `409 Conflict` (duplicado)

---

## 🧱 Estructura del proyecto

```
src/main/java/com/foro/demo
├─ config / security
│  ├─ SecurityConfig.java        # HttpSecurity, filtros, CORS
│  ├─ JwtAuthFilter.java         # Valida el Bearer token por request
│  └─ JwtService.java            # Generar / validar JWT
├─ domain
│  ├─ Tema.java  Usuario.java  Categoria.java  Respuesta.java
├─ dto
│  ├─ TopicoRequest.java         # titulo, mensaje, autorId, cursoId
│  └─ TopicoResponse.java        # id, titulo, mensaje, autorId, cursoId, creadoEn
├─ repository
│  ├─ TemaRepository.java        # queries + existsByTitulo...Contenido...
│  ├─ UsuarioRepository.java
│  └─ CategoriaRepository.java
├─ service
│  └─ TopicoService.java         # reglas (crear, listar, obtener, actualizar, eliminar)
└─ web
   ├─ AuthController.java        # /auth/login
   └─ TopicoController.java      # /topicos CRUD
```

---

## 🧪 Colección Postman (sugerencia)

Incluye en el repo una colección con:

1. **Auth / Login**
2. **Topicos / Crear**
3. **Topicos / Listar**
4. **Topicos / Obtener por id**
5. **Topicos / Actualizar**
6. **Topicos / Eliminar**

---

## 🩹 Troubleshooting

* **`mvn` no reconocido (Windows)** → usa Maven Wrapper:
  `.\n  mvnw -v` / `.
  mvnw spring-boot:run`
* **Flyway “checksum mismatch”** → borra la BD local o usa `flyway repair`. No edites migraciones ya aplicadas.
* **Índices sobre `TEXT` en MySQL** → si necesitas índices únicos, usa `VARCHAR` o especifica longitud.
* **401 con JWT** → verifica cabecera `Authorization: Bearer <token>` y que `app.jwt.secret` coincida con el usado al firmar.

---

## 📜 Licencia

Proyecto educativo para el **Challenge Foro Alura**. Siéntete libre de adaptarlo y mejorarlo.

---

## 👤 Autor

**oalvaro77** – Pull requests e *issues* son bienvenidos ✨
