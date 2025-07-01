-- liquibase formatted sql

-- changeset re1kur:1
CREATE TABLE IF NOT EXISTS tasks
(
    id                BIGSERIAL PRIMARY KEY,
    title             VARCHAR(64)              NOT NULL UNIQUE,
    short_description VARCHAR(512),
    description       TEXT                     NOT NULL,
    starts_at         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    ends_at           TIMESTAMP WITH TIME ZONE,
    reward            SMALLINT                 NOT NULL DEFAULT 5 CHECK ( reward > 0 )
);

-- changeset re1kur:2
CREATE TABLE IF NOT EXISTS task_image
(
    task_id    BIGINT                   NOT NULL,
    file_id    VARCHAR(256)             NOT NULL,
    file_url   VARCHAR(2048)            NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    PRIMARY KEY (task_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id)
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS tasks_files
(
    task_id    BIGINT                   NOT NULL,
    file_id    VARCHAR(256)             NOT NULL,
    file_url   VARCHAR(2048)            NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    PRIMARY KEY (task_id, file_id)
);

--changeset re1kur:4
CREATE TABLE IF NOT EXISTS task_attempts
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      UUID                     NOT NULL,
    task_id      BIGINT                   NOT NULL,
    attempted_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    text_content TEXT,
    moderator_id UUID,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS task_attempt_files
(
    task_attempt_id BIGINT                   NOT NULL,
    file_id         VARCHAR(256),
    file_url        VARCHAR(2048),
    expires_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (task_attempt_id) REFERENCES task_attempts (id) ON DELETE CASCADE
);

--changeset re1kur:6
CREATE TABLE IF NOT EXISTS users_tasks
(
    user_id         UUID   NOT NULL,
    task_id         BIGINT NOT NULL,
    last_attempt_id BIGINT NOT NULL,
    confirmed       BOOLEAN,
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (last_attempt_id) REFERENCES task_attempts (id) ON DELETE SET NULL
);