CREATE TABLE IF NOT EXISTS ACTIVITIES (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    name            VARCHAR(255),
    description     TEXT,
    start_date      VARCHAR(60),
    end_date        VARCHAR(60),
    place           VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS USERS (
    id              VARCHAR(60) PRIMARY KEY,
    email           VARCHAR(255) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    role            VARCHAR(60) NOT NULL
);

CREATE TABLE IF NOT EXISTS PARTICIPATIONS (
    id              VARCHAR(60) PRIMARY KEY,
    user_id         VARCHAR(60) REFERENCES USERS(id) ON DELETE CASCADE,
    activity_id     VARCHAR(60) REFERENCES ACTIVITIES(id) ON DELETE CASCADE
);