DELETE FROM users_roles;
DELETE FROM users;
INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'example@gmail.com', 'password', 'Bob', 'Johnson', 'London', FALSE),
       (2, 'alice@gmail.com', 'password123', 'Alice', 'Alison', 'New York', FALSE),
       (3, 'john@gmail.com', 'password1234', 'John', 'Alison', 'New York', FALSE);
