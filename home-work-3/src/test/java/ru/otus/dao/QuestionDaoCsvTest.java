package ru.otus.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.configuration.FileSourceProvider;
import ru.otus.exception.DataLoadingException;
import ru.otus.model.Question;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@DisplayName("Класс QuestionDaoCsv ")
@ExtendWith(MockitoExtension.class)
public class QuestionDaoCsvTest {
    @InjectMocks
    private QuestionDaoCsv questionDao;
    @Mock
    FileSourceProvider fileSourceProvider;

    @BeforeEach
    void loadProperties() throws IOException {
        Properties prop = new Properties();
        prop.load(getClass().getClassLoader().getResourceAsStream("test.properties"));
        String file = prop.getProperty("fileName");
        given(fileSourceProvider.getFileName()).willReturn(file);
    }

    @Test
    @DisplayName("загружает все вопросы имеющиеся в файле")
    void shouldLoadQuestions() throws DataLoadingException {
        List<Question> questions = questionDao.getAllQuestions();

        assertEquals(2, questions.size());
    }

    @Test
    @DisplayName("корректно загружает текст вопроса")
    void shouldLoadQuestionText() throws DataLoadingException {
        List<Question> questions = questionDao.getAllQuestions();

        assertThat(questions.toString()).contains("what is the name of the teacher");
        assertThat(questions.toString()).contains("what is the name of the subject");
    }

    @Test
    @DisplayName("загружает все ответы для каждого из вопросов")
    void shouldLoadAnswers() throws DataLoadingException {
        List<Question> questions = questionDao.getAllQuestions();
        for (Question question : questions) {
            assertEquals(5, question.answers().size());
        }
    }

    @Test
    @DisplayName("корректно загружает текст ответов")
    void shouldLoadAnswerText() throws DataLoadingException {
        List<Question> questions = questionDao.getAllQuestions();

        assertThat(questions.toString()).contains("Tom");
        assertThat(questions.toString()).contains("Bob");
        assertThat(questions.toString()).contains("Aleksandr");
        assertThat(questions.toString()).contains("Mike");
        assertThat(questions.toString()).contains("Ivan");
    }

    @Test
    @DisplayName("корректно загружает правильный ответ")
    void shouldLoadRightAnswer() throws DataLoadingException {
        List<Question> questions = questionDao.getAllQuestions();

        assertEquals(questions.get(0).getNumberRightAnswer(), 3);
        assertEquals(questions.get(1).getNumberRightAnswer(), 2);
    }
}
