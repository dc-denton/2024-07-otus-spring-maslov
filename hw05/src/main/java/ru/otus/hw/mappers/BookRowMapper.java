package ru.otus.hw.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookRowMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");

        Author author = new Author(rs.getLong("author_id"),
                rs.getString("author_name"));
        Genre genre = new Genre(rs.getLong("genre_id"),
                rs.getString("genre_name"));

        return new Book(id, title, author, genre);
    }
}
