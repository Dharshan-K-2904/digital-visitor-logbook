USE visitor_logbook;

INSERT INTO users (name,email,password,role,active)
SELECT 'Admin User','admin@test.com','admin123','ADMIN',1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='admin@test.com');

INSERT INTO users (name,email,password,role,active)
SELECT 'Host Employee','host@test.com','host123','HOST',1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='host@test.com');

INSERT INTO users (name,email,password,role,active)
SELECT 'Security Guard','security@test.com','security123','SECURITY',1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='security@test.com');

INSERT INTO users (name,email,password,role,active)
SELECT 'Bob Visitor','visitor@test.com','visitor123','VISITOR',1 FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email='visitor@test.com');
