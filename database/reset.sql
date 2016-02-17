DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

DROP DATABASE IF EXISTS System;
CREATE DATABASE System;

CREATE TABLE Account (
    account_name VARCHAR(40) PRIMARY KEY
);

CREATE TABLE Book (
    book_id VARCHAR(40) PRIMARY KEY,
    title VARCHAR (60) NOT NULL
);

CREATE TABLE Author (
    author_id VARCHAR(40) PRIMARY KEY,
    author_name VARCHAR(60)
);

CREATE TABLE Series (
    series_id VARCHAR(40) PRIMARY KEY,
    series_name VARCHAR(60)
);

CREATE TABLE Account_Book (
    book_id VARCHAR REFERENCES Book (book_id),
    account_name VARCHAR REFERENCES Account (account_name),
    CONSTRAINT account_book_pkey PRIMARY KEY (book_id, account_name)
);

CREATE TABLE Book_Version (
    book_id VARCHAR REFERENCES Book (book_id),
    account_name VARCHAR REFERENCES Account (account_name),
    format VARCHAR(10),
    location TEXT UNIQUE,
    CONSTRAINT book_version_pkey PRIMARY KEY (book_id, account_name, format)
);

CREATE TABLE Book_Review (
    book_id VARCHAR REFERENCES Book (book_id),
    review_id SERIAL PRIMARY KEY,
    rating INTEGER,
    review TEXT
);

CREATE TABLE Book_Author (
    book_id VARCHAR REFERENCES Book (book_id),
    author_id VARCHAR REFERENCES Author (author_id),
    CONSTRAINT book_author_pkey PRIMARY KEY (book_id, author_id)
);

CREATE TABLE Book_Series (
    book_id VARCHAR REFERENCES Book (book_id),
    series_id VARCHAR REFERENCES Series (series_id),
    index INTEGER,
    CONSTRAINT book_series_pkey PRIMARY KEY (book_id, series_id)
);

CREATE TABLE Book_Tag (
    account_name VARCHAR REFERENCES Account (account_name),
    book_id VARCHAR REFERENCES Book (book_id),
    tag VARCHAR(20),
    CONSTRAINT book_tag_pkey PRIMARY KEY (account_name, book_id, tag)
);

