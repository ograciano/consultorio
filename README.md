# Medical WebFlux App

Aplicación de consultorio médico desarrollada en Java 21 con Spring Boot 3.3.0, WebFlux y R2DBC. Esta app gestiona oficinas médicas y doctores de forma reactiva.

---

## 🧱 Tecnologías utilizadas
- Java 21
- Spring Boot 3.3.0
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL (Docker)
- Docker & Docker Compose
- Swagger UI / OpenAPI (springdoc)

---

## 🚀 ¿Cómo levantar el proyecto?

### 1. Clonar el repositorio
```bash
git clone <repo-url>
cd medical-webflux-app
```

### 2. Crear el JAR
```bash
./gradlew bootJar
```
Esto genera el archivo:
```
build/libs/medical-webflux-app-0.0.1-SNAPSHOT.jar
```

### 3. Levantar el entorno con Docker Compose
```bash
docker-compose up --build
```
Esto inicia:
- PostgreSQL en `localhost:5432` (user/password: `postgres`)
- Aplicación Web en `localhost:8080`

### 4. Verificar que todo esté funcionando
- API abierta en: [http://localhost:8080](http://localhost:8080)
- Documentación Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🗃️ Estructura del proyecto
| Archivo | Descripción |
|--------|-------------|
| `build.gradle` | Configuración de dependencias y compilación |
| `docker-compose.yml` | Infraestructura de la base de datos y aplicación |
| `MedicalApplication.java` | Punto de entrada principal del proyecto |
| `schema.sql` | Script de creación de tablas `offices` y `doctors` con `SERIAL` auto-incrementable |

---

## 🧪 Pruebas
Puedes ejecutar las pruebas unitarias con:
```bash
./gradlew test
```

---

## 🔄 Resetear entorno
Si quieres borrar los datos y reiniciar completamente:
```bash
docker-compose down -v
./gradlew bootJar
docker-compose up --build
```

---

## 📝 Notas adicionales
- Los registros no se eliminan físicamente; se usa `is_deleted` para borrado lógico.
- La base de datos se inicializa con `schema.sql`.

---

> Proyecto desarrollado como prueba técnica con enfoque en reactividad, limpieza y buenas prácticas.