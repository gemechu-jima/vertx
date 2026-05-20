# Full-Stack CRUD Application (Vert.x + Angular)

A complete full-stack CRUD application with:
- **Backend**: Vert.x 5.0.12 (Java) with RESTful API
- **Frontend**: Angular 17 with TypeScript

## Project Structure

```
starter/
├── backend/                         (Vert.x backend)
│   ├── src/
│   │   ├── main/java/
│   │   │   └── com/example/starter/
│   │   │       └── MainVerticle.java    (CRUD API)
│   │   └── test/java/
│   ├── pom.xml                      (Maven config)
│   ├── README.adoc                  (Backend docs)
│   └── ...
│
├── frontend/                        (Angular frontend)
│   ├── src/
│   │   ├── app/
│   │   │   ├── services/
│   │   │   │   └── item.service.ts
│   │   │   ├── app.component.ts
│   │   │   ├── app.component.html
│   │   │   └── app.component.css
│   │   ├── main.ts
│   │   └── index.html
│   ├── package.json
│   ├── angular.json
│   ├── README.md                    (Frontend docs)
│   └── ...
│
└── README.md                        (This file)
```

## Quick Start

### Backend (Vert.x)

```bash
cd /home/hp/Downloads/starter
./mvnw clean compile exec:java
```

Server runs on `http://localhost:8888`

### Frontend (Angular)

In a new terminal:

```bash
cd /home/hp/Downloads/starter/frontend
npm install
npm start
```

App opens on `http://localhost:4200`

## API Endpoints

All endpoints return JSON and run on `http://localhost:8888`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/items` | Get all items |
| GET | `/items/:id` | Get single item |
| POST | `/items` | Create new item |
| PUT | `/items/:id` | Update item |
| DELETE | `/items/:id` | Delete item |

## Features

### Backend (Vert.x)
- REST API for CRUD operations
- JSON request/response handling
- In-memory data storage
- Sample data on startup
- Error handling with proper HTTP status codes

### Frontend (Angular)
- Display all items in a table/cards
- Add new items with form
- Edit existing items inline
- Delete items with confirmation
- Real-time UI updates
- Loading states
- Error handling

## Development Workflow

1. **Backend**: Edit `src/main/java/com/example/starter/MainVerticle.java`
2. **Frontend**: Edit files in `frontend/src/app/`
3. Both support hot reload during development

## Testing the API

### Using curl

List all items:
```bash
curl http://localhost:8888/items
```

Create new item:
```bash
curl -X POST http://localhost:8888/items \
  -H "Content-Type: application/json" \
  -d '{"name":"My Item","description":"Test"}'
```

Get specific item:
```bash
curl http://localhost:8888/items/1
```

Update item:
```bash
curl -X PUT http://localhost:8888/items/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated","description":"New desc"}'
```

Delete item:
```bash
curl -X DELETE http://localhost:8888/items/1
```

## Tools & Versions

- **Java**: 17+
- **Maven**: 3.8+
- **Node.js**: 18+
- **Angular**: 17
- **Vert.x**: 5.0.12
- **TypeScript**: 5.2

## Folder Organization

- **backend/** — All Vert.x/Java code (keep as is)
- **frontend/** — All Angular/TypeScript code (keep as is)

Keep them in separate folders for:
- Independent deployment
- Different build processes
- Clear separation of concerns
- Easier scaling

## Next Steps

1. Add database support (MongoDB, PostgreSQL)
2. Add authentication/authorization
3. Add more complex business logic
4. Deploy to cloud (AWS, GCP, Azure)
5. Add unit tests
6. Add integration tests
