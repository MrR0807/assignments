CREATE TABLE bank_user
(
    id       BIGINT auto_increment NOT NULL,
    email    VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL,

    CONSTRAINT PK__user_id PRIMARY KEY (id),
    CONSTRAINT UQ__email UNIQUE (email)

);