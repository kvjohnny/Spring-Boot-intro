DELETE FROM users_roles;
DELETE FROM shopping_carts;
DELETE FROM users;
DELETE FROM roles;
INSERT INTO roles (id, name)
VALUES (1, 'USER');
