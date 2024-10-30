-- Active: 1726757589722@@127.0.0.1@3306@restfulapi
CREATE TABLE users (
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    token VARCHAR(100),
    token_expired_at BIGINT,
    PRIMARY KEY (username),
    UNIQUE(token)
)

CREATE TABLE contact (
    id VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),
    phone VARCHAR(100),
    email VARCHAR (100),
    PRIMARY KEY (id),
    FOREIGN KEY fk_users_contact (username) REFERENCES users (username)
)

CREATE TABLE addresses (
    id VARCHAR(100) NOT NULL,
    contact_id VARCHAR(100) NOT NULL,
    street VARCHAR(100),
    city VARCHAR(100),
    province VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(10),
    PRIMARY KEY (id),
    FOREIGN KEY fk_contact_addresses (contact_id) REFERENCES contact (id)
)