package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var check = entityManager.find(Book.class, id);
        if (check != null) {
            entityManager.remove(check);
        }
    }

    @Override
    public Optional<Book> findById(long id) {
        return entityManager.createQuery("select b from Book b where b.id = :id", Book.class)
                .setParameter("id", id)
                .setHint("jakarta.persistence.fetchgraph"
                        , entityManager.getEntityGraph("book-author-genres-entity-graph"))
                .getResultList().stream().findAny();
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("select b from Book b", Book.class)
                .setHint("jakarta.persistence.fetchgraph", entityManager.getEntityGraph("book-entity-graph"))
                .getResultList();
    }
}
