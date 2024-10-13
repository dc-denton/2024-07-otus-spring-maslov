insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into comments(text, book_id)
values ('text_1', 1), ('text_2', 1), ('text_3', 2) ,('text_4', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 2),   (2, 3),
       (3, 1),   (3, 3);
