package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.AuthorRepository;
import ru.otus.dao.BookRepository;
import ru.otus.dao.CommentRepository;
import ru.otus.dao.GenreRepository;
import ru.otus.domain.Author;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;
import ru.otus.domain.Genre;
import ru.otus.exception.AuthorNotFoundException;
import ru.otus.exception.GenreNotFoundExeption;
import ru.otus.exception.NoFoundBookException;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository,
                           AuthorRepository authorRepository,
                           GenreRepository genreRepository,
                           CommentRepository commentRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Book getById(long id) throws NoFoundBookException {
        return bookRepository.getById(id).orElseThrow(
                () -> new NoFoundBookException("The book with id " + id + " was not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAll() {
        return bookRepository.getAll();
    }

    @Override
    @Transactional
    public Book insert(String title, long authorId, long genreId)
            throws AuthorNotFoundException, GenreNotFoundExeption {
        Author author = authorRepository.getById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("This author does not exist"));
        Genre genre = genreRepository.getById(genreId)
                .orElseThrow(() -> new GenreNotFoundExeption("This genre does not exist"));
        Book book = new Book(title, author, genre);
        return bookRepository.insert(book);
    }

    @Override
    @Transactional
    public void update(long id, String title, long authorId, long genreId)
            throws AuthorNotFoundException, GenreNotFoundExeption {
        Author author = authorRepository.getById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("This author does not exist"));
        Genre genre = genreRepository.getById(genreId)
                .orElseThrow(() -> new GenreNotFoundExeption("This genre does not exist"));
        Book book = new Book(id, title, author, genre);
        bookRepository.update(book);
    }

    @Override
    @Transactional
    public void deleteById(long id) throws NoFoundBookException {
        bookRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentToBook(long id) {
        return commentRepository.getAllCommentToBook(id);
    }

    @Override
    @Transactional
    public Comment addComment(String text, long book_id) throws NoFoundBookException {
        Book book = getById(book_id);
        return commentRepository.insert(new Comment(text, book));
    }

    @Override
    @Transactional
    public void deleteCommentById(long id) throws NoFoundBookException {
        commentRepository.deleteById(id);
    }
}
