CREATE DATABASE visitor_logbook;

USE visitor_logbook;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(20)
);

CREATE TABLE visit_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    visitor_id INT,
    host_email VARCHAR(100),
    purpose VARCHAR(255),
    status VARCHAR(20),
    request_time TIMESTAMP
);