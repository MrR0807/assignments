CREATE TABLE bank_user
(
    id       BIGINT auto_increment NOT NULL,
    email    VARCHAR(255)          NOT NULL,
    password VARCHAR(255)          NOT NULL,

    CONSTRAINT PK__bank_user__id PRIMARY KEY (id),
    CONSTRAINT UQ__bank_user__email UNIQUE (email)
);

CREATE TABLE account
(
    email   VARCHAR(255)   NOT NULL,
    balance DECIMAL(19, 4) NOT NULL default 0,

    CONSTRAINT PK__account__email PRIMARY KEY (email)
);

CREATE TABLE account_record
(
    id      BIGINT auto_increment NOT NULL,
    created TIMESTAMP             NOT NULL,
    action  VARCHAR(50)           NOT NULL,
    amount  DECIMAL(19, 4)        NOT NULL,
    email   VARCHAR(255)          NOT NULL,

    CONSTRAINT PK__record__id PRIMARY KEY (id),
    CONSTRAINT FK__record__account__email FOREIGN KEY (email) REFERENCES account (email)
);