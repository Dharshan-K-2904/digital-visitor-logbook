# 📋 Digital Visitor Logbook with Approval Flow

> **UE23CS352B – Object Oriented Analysis & Design | Mini Project**  
> Department of Computer Science and Engineering  
> PES University, Bengaluru – 560 085, Karnataka, India  
> Semester: January – May 2026

---

## 👥 Team Members

| Name | SRN | Module |
|------|-----|--------|
| Balachandragouda Kallappagouda Patil | PES1UG23CS135 | Visitor Registration & Profile Management |
| Balaji Darshan V S | PES1UG23CS136 | Host Approval and Rejection Workflow |
| Chethan M S | PES1UG23CS163 | Visitor Check-In and Check-Out Logging |
| Dharshan K | PES1UG23CS184 | Admin Dashboard and Report Generation |

**Faculty Guide:** Shridevi A Sawant

---

## 📌 Problem Statement

Most organizations still rely on paper-based visitor registers, which are inefficient, insecure, and difficult to audit. The **Digital Visitor Logbook with Approval Flow** replaces manual entry management with a structured digital system that enforces host-based authorization before granting visitor access, while maintaining accurate, searchable records of all entries and exits.

---

## ✨ Key Features

- 🔐 **Role-based login** — Visitor, Host, Security Personnel, and Admin portals
- 📝 **Digital visit request submission** by visitors with purpose and host selection
- ✅ **Host approval / rejection workflow** with real-time notification support
- 🚪 **Security check-in and check-out logging** at the entry point
- ⏱️ **Automatic visit duration calculation** after checkout
- 📊 **Admin dashboard** with searchable audit log and report export (CSV / PDF / Excel)
- 👤 **Profile management** for all user roles
- 🏭 **User account management** by administrators

---

## 🛠️ Technology Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.5.13 (Spring MVC) |
| Template Engine | Thymeleaf |
| Database | MySQL 8.x |
| ORM / Data Access | Spring Data JPA + Spring JDBC |
| Build Tool | Apache Maven |
| Utilities | Lombok 1.18.38 |
| Dev Tooling | Spring Boot DevTools |

---

## 📦 Dependencies (pom.xml)

| Dependency | Group ID | Purpose |
|------------|----------|---------|
| `spring-boot-starter-web` | org.springframework.boot | Spring MVC web layer |
| `spring-boot-starter-thymeleaf` | org.springframework.boot | Server-side HTML templating |
| `spring-boot-starter-jdbc` | org.springframework.boot | Raw JDBC access via `JdbcTemplate` |
| `spring-boot-starter-data-jpa` | org.springframework.boot | JPA / Hibernate ORM |
| `mysql-connector-j` | com.mysql | MySQL JDBC driver (runtime) |
| `lombok` | org.projectlombok | Boilerplate code reduction (provided) |
| `spring-boot-devtools` | org.springframework.boot | Live reload during development (runtime/optional) |
| `spring-boot-starter-test` | org.springframework.boot | JUnit & Mockito testing support (test) |

---

## 🏗️ Architecture & Design

### MVC Architecture

The project follows **Spring MVC**:
- **Model** — `User`, `VisitRequest`, `UserRole` entities represent the domain.
- **Controller** — `AdminController`, `AuthController`, `HostController`, `SecurityController`, `VisitorController` handle HTTP requests.
- **View** — Thymeleaf HTML templates in `src/main/resources/templates/`.

### Design Patterns Implemented

| Pattern | Class(es) | How It Is Used |
|---------|-----------|----------------|
| **Singleton** | `DatabaseConnectionManager` | Ensures a single shared DB connection manager across the application lifecycle |
| **Factory** | `UserFactory`, `UserFactoryRegistry`, `AdminUserFactory`, `HostUserFactory`, `VisitorUserFactory`, `SecurityUserFactory` | Creates the correct `User` type based on the assigned role |
| **Observer** | `Subject`, `Observer`, `VisitRequestSubject`, `NotificationObserver` | Notifies hosts automatically when a new visit request is submitted |
| **Strategy** | `ReportStrategy`, `ReportContext`, `CsvReportStrategy`, `PdfReportStrategy`, `ExcelReportStrategy` | Selects the appropriate report format (CSV / PDF / Excel) at runtime |
| **DAO** | `UserDAO`, `VisitRequestDAO` (+ Impl classes) | Abstracts all MySQL queries from business logic |

