package ru.otus.hw.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockBean
    private TestFileNameProvider appProperties;

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    @DisplayName("Questions file is incorrect: Test passed")
    public void exceptionThrowFindAll() {
        Mockito.when(appProperties.getTestFileName()).thenReturn("cuestions.csv");

        assertThrows(QuestionReadException.class,
                () -> csvQuestionDao.findAll());
    }

    @Test
    @DisplayName("Questions file is correct: Test passed")
    public void successFindAll() {
        List<Question> expectedQuestions = List.of(
                new Question("How does Jim spell his first name?",
                        List.of(new Answer("JMI", false),
                                new Answer("MIJ", true),
                                new Answer("IJM", false))),
                new Question("When is Jim's birthday?",
                        List.of(new Answer("The day he was born", false),
                                new Answer("June 9, 1994", true),
                                new Answer("Today if you have a present", false))));


        Mockito.when(appProperties.getTestFileName()).thenReturn("questions.csv");

        List<Question> actualQuestions = csvQuestionDao.findAll();

        assertEquals(expectedQuestions, actualQuestions);
    }
}