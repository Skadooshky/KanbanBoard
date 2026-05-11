# KanbanBoard

KanbanBoard is a Java desktop application built using Java Swing and SQLite that simulates a virtual Kanban task management board. The application allows users to create, manage, and organise work items across multiple workflow stages using a visual Kanban-style interface.

This project was developed as part of coursework and focuses on object-oriented programming, GUI development, database integration, layered architecture, and software testing practices.

## Features

- Create and manage Kanban cards/tasks
- Organise tasks into workflow columns:
  - TO DO
  - IN PROGRESS
  - DONE
- Support for swimlanes:
  - REGULAR
  - EXPEDITE
- Add and edit:
  - Title
  - Description
  - Owner
  - Assignee
  - Due dates
- Delete tasks/cards
- Persistent SQLite database storage
- Card selection management
- Responsive desktop UI using Java Swing
- Unit and integration testing support

## Technologies Used

- Java
- Java Swing
- SQLite
- JDBC
- Apache NetBeans
- JUnit

## Architecture

The project follows a layered structure to separate UI logic from database operations.

```text
UI Layer (Swing)
      ↓
Business / Logic Layer
      ↓
DAO Layer
      ↓
SQLite Database
```

This separation improves maintainability and keeps SQL operations outside of the UI layer.

## Project Structure

```text
KanbanBoard/
├── src/
│   ├── ui/                # Swing UI classes
│   ├── dao/               # Database access objects
│   ├── models/            # Data models
│   ├── database/          # Database connection classes
│   ├── services/          # Business logic/services
│   └── utils/             # Helper utilities
│
├── test/                  # Unit and integration tests
│
├── Kanban.db              # SQLite database
├── build.xml
├── manifest.mf
├── README.md
└── nbproject/             # NetBeans project files
```

## Database

The application uses SQLite for persistent storage.

Example fields stored for cards:

- ID
- Title
- Description
- Status
- Owner
- Assignee
- Due Date
- Expedite Flag
- Created Timestamp

## How to Run

### 1. Clone the Repository

```bash
git clone https://github.com/Skadooshky/KanbanBoard.git
```

### 2. Open the Project

Open the project in:

- Apache NetBeans
- IntelliJ IDEA
- Eclipse

### 3. Build the Project

If using NetBeans:

```text
Clean and Build Project
```

Or via terminal:

```bash
javac *.java
```

### 4. Run the Application

Run the main application class from your IDE.

## Running Tests

Using NetBeans or your IDE:

```text
Run > Test Project
```

Or with JUnit through your build configuration.

## Development Focus

This project focused on:

- Java desktop application development
- GUI development with Swing
- SQLite database integration
- DAO architecture patterns
- Separation of concerns
- Object-oriented programming
- Testing and debugging workflows

## Author

Skadooshky
