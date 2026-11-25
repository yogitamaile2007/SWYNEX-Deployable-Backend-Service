# API Documentation

Interactive docs: **`/swagger-ui.html`** (raw spec at `/v3/api-docs`).
Postman: import `postman/SWYNEX-Task-API.postman_collection.json` (set the `baseUrl` and `apiKey` variables).
Base URL: `http://localhost:8080` (or your live URL)

| Method | Endpoint | Description | Key needed* |
|--------|----------|-------------|-------------|
| GET | `/actuator/health` | Health check | No |
| GET | `/api/tasks` | List tasks. Query: `status`, `priority`, `q`, `page` (0-based), `size` (1-200) | No |
| GET | `/api/tasks/{id}` | Get one task | No |
| POST | `/api/tasks` | Create a task | Yes |
| PUT | `/api/tasks/{id}` | Partial update | Yes |
| DELETE | `/api/tasks/{id}` | Delete a task | Yes |
| GET | `/api/stats` | Counts by status and priority | No |
| POST | `/api/seed` | Reset to sample data | Yes |

\* Only enforced when the `API_KEY` environment variable is set. Send it as header `X-API-Key: <key>`.

## Field values
- `status`: `TODO`, `IN_PROGRESS`, `DONE` (default `TODO`)
- `priority`: `LOW`, `MEDIUM`, `HIGH` (default `MEDIUM`)
- `title`: required on create, max 120 characters
- `description`: max 1000 characters

## Examples
```bash
# List high-priority tasks
curl "http://localhost:8080/api/tasks?priority=HIGH"

# Create
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" -H "X-API-Key: change-me" \
  -d '{"title":"Review pull request","priority":"HIGH"}'

# Update
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" -H "X-API-Key: change-me" \
  -d '{"status":"DONE"}'

# Delete
curl -X DELETE http://localhost:8080/api/tasks/1 -H "X-API-Key: change-me"
```

Sample response:
```json
{
  "id": 1,
  "title": "Review pull request",
  "description": "",
  "status": "TODO",
  "priority": "HIGH",
  "createdAt": "2026-11-14T15:30:00.123",
  "updatedAt": "2026-11-14T15:30:00.123"
}
```

## Error responses
| Code | Meaning |
|------|---------|
| 400 | Validation error / invalid enum value / malformed JSON |
| 401 | Missing or invalid API key |
| 404 | Task not found |
