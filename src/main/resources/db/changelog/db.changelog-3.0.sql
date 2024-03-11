--liquibase formatted sql

--changeset kshev:1
    ALTER TABLE users
    ADD COLUMN image VARCHAR(64)


--changeset kshev:2
    ALTER TABLE users_aud
    ADD COLUMN image VARCHAR(64)