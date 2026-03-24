CREATE DATABASE visitor_logbook;

USE visitor_logbook;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20)
);

CREATE TABLE visitors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    phone VARCHAR(15),
    organization VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(id)
);