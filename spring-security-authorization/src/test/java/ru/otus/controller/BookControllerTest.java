package ru.otus.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;
import ru.otus.domain.Genre;
import ru.otus.dto.BookDto;
import ru.otus.service.AuthorService;
import ru.otus.service.BookService;
import ru.otus.service.CommentService;
import ru.otus.service.GenreService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Класс BookControllerTest ")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    private final long bookId = 1;

    @DisplayName("возвращает все книги")
    @WithMockUser(username = "user")
    @Test
    void booksPageShouldReturnCorrectView() throws Exception {
        Author author = new Author(1,"Test_author");
        Genre genre = new Genre(1, "Test_genre");
        Book book1 = new Book(1,"Test book", author, genre);
        Book book2 = new Book(2,"Test book2", author, genre);
        List<Book> bookList = List.of(book1, book2);

        given(bookService.getAll()).willReturn(bookList);

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", bookList));
    }

    @DisplayName("возвращает книгу со всеми комментариями")
    @WithMockUser(username = "user")
    @Test
    void bookPageShouldReturnCorrectView() throws Exception {
        Author author = new Author(1,"Test_author");
        Genre genre = new Genre(1, "Test_genre");
        Book book = new Book(1,"Test book", author, genre);
        Comment comment1 = new Comment("Comment 1", book);
        Comment comment2 = new Comment("Comment 2", book);
        List<Comment> commentList = List.of(comment1, comment2);

        given(bookService.getById(book.getId())).willReturn(book);
        given(commentService.getAllCommentToBook(book.getId())).willReturn(commentList);

        mvc.perform(get("/book/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("comments", commentList));
    }

    @DisplayName("возвращает страницу для редактирования книги")
    @WithMockUser(username = "user")
    @Test
    void editPageShouldReturnCorrectView() throws Exception {
        Author author1 = new Author(1,"Test_author1");
        Author author2 = new Author(2,"Test_author2");
        Genre genre1 = new Genre(1, "Test_genre1");
        Genre genre2 = new Genre(2, "Test_genre2");
        List<Author> authorList = List.of(author1, author2);
        List<Genre> genreList = List.of(genre1, genre2);
        Book book = new Book(1,"Test book", author1, genre1);

        given(bookService.getById(book.getId())).willReturn(book);
        given(authorService.getAllAuthors()).willReturn(authorList);
        given(genreService.getAllGenre()).willReturn(genreList);

        mvc.perform(get("/edit?id=" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", new BookDto(book)))
                .andExpect(model().attribute("authors", authorList))
                .andExpect(model().attribute("genres", genreList));
    }

    @DisplayName("сохраняет книгу")
    @WithMockUser(username = "user")
    @Test
    void saveBookShouldReturnCorrectView() throws Exception {
        Author author = new Author(1,"Test_author");
        Genre genre = new Genre(1, "Test_genre");
        Book book = new Book(1,"Test book", author, genre);

        given(bookService.save(new BookDto(book))).willReturn(book);

        mvc.perform(put( "/edit").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }


    @DisplayName("возвращает страницу для создания новой книги")
    @WithMockUser(username = "user")
    @Test
    void createBookPageShouldReturnCorrectView() throws Exception {
        Author author1 = new Author(1,"Test_author1");
        Author author2 = new Author(2,"Test_author2");
        Genre genre1 = new Genre(1, "Test_genre1");
        Genre genre2 = new Genre(2, "Test_genre2");
        List<Author> authorList = List.of(author1, author2);
        List<Genre> genreList = List.of(genre1, genre2);

        given(authorService.getAllAuthors()).willReturn(authorList);
        given(genreService.getAllGenre()).willReturn(genreList);

        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("authors", authorList))
                .andExpect(model().attribute("genres", genreList));
    }

    @DisplayName("создаёт новую книгу")
    @WithMockUser(username = "user")
    @Test
    void createBookShouldReturnCorrectView() throws Exception {
        long bookId = 1;
        Author author = new Author(1,"Test_author");
        Genre genre = new Genre(1, "Test_genre");
        Book book = new Book(1,"Test book", author, genre);

        given(bookService.save(new BookDto(book))).willReturn(book);
        mvc.perform(post("/book").with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("удаляет книгу")
    @WithMockUser(username = "user")
    @Test
    void deleteBookShouldReturnCorrectView() throws Exception {
        long bookId = 1;

        ArgumentCaptor<Long> valueCapture = ArgumentCaptor.forClass(Long.class);
        doNothing().when(bookService).deleteById(valueCapture.capture());

        mvc.perform(delete("/book/" + bookId).with(csrf()))
                .andExpect(status().is3xxRedirection());
        assertEquals(bookId, valueCapture.getValue());
    }

    @DisplayName("неавторизированным пользователям разрешает доступ к странице логина")
    @Test
    void givenAccessToLoginPage() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @DisplayName("неавторизированным пользователям запрещает доступ к корневой странице")
    @Test
    void forbiddenBooksPage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("неавторизированным пользователям запрещает доступ к странице книги")
    @Test
    void forbiddenBookPage() throws Exception {
        mvc.perform(get("/book/" + bookId))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("неавторизированным пользователям запрещает доступ к странице для редактирования книги")
    @Test
    void forbiddenBookEditPage() throws Exception {
        mvc.perform(get("/edit?id=" + bookId))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("неавторизированным пользователям запрещает сохранять книгу")
    @Test
    void forbiddenToSaveBook() throws Exception {
        mvc.perform(put( "/edit").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("неавторизированным пользователям запрещает возвращать страницу для создания новой книги")
    @Test
    void forbiddenToReturnCreateBookPage() throws Exception {
        mvc.perform(get("/book"))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("неавторизированным пользователям запрещает создавать новую книгу")
    @Test
    void forbiddenToCreateBook() throws Exception {
        mvc.perform(post("/book").with(csrf()))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("неавторизированным пользователям запрещает удалять книгу")
    @Test
    void forbiddenToDeleteBook() throws Exception {
        mvc.perform(delete("/book/" + bookId).with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}

