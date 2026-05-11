# Kanban Board

A Java desktop Kanban board application built with Swing and SQLite. This project was created to manage task cards across a simple workflow while demonstrating object-oriented programming, database persistence, and separation between the user interface and data access logic.

## Features

- Create, view, edit, move, and delete task cards
- Organise cards by status:
  - To Do
  - In Progress
  - Done
- Separate regular and expedite task lanes
- Store task details including title, description, owner, assignee, due date, status, and expedite flag
- Save card data using a local SQLite database
- Use DAO classes to keep SQL/database logic separate from the UI layer

## Tech Stack

- Java
- Java Swing
- SQLite
- JDBC
- Apache NetBeans
- Ant build system

## Project Structure

```txt
src/
├── dao/              # Database access logic
├── db/               # Database connection handling
├── model/            # Card model classes
├── ui/               # Swing user interface
└── virtualkanban/    # Application entry point
