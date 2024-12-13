-- Добавление админов
INSERT INTO entity_schema.users (email, password, role)
VALUES ('admin1@example.ru', 'password', 'ADMIN');

INSERT INTO entity_schema.users (email, password, role)
VALUES ('admin2@example.ru', 'password', 'ADMIN');

-- Добавление пользователей
INSERT INTO entity_schema.users (email, password, role)
VALUES ('user1@example.ru', 'password', 'USER');

INSERT INTO entity_schema.users (email, password, role)
VALUES ('user2@example.ru', 'password', 'USER');

select * FROM entity_schema.users