# SWYNEX-Deployable-Backend-Service

**SWYNEX Internship – Task 4: Deployable Backend Service**

A REST API for managing tasks, built with **Java 17, Spring Boot 3, Spring Data JPA (Hibernate) and MySQL**.
Another developer can clone it, run it with one command, or deploy it with Docker / Render.

- **GitHub repository:** `<paste your GitHub repo URL here>`
- **Live service URL:** `<paste your live URL here>`
- **Swagger API docs (live):** `<live URL>/swagger-ui.html`

## Features
- Full CRUD for tasks with filtering, search and pagination
- Bean validation and clean JSON error responses (400 / 401 / 404)
- Optional API-key protection for write endpoints (`API_KEY` env variable)
- Health check (`/actuator/health`) and statistics endpoint
- Swagger UI generated automatically
- Sample data auto-loaded on startup (`src/main/resources/sample_data.json`)
- Automated tests (JUnit 5 + MockMvc), Dockerfile, `docker-compose.yml` (app + MySQL), `render.yaml`
- Postman collection in `postman/`

## Requirements
- JDK 17 or newer
- Maven 3.9+ (or use the Maven built into Eclipse / IntelliJ)
- MySQL 8 running on `localhost:3306` (user `root`, password `root` by default – change via
  `DB_USER` / `DB_PASSWORD`). The `swynex_tasks` database and its tables are created automatically.

## Run
```bash
mvn spring-boot:run
```
Open http://localhost:8080/swagger-ui.html

**No MySQL installed?** Use the in-memory demo profile:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

**Or start everything with Docker:**
```bash
docker compose up --build
```

## Run tests
```bash
mvn test
```

## Build a runnable jar
```bash
mvn clean package
java -jar target/swynex-backend-service-1.0.0.jar
```

## Project structure
```
src/main/java/com/swynex/backend/
    BackendApplication.java
    controller/   REST endpoints
    service/      business logic + sample data loader
    repository/   Spring Data JPA repository
    model/        Task entity, Status, Priority enums
    dto/          request objects with validation
    config/       API-key filter, startup data loader
    exception/    global error handling
src/main/resources/   application.properties, application-h2.properties, sample_data.json
postman/              Postman collection (import into Postman)
src/test/java/        tests
docs/                 API.md, DEPLOYMENT.md
Dockerfile  docker-compose.yml  render.yaml  pom.xml
```

## Endpoints (summary)
| Method | Path | Purpose |
|--------|------|---------|
| GET | `/actuator/health` | Health check |
| GET | `/api/tasks` | List / filter / search tasks |
| GET | `/api/tasks/{id}` | Get a task |
| POST | `/api/tasks` | Create a task |
| PUT | `/api/tasks/{id}` | Update a task |
| DELETE | `/api/tasks/{id}` | Delete a task |
| GET | `/api/stats` | Task statistics |
| POST | `/api/seed` | Reset to sample data |

Full details with curl examples: [docs/API.md](docs/API.md)
Deployment guide (local, Docker, Render, Eclipse): [docs/DEPLOYMENT.md](docs/DEPLOYMENT.md)

## Tech stack
Java 17 · Spring Boot 3.3 · Spring Web · Spring Data JPA · Hibernate · MySQL · Maven · Swagger/OpenAPI · Postman · JUnit 5 · Docker · Git/GitHub · Eclipse

---
Built by `<your name>` as part of the SWYNEX Technologies internship.
