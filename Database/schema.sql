CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255),
    role VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS visit_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    visitor_id INT,
    visitor_name VARCHAR(100),
    host_email VARCHAR(100),
    purpose VARCHAR(255),
    status VARCHAR(20),
    request_time TIMESTAMP,
    check_in_time TIMESTAMP NULL,
    check_out_time TIMESTAMP NULL,
    visit_duration INT NULL
);