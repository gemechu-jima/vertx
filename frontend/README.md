# Angular Frontend

This is the Angular frontend for the CRUD application.

## Project Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── services/
│   │   │   └── item.service.ts      (API service)
│   │   ├── app.component.ts         (Main component)
│   │   ├── app.component.html       (Template)
│   │   └── app.component.css        (Styles)
│   ├── main.ts                      (Entry point)
│   ├── index.html                   (HTML page)
│   └── styles.css                   (Global styles)
├── package.json                     (Dependencies)
├── angular.json                     (Angular config)
├── tsconfig.json                    (TypeScript config)
└── README.md                        (This file)
```

## Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Start the development server:
```bash
npm start
```

The app will run on `http://localhost:4200`

## Build

To build for production:
```bash
npm run build
```

## Features

- **View Items**: Display all items from the Vert.x backend
- **Add Item**: Create a new item with name and description
- **Edit Item**: Update existing item details
- **Delete Item**: Remove an item

## Backend Connection

The frontend connects to the Vert.x backend at `http://localhost:8888/items`

Make sure the Vert.x server is running before starting the Angular app.

## API Endpoints

- `GET /items` - Get all items
- `GET /items/:id` - Get single item
- `POST /items` - Create new item
- `PUT /items/:id` - Update item
- `DELETE /items/:id` - Delete item
