# Deployment Notes

## 1. Run locally
1. Install MySQL 8 and make sure it is running (user `root`, password `root`, or set `DB_USER` / `DB_PASSWORD`).
2. Start the app:
```bash
mvn spring-boot:run
```
The database `swynex_tasks` and the `tasks` table are created automatically by Hibernate, and sample data
loads on first start. Open http://localhost:8080/swagger-ui.html.

Without MySQL (demo only): `mvn spring-boot:run -Dspring-boot.run.profiles=h2`

Test the API with Postman: import `postman/SWYNEX-Task-API.postman_collection.json`.

## 2. Run the jar
```bash
mvn clean package
java -jar target/swynex-backend-service-1.0.0.jar
```

## 3. Run with Docker (app + MySQL)
```bash
docker compose up --build
```

## 4. Deploy on Railway (easiest with MySQL)
1. Push this repo to GitHub.
2. On https://railway.app: **New Project → Deploy from GitHub repo** (the `Dockerfile` is detected).
3. In the same project click **+ New → Database → MySQL**.
4. Open your service → **Variables** and add (Railway fills the `${{MySQL.*}}` references):
   - `DB_URL` = `jdbc:mysql://${{MySQL.MYSQLHOST}}:${{MySQL.MYSQLPORT}}/${{MySQL.MYSQLDATABASE}}?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`
   - `DB_USER` = `${{MySQL.MYSQLUSER}}`
   - `DB_PASSWORD` = `${{MySQL.MYSQLPASSWORD}}`
   - `API_KEY` = any secret (optional)
5. Service → **Settings → Networking → Generate Domain**, then open `<domain>/swagger-ui.html`.

## 5. Deploy on Render
Render's web service works with the same `Dockerfile`, but it has no free MySQL, so point
`DB_URL`, `DB_USER`, `DB_PASSWORD` at an external MySQL (e.g. the Railway database, Aiven free tier).
Set the health check path to `/actuator/health`.

The app reads the `PORT` variable automatically on both platforms.

## Environment variables
| Variable | Purpose | Default |
|----------|---------|---------|
| `PORT` | HTTP port | 8080 |
| `API_KEY` | Protects POST/PUT/DELETE when set | empty (open) |
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | MySQL connection | `localhost:3306/swynex_tasks`, root/root |

## Notes
- Tables are managed by Hibernate (`ddl-auto=update`). Sample data is inserted only when the table is empty.
- Tests use an in-memory H2 database, so `mvn test` does not need MySQL.

## Opening in Eclipse
1. Unzip the project (do not import the zip itself).
2. **File → Import → Maven → Existing Maven Projects** → select the unzipped folder (it contains `pom.xml`) → Finish.
   Wait for the dependency download to finish (bottom-right progress bar).
3. Make sure Eclipse uses JDK 17+: right-click project → Properties → Java Build Path → Libraries.
4. Start MySQL, then run: open `BackendApplication.java` → right-click → **Run As → Java Application**
   (or Spring Tools: Run As → Spring Boot App).
5. Open http://localhost:8080/swagger-ui.html
