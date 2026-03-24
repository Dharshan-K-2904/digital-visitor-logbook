Here is the complete, formatted **README.md** file for your project. I have integrated all your requirements into a professional, scannable document that follows standard software documentation practices.

````markdown
# 📘 Digital Visitor Logbook System

## 👨‍💻 Module: Visitor Registration & Authentication
**Project:** Digital Visitor Logbook with Approval Flow  
**Focus:** User Onboarding, Identity Management, and Security

This module serves as the core foundation for the Digital Visitor Logbook, handling how users enter the system, how their data is stored, and how they are authenticated for further actions within the approval workflow.

---

## 🚀 Tech Stack

| Component      | Technology              |
| :------------- | :---------------------- |
| **Backend** | Java 17+ (Spring Boot)  |
| **Database** | MySQL                   |
| **Frontend** | Thymeleaf (HTML/CSS)    |
| **Build Tool** | Maven                   |

---

## 📁 Project Structure

The project follows a standard Spring Boot layered architecture:

```text
backend/
 ├── src/main/java/com/visitor/
 │    ├── controller/   → Route handlers (Login, Register)
 │    ├── service/      → Business logic layer
 │    ├── dao/          → Repository / Database operations
 │    ├── model/        → Entity classes (User, Visitor)
 │    └── config/       → Security & App configuration
 └── src/main/resources/
      ├── templates/    → HTML View files
      ├── static/css    → Styling & Assets
      └── application.properties → DB & Port config
````

-----

## ⚙️ Setup Instructions

### 🔹 1. Install Requirements

Ensure your development environment has:

  * **JDK 17** or higher
  * **Maven** (latest version)
  * **MySQL Server**

### 🔹 2. Setup Database

Open your MySQL terminal or Workbench and execute the following script:

```sql
CREATE DATABASE visitor_logbook;
USE visitor_logbook;

-- User Table (for Authentication)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'VISITOR'
);

-- Visitor Table (for Details)
CREATE TABLE visitors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    phone VARCHAR(15),
    organization VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### 🔹 3. Configure Application

Open `src/main/resources/application.properties` and update the credentials to match your local MySQL setup:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/visitor_logbook
spring.datasource.username=root
spring.datasource.password=your_secure_password
spring.jpa.hibernate.ddl-auto=update
```

### 🔹 4. Run the Project

Open your terminal inside the `backend` folder and run:

```bash
mvn clean install
mvn spring-boot:run
```

### 🔹 5. Open in Browser

Once the console shows that the application has started, navigate to:

> **[http://localhost:8080](https://www.google.com/search?q=http://localhost:8080)**

-----

## 🛠 Features Included

  * **Visitor Registration:** Form to capture name, email, phone, and organization.
  * **Secure Authentication:** Login system to verify credentials.
  * **Role Management:** Basic logic to differentiate between Visitors and Admins.
  * **Dashboard Access:** Redirection to a landing page upon successful entry.

-----

> **Note:** Ensure that the MySQL service is running before starting the Spring Boot application to avoid connection timeout errors.

```

Would you like me to help you write the **Java Entity classes** or the **Login Controller** to match this database schema?
```