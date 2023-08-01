package ru.otus.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.domain.Author;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс AuthorDaoJdbc ")
@JdbcTest
@Import(AuthorDaoJdbc.class)
public class AuthorDaoJdbcTest {

    private final AuthorDao authorDao;

    @Autowired
    public AuthorDaoJdbcTest(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @DisplayName("возвращает ожидаемого автора по его id")
    @Test
    void shouldReturnExpectedAuthorById() {
        Author expectedAuthor = new Author(1,"Pushkin");
        Author actualAuthor = authorDao.getById(expectedAuthor.getId()).get();
        assertEquals(expectedAuthor, actualAuthor);
    }
    @DisplayName("возвращает ожидаемый список авторов")
    @Test
    void shouldReturnExpectedAuthorList() {
        List<Author> author = authorDao.getAll();
        assertEquals(4, author.size());
    }

    @DisplayName("добавляет автора в БД если такого автора ещё нет")
    @Test
    void shouldInsertAuthor() {
        int beforeSize =  authorDao.getAll().size();

        Author expectedAuthor = new Author("Test_author");
        Author author = authorDao.insert(expectedAuthor);
        Author actualAuthor = authorDao.getById(author.getId()).get();
        assertEquals(expectedAuthor, actualAuthor);

        int afterSize =  authorDao.getAll().size();
        assertEquals(beforeSize + 1, afterSize);

        author = authorDao.insert(expectedAuthor);
        actualAuthor = authorDao.getById(author.getId()).get();
        assertEquals(expectedAuthor, actualAuthor);

        int afterInsertDuplicateSize =  authorDao.getAll().size();
        assertEquals(afterSize, afterInsertDuplicateSize);
    }
}