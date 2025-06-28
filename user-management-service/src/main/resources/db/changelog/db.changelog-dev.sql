--liquibase formatted sql

--changeset re1kur:1
INSERT INTO users(id, email, password, enabled)
VALUES ('11111111-1111-1111-1111-111111111111', 'moderator@mail.com',
        '$2a$12$0Jb52LcCOF2Wxo1Wb86Wb.Bh5pMbt/pxQPtrKAMULwR66rmFVpacq', true);

--changeset re1kur:2
INSERT INTO users(id, email, password, enabled)
VALUES ('22222222-2222-2222-2222-222222222222', 'user@mail.com',
        '$2a$12$gZUhMeauaZQJOOrXnjN9ROyaEep7OZBy8Kcf7Jr5SDZKpmQ.de6vW', true);

--changeset re1kur:3
INSERT INTO users_roles(user_id, role_id)
VALUES ('11111111-1111-1111-1111-111111111111', (select id from roles where name = 'MODERATOR'));

--changeset re1kur:4
INSERT INTO users_roles(user_id, role_id)
VALUES ('22222222-2222-2222-2222-222222222222', (select id from roles where name = 'USER'));
