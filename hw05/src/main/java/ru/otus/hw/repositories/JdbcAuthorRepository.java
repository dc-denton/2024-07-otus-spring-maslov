package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.repositories.mappers.AuthorRowMapper;
import ru.otus.hw.models.Author;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbc;

    private final AuthorRowMapper authorRowMapper;

    @Override
    public List<Author> findAll() {
        return jdbc.query("select id, full_name from authors", authorRowMapper);
    }

    @Override
    public Optional<Author> findById(long id) {
       return Optional.ofNullable(jdbc.queryForObject(
                "select id, full_name from authors where id = :id", Map.of("id", id), authorRowMapper
        ));
    }
}
