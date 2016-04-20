DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE Account (
    account_name VARCHAR(40) PRIMARY KEY
);

CREATE TABLE Book (
    book_id VARCHAR(40) PRIMARY KEY,
    title VARCHAR (60) NOT NULL,
    description TEXT
);

CREATE TABLE Author (
    author_id VARCHAR(40) PRIMARY KEY,
    author_name VARCHAR(60),
    description TEXT
);

CREATE TABLE Series (
    series_id VARCHAR(40) PRIMARY KEY,
    series_name VARCHAR(60)
);

CREATE TABLE Account_Book (
    account_name VARCHAR REFERENCES Account (account_name),
    book_id VARCHAR REFERENCES Book (book_id),
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

INSERT INTO Account Values
	('adam'),
	('nick'),
	('steve');

INSERT INTO Book VALUES
	('mobydick', 'Moby Dick'),
	('hp1', 'Harry Potter and The Sorcerer''s Stone'),
	('hp2', 'Harry Potter and the Chamber of Secrets'),
	('worm', 'Worm');

INSERT INTO Author VALUES
	('hm', 'Herman Melville'),
	('jkr', 'J.K. Rowling'),
	('wbow', 'Wildbow');

INSERT INTO Series VALUES
	('hp', 'Harry Potter');

INSERT INTO Account_Book (account_name, book_id) VALUES
	('adam', 'mobydick'),
	('adam', 'hp1'),
	('adam', 'hp2'),
	('adam', 'worm'),
	('nick', 'hp1'),
	('nick', 'hp2');

INSERT INTO Book_Review (book_id, review, rating) VALUES
	('mobydick', 'boring', 1),
	('mobydick', 'classic', 2),
	('worm', 'my favorite book', 5);

INSERT INTO Book_Tag VALUES
	('adam', 'worm', 'fantasy'),
	('adam', 'worm', 'superheroes'),
	('adam', 'hp1', 'fantasy'),
	('adam', 'hp2', 'fantasy');

INSERT INTO Book_Version (book_id, account_name, format, location) VALUES
	('hp2', 'nick', 'pdf', 'HarryPotter1.pdf'),
	('hp2', 'nick', 'tex', 'How_Converted_HP_TO_Latex.tex');
	
INSERT INTO book_author VALUES
	('hp2', 'jkr');
	
	
INSERT INTO book_series VALUES
	('hp1', 'hp', 1),
	('hp2', 'hp', 2);
