# Task Management Application

## Overview
A Java-based task management system enabling user registration, login, task tracking, and performance dashboard.

## Features
- User Registration and Authentication
- Task Creation and Management
- User Dashboard with Task Progress
- Password Recovery
- Task Performance Tracking

## Technical Components
- User Management
- Task Tracking
- Dashboard Reporting
- File-based Data Storage

## Prerequisites
- Java Development Kit (JDK)
- Java Runtime Environment

## Project Structure
```
project-root/
│
├── Users/
│   ├── user_credentials.txt
│   └── [username].txt
│
├── MainProgram.java
├── UserManagementApp.java
├── Dashboard.java
└── TaskManager.java
```

## Key Classes
- `MainProgram`: Application entry point
- `UserManagementApp`: User authentication
- `Dashboard`: Task tracking and reporting
- `TaskManager`: Interface for task operations

## Usage
1. Compile Java files
2. Run `MainProgram`
3. Choose registration or login
4. Manage tasks through dashboard

## Security Features
- Password complexity requirements
- Mobile number validation
- Username uniqueness check

## Data Persistence
- User credentials stored in text files
- Task information per user
