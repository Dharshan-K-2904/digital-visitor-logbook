# Digital Visitor Logbook with Approval Flow

## 👨‍💻 My Contribution (Chethan MS)

### 🎯 Role
**Visitor Registration & Profile Management**  
**Login and Authentication**

---

## ✅ Work Completed

### 🔹 1. Visitor Registration
- Implemented backend API to register new visitors
- Connected frontend form with backend using JavaScript (fetch API)
- Stored visitor data in database

### 🔹 2. Visitor Login (Authentication)
- Developed login functionality using email and password
- Validated user credentials from database
- Displayed login success/failure messages

### 🔹 3. Backend Development (Spring Boot)
- Created:
  - `VisitorController`
  - `VisitorService`
  - `VisitorRepository`
  - `Visitor` model
- Implemented REST APIs:
  - `POST /visitor/register`
  - `POST /visitor/login`

### 🔹 4. Database Integration
- Initially used **H2 Database**
- Migrated to **MySQL**
- Configured database using `application.properties`

### 🔹 5. Frontend Development
- Designed pages:
  - `register.html`
  - `login.html`
  - `profile.html`
- Implemented:
  - Form handling using JavaScript
  - API integration using fetch()

### 🔹 6. Frontend + Backend Integration
- Connected UI with backend APIs
- Fixed CORS issues
- Ensured smooth data flow between frontend and backend

### 🔹 7. Testing
- Tested APIs using browser and Thunder Client
- Verified:
  - Registration success
  - Login functionality

---

## 🛠️ Technologies Used

- Java (Spring Boot)
- Spring Data JPA
- MySQL
- HTML, CSS, JavaScript
- Maven
- Git & GitHub

---

## 🚀 How to Run

### Backend
```bash
mvn spring-boot:run