-- liquibase formatted sql

-- changeset re1kur:1
CREATE TABLE IF NOT EXISTS tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(64) NOT NULL UNIQUE,
    description TEXT        NOT NULL,
    reward      INT         NOT NULL DEFAULT 5 CHECK ( reward > 0 )
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS task_attempts
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         UUID   NOT NULL,
    task_id   BIGINT NOT NULL,
    attempt_time    TIMESTAMP NOT NULL DEFAULT now(),
    text_content    TEXT,
    file_content_id VARCHAR(256),
    confirmed       BOOLEAN,
    moderator_id    UUID,
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS users_tasks
(
    user_id         UUID,
    task_id   BIGINT,
    last_attempt_id BIGINT,
    status          TEXT NOT NULL CHECK (status IN ('pending', 'confirmed', 'rejected')) DEFAULT 'pending',
    PRIMARY KEY (user_id, task_id),
    FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    FOREIGN KEY (last_attempt_id) REFERENCES task_attempts (id) ON DELETE SET NULL
);
