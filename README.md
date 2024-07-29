# Java Challenge

Se requiere desarrollar una aplicación en Java + Spring Boot que permita realizar las siguientes operaciones:

- Crear un producto
- Actualizar un producto
- Eliminar un producto
- Listar todos los productos
- Listar un producto por Id
- Buscar productos por nombre

La aplicación debe contar con una base de datos (en memoria o tradicional) y debe exponer una API REST que permita realizar las operaciones mencionadas. Además, se debe implementar un sistema de autenticación para acceder a las operaciones (puede ser algo tan simple como basic auth).
Cada vez que se crea un producto, se debe almacenar estadísticas de uso del sistema (a criterio, por ejemplo cantidad de productos en una categoría). Esta información debe ser generada por un servicio que se ejecute de forma asíncrona y puede ser consultada a través de un endpoint de la API.

## Requerimientos de resolución

- Docker
- Java 17

## Iniciar aplicación

Para iniciar la aplicación en modo local debes tener ejecutando el demonio Docker.


```shell
#!/bin/bash

# init database
./gradlew databaseStart

# inti app
./gradlew bootRun

# stop and remove database
./gradlew databaseStop

```

La configuración base es la siguiente:

```text
spring.application.name=java-challenge
spring.datasource.url=jdbc:postgresql://localhost:5432/java-challenge
spring.datasource.username=java-challenge
spring.datasource.password=java-challenge
spring.datasource.platform=postgres
spring.datasource.driverClassName: org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.security.user.name=java
spring.security.user.password=java
```
La aplicación estará ejecutándose en [http://localhost:8080](http://localhost:8080).


### Swagger UI

Encontrarás la documentación y acceso a la API [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) mediante Swagger.

### Autenticación

La aplicación tiene autenticación básica con las siguientes credenciales:

- **USERNAME**: java
- **PASSWORD**: java

## Testing

El proyecto contiene pruebas unitarias las cuales puedes ejecutar de la siguiente manera.

```shell
#!/bin/bash
./gradlew test
```

## Despliegue local

Puedes desplegar la aplicación con Docker de la siguiente manera:

```shell
#!/bin/bash

# deploy app and infra local
./gradlew infraStart

# stop app and infra
./gradlew infraStop

```

> Estas tareas personalizadas por debajo ejecutarán Docker Compose.

## Gradle tasks

Tareas personalizadas creadas de Gradle.

```text
Infrastructure tasks
--------------------
databaseStart - Start the application PostgreSQL database
databaseStop - Stop and remove the application PostgreSQL database
infraStart - Deploy local infrastructure with database and application running
infraStop - Stop local infrastructure
```
## Créditos

Este proyecto fue desarrollado por:

- **Benjamín** - Desarrollador Principal

### Tecnologías
- Java 17
- Spring 3.3.2
- Docker
- TestContainers
- JUnit
- SpringSecurity
- SpringJPA
- OpenApi

---

Desarrollado con 💙 usando [Java](https://www.java.com), [Spring Boot](https://spring.io/projects/spring-boot), y [Docker](https://www.docker.com). 
