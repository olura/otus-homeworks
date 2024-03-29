package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.dao.BookRepository;
import ru.otus.dao.CommentRepository;
import ru.otus.domain.Book;
import ru.otus.domain.Comment;
import ru.otus.exception.BookNotFoundException;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BookRepository bookRepository) {
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getAllCommentToBook(String id) {
        return commentRepository.findByBookId(id);
    }

    @Override
    @Transactional
    public Comment addComment(String text, String bookId) throws BookNotFoundException {
        Book book = bookRepository.findById(bookId).get();
        return commentRepository.save(new Comment(text, book));
    }

    @Override
    @Transactional
    public void deleteCommentById(String id) {
        commentRepository.deleteById(id);
    }
}
