package ru.otus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.exception.DataLoadingException;
import ru.otus.model.Question;

import java.util.List;

import static java.lang.System.exit;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    private final IOService ioService;

    @Autowired
    public QuestionServiceImpl(QuestionDao questionDao, IOService ioService) {
        this.questionDao = questionDao;
        this.ioService = ioService;
    }

    @Override
    public List<Question> getQuestions() {
        List<Question> questions = null;

        try {
            questions = questionDao.getAllQuestions();
        } catch (DataLoadingException e) {
            ioService.println("Data loading error: " + e.getMessage());
            if (e.getCause() != null) {
                ioService.println(e.getCause().getMessage());
            }
            ioService.println("Test stopped");
            exit(1);
        }
        return questions;
    }
}

