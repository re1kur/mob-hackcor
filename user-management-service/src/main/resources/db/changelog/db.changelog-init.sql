--liquibase formatted sql

--changeset re1kur:1
INSERT INTO roles(name)
VALUES ('MODERATOR');

--changeset re1kur:2
INSERT INTO users(email, password, enabled)
VALUES ('moderator@mail.com', '$2a$12$0Jb52LcCOF2Wxo1Wb86Wb.Bh5pMbt/pxQPtrKAMULwR66rmFVpacq', true);

--changeset re1kur:3
INSERT INTO users_roles(user_id, role_id)
VALUES ((select id from users where email = 'moderator@mail.com'), (select id from roles where name = 'MODERATOR'));