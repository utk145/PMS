# This file is to include the notes/links of important things to keep handy or for reference while working on the project


## Useful Links
- https://start.spring.io/
- https://docs.spring.io/spring-data/jpa/reference/repositories/query-methods-details.html#repositories.query-methods.query-creation
- https://springdoc.org/
- https://hub.docker.com/

## Environment Variables for Docker 
### patient-service
SPRING_DATASOURCE_PASSWORD=password;
SPRING_DATASOURCE_URL=jdbc:postgresql://patient-service-db:5432/db;
SPRING_DATASOURCE_USERNAME=admin_user;
SPRING_JPA_HIBERNATE_DDL_AUTO=update;
SPRING_SQL_INIT_MODE=always

### patient-service-db
POSTGRES_DB=db;POSTGRES_PASSWORD=password;POSTGRES_USER=admin_user