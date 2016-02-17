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
