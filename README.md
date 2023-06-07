# Student and School Microservices

## Description
Here are two simple RESTful web services. Microservices communicate with Feign Client between them.

### Student API Endpoints
***baseUrl:*** ***http://127.0.0.1:8181/v1/students***
* ***GET*** `/{id}`
* ***GET*** `/`
* ***GET*** `/school/{schoolId}`
* ***POST*** `/`
* ***DELETE*** `/{id}`
* ***PUT*** `/{id}`
### School API Endpoints
***baseUrl:*** ***http://127.0.0.1:8282/v1/schools***
* ***GET*** `/{id}`
* ***GET*** `/`
* ***GET*** `/{id}/students`
* ***POST*** `/`
* ***DELETE*** `/{id}`
* ***PUT*** `/{id}`

## Used Technologies

- Spring Boot
- Spring Data
- Gradle
- Docker
- Feign Client
- PostgreSQL
- Liquibase
- Lombok
- JUnit5


## Requirements
- Java 17
- Docker
> **Note:** Add JAVA_HOME path environment variable with the value of Java 17 folder

## Installation

1. Clone this repository

   ```bash
   git clone https://github.com/turqudquliyev/microservice.git
   ```

2. Go to microservice directory
   ```bash
   cd microservice
   ```

## Run
1. Build school-0.1.jar file on terminal
   ```bash
   ./gradlew bootJar --build-file /school/build.gradle
   ```

2. Build student-0.1.jar file on terminal
   ```bash
   ./gradlew bootJar --build-file /student/build.gradle
   ```

3. Run Docker and execute following command:
   ```bash
   docker-compose up --build -d
   ```


## Usage

You can test the APIs import Postman collection.

If you want see database structure:


&ensp;
You can visit [PgAdmin](http://localhost:8080)
```
Email: root@pgadmin.com
Password: password
```

&ensp;
or

```bash
docker exec -it school-db /bin/sh
```
```
PGPASSWORD=password psql -U root -d school
```
```
SELECT * FROM schools;
```    

&ensp;
and run these commands for student database


## Test

You can also run Unit and Integration tests by following commands:

   ```bash
   ./gradlew test --build-file /school/build.gradle
   ```

   ```bash
   ./gradlew test --build-file /student/build.gradle
   ```