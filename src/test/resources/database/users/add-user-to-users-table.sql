DELETE FROM users_roles;
DELETE FROM users;
INSERT INTO users (id, email, password, first_name, last_name, shipping_address, is_deleted)
VALUES (1, 'example@gmail.com', '$2a$10$fEsP3UeV0i9mNcbxpNdl5Ovj270RPcmMOR0SWj4M1Cq9uOfsQP8/C', 'Bob', 'Johnson', 'London', FALSE);
