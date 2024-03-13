--liquibase formatted sql

--changeset kshev:1
CREATE TABLE IF NOT EXISTS phone
(
    id BIGSERIAL PRIMARY KEY ,
    phone_number VARCHAR(64) NOT NULL ,
    owner_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE
    );