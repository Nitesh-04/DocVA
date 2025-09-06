# DocVA – Document Versioning & Audit System

DocVA is a Spring Boot application for managing documents with version control and auditing.  
It allows users to:
- Upload and track multiple document versions
- Retrieve the latest or specific versions
- Automatically log all actions (upload, delete, update)
- Secure APIs with JWT authentication

---

## Features
- **Document Management** – create, update, delete documents
- **Version Control** – maintain multiple versions per document
- **Audit Logging** – every user action is recorded
- **JWT Authentication** – secure endpoints with Bearer tokens
- **DTO Support** – clean API responses decoupled from internal entities

---

## Tech Stack
- Java 17+
- Spring Boot 3.5
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL (configurable)
- Maven

---