### Design Principles Applied

| Principle | How It Is Applied |
|-----------|------------------|
| **Single Responsibility** | Each class handles exactly one concern (e.g., `VisitorController` handles only visitor-related requests) |
| **Open/Closed** | New user roles can be added as new `UserFactory` subclasses without modifying existing role logic |
| **Dependency Inversion** | Controllers depend on DAO/Service *interfaces*, not concrete implementations |
| **Separation of Concerns** | UI (Thymeleaf), business logic (Services), and data access (DAOs) are kept in strictly separate layers |

---

## 👤 User Roles & Responsibilities

| Role | Responsibilities |
|------|----------------|
| **Visitor** | Self-register, submit visit requests with purpose and host details, view request status |
| **Host (Employee)** | Review pending visit requests, approve or reject with reason, view visit history |
| **Security Personnel** | Verify approval status at entry, record check-in and check-out timestamps |
| **Administrator** | Manage all user accounts, monitor full visitor logs, generate audit reports |

---

## ⚙️ Configuration Changes Required Before Running

### 1. Prerequisites — Install These First

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | **17** or later | https://adoptium.net/ |
| Apache Maven | **3.8+** | https://maven.apache.org/download.cgi |
| MySQL Server | **8.0+** | https://dev.mysql.com/downloads/ |

### 2. Create the MySQL Database

Open your MySQL client (MySQL Workbench, DBeaver, or terminal) and run:

```sql
CREATE DATABASE visitor_logbook
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
```

> ⚠️ The database **must** be named `visitor_logbook` exactly as shown above.

### 3. Update `application.properties`

Open `src/main/resources/application.properties` and update the following lines to match **your** MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/visitor_logbook?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
spring.datasource.username=admin        # ← Change to your MySQL username
spring.datasource.password=Admin@2026   # ← Change to your MySQL password
```

> 💡 The default credentials in the file are `admin` / `Admin@2026`. Replace these with your own MySQL user credentials. If you are using the root user, change the username to `root` and set the correct password.

**Full `application.properties` reference:**

```properties
server.port=8080

# ===============================
# MYSQL DATABASE CONFIG
# ===============================
spring.datasource.url=jdbc:mysql://localhost:3306/visitor_logbook?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===============================
# THYMELEAF CONFIG
# ===============================
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.cache=false

# ===============================
# SQL INIT CONFIG
# ===============================
spring.sql.init.mode=always
spring.sql.init.schema-locations=classpath:database/schema.sql
spring.sql.init.data-locations=classpath:database/data.sql
spring.sql.init.continue-on-error=true

# ===============================
# JPA / HIBERNATE CONFIG
# ===============================
spring.jpa.hibernate.ddl-auto=none
spring.jpa.open-in-view=false
spring.jpa.show-sql=true

