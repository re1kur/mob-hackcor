--liquibase formatted sql

--changeset re1kur:1
CREATE TABLE IF NOT EXISTS courses
(
    course_id   UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT now()
);

--changeset re1kur:2
CREATE TABLE IF NOT EXISTS lectures
(
    lecture_id  UUID PRIMARY KEY,
    course_id   UUID REFERENCES courses (course_id) ON DELETE CASCADE,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    order_index INTEGER
);

--changeset re1kur:3
CREATE TABLE IF NOT EXISTS quizzes
(
    quiz_id       UUID PRIMARY KEY,
    course_id     UUID REFERENCES courses (course_id) ON DELETE CASCADE,
    title         VARCHAR(255) NOT NULL,
    passing_score INTEGER      NOT NULL DEFAULT 60
);


--changeset re1kur:4
CREATE TABLE IF NOT EXISTS questions
(
    question_id          UUID PRIMARY KEY,
    quiz_id              UUID REFERENCES quizzes (quiz_id) ON DELETE CASCADE,
    text                 TEXT    NOT NULL,
    correct_answer_index INTEGER NOT NULL
);

--changeset re1kur:5
CREATE TABLE IF NOT EXISTS question_options
(
    option_id   UUID PRIMARY KEY,
    question_id UUID REFERENCES questions (question_id) ON DELETE CASCADE,
    option_text TEXT NOT NULL
);


--changeset re1kur:6
CREATE TABLE IF NOT EXISTS course_progress
(
    user_id            UUID NOT NULL,
    course_id          UUID NOT NULL,
    score              INTEGER,
    completed          BOOLEAN DEFAULT FALSE,
    completed_at       TIMESTAMP,
    earned_certificate BOOLEAN DEFAULT FALSE,
    earned_points      INTEGER DEFAULT 0,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (course_id) REFERENCES courses (course_id) ON DELETE CASCADE
);

--changeset re1kur:7
CREATE TABLE IF NOT EXISTS flashcards
(
    flashcard_id UUID PRIMARY KEY,
    course_id    UUID REFERENCES courses (course_id) ON DELETE CASCADE,
    question     TEXT NOT NULL,
    answer       TEXT NOT NULL
);