# ===============================
# LOGGING
# ===============================
logging.level.org.springframework.jdbc.core=DEBUG
```

### 4. Database Schema & Seed Data (Auto-Applied)

The application **automatically** creates the tables and inserts default users on first startup using the SQL files at:

- `src/main/resources/database/schema.sql` — Creates `users` and `visit_requests` tables
- `src/main/resources/database/data.sql` — Inserts four default test accounts

> You do **not** need to run these SQL files manually. Spring Boot's `spring.sql.init.mode=always` handles this.

---

## 🚀 Running the Application

### Step 1 — Clone the Repository

```bash
git clone https://github.com/Dharshan-K-2904/digital-visitor-logbook.git
cd digital-visitor-logbook
```

### Step 2 — Configure Database

Complete the [Configuration Changes](#️-configuration-changes-required-before-running) section above.

### Step 3 — Build & Run

```bash
mvn spring-boot:run
```

Or build the JAR first and then run:

```bash
mvn clean package -DskipTests
java -jar target/digital-visitor-logbook-1.0.0.jar
```

### Step 4 — Open in Browser

```
http://localhost:8080
```

---

## 🔑 Default Test Credentials

These accounts are seeded automatically by `data.sql` on first startup:

| Role | Email | Password |
|------|-------|----------|
| **Admin** | admin@test.com | admin123 |
| **Host** | host@test.com | host123 |
| **Security** | security@test.com | security123 |
| **Visitor** | visitor@test.com | visitor123 |

> ⚠️ Change these credentials immediately in a production environment.

---

## 📁 Project Structure

```
digital-visitor-logbook/
├── src/
│   ├── main/
│   │   ├── java/com/visitorlogbook/
│   │   │   ├── DigitalVisitorLogbookApplication.java   # Spring Boot entry point
│   │   │   ├── config/
│   │   │   │   └── AppConfig.java                      # Spring configuration
│   │   │   ├── controller/
│   │   │   │   ├── AdminController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── HostController.java
│   │   │   │   ├── SecurityController.java
│   │   │   │   └── VisitorController.java
│   │   │   ├── dao/                                     # DAO interfaces & implementations
│   │   │   ├── factory/                                 # Factory Pattern
│   │   │   │   ├── UserFactory.java
│   │   │   │   ├── UserFactoryRegistry.java
│   │   │   │   ├── AdminUserFactory.java
│   │   │   │   ├── HostUserFactory.java
│   │   │   │   ├── SecurityUserFactory.java
│   │   │   │   └── VisitorUserFactory.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── UserRole.java
│   │   │   │   ├── VisitRequest.java
│   │   │   │   └── VisitorRequest.java
│   │   │   ├── observer/                                # Observer Pattern
│   │   │   │   ├── Subject.java
│   │   │   │   ├── Observer.java
│   │   │   │   ├── VisitRequestSubject.java
│   │   │   │   └── NotificationObserver.java
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   │   ├── UserManagementService.java
│   │   │   │   ├── UserManagementServiceImpl.java
│   │   │   │   ├── VisitRequestService.java
│   │   │   │   └── VisitRequestServiceImpl.java
│   │   │   ├── singleton/                               # Singleton Pattern
│   │   │   │   └── DatabaseConnectionManager.java
│   │   │   ├── strategy/                                # Strategy Pattern
│   │   │   │   ├── ReportStrategy.java
│   │   │   │   ├── ReportContext.java
│   │   │   │   ├── CsvReportStrategy.java
│   │   │   │   ├── PdfReportStrategy.java
│   │   │   │   └── ExcelReportStrategy.java
│   │   │   └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── database/
│   │       │   ├── schema.sql                           # Table definitions
│   │       │   └── data.sql                             # Default seed users
│   │       ├── static/                                  # CSS, JS, images
│   │       └── templates/                               # Thymeleaf HTML views
│   │           ├── auth/
│   │           ├── admin/
│   │           ├── host/
│   │           ├── security/
│   │           ├── visitor/
│   │           └── profile.html
│   └── test/
├── docs/
│   └── diagrams/                                        # UML diagrams
├── pom.xml
├── LICENSE
└── README.md
```

---

## 🗄️ Database Schema

```sql
-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'VISITOR',
    active      TINYINT(1)   NOT NULL DEFAULT 1,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Visit Requests Table
CREATE TABLE IF NOT EXISTS visit_requests (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    visitor_id       BIGINT       NOT NULL,
    visitor_name     VARCHAR(100) NOT NULL,
    host_id          BIGINT,
    host_name        VARCHAR(100) NOT NULL,
    purpose          VARCHAR(255) NOT NULL,
    visit_date       DATE         NOT NULL,
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    check_in         DATETIME,
    check_out        DATETIME,
    duration_minutes BIGINT,
    created_at       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (visitor_id) REFERENCES users(id) ON DELETE CASCADE
);
```

---

## 🔗 Repository

**GitHub:** [https://github.com/Dharshan-K-2904/digital-visitor-logbook](https://github.com/Dharshan-K-2904/digital-visitor-logbook)

---

## 📜 License

This project is licensed under the terms of the [LICENSE](LICENSE) file included in this repository.

---

## 🎓 Academic Context

| Field | Details |
|-------|---------|
| Course | UE23CS352B – Object Oriented Analysis & Design |
| Project Type | Mini Project |
| Department | Computer Science and Engineering |
| Institution | PES University, Bengaluru |
| Semester | January – May 2026 |
| Faculty Guide | Shridevi A Sawant |