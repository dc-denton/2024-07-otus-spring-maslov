package ru.otus.hw.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider appProperties;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Test
    public void exceptionThrowFindAll() {
        Mockito.when(appProperties.getTestFileName()).thenReturn("cuestions.csv");

        assertThrows(QuestionReadException.class,
                () -> csvQuestionDao.findAll());
    }

    @Test
    public void successFindAll() {
        Answer answer1 = new Answer("JMI", false);
        Answer answer2 = new Answer("MIJ", true);
        Answer answer3 = new Answer("IJM", false);
        List<Answer> answers = new ArrayList<>();
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        Question question1 = new Question("How does Jim spell his first name?", answers);
        Answer answer4 = new Answer("The day he was born", false);
        Answer answer5 = new Answer("June 9, 1994", true);
        Answer answer6 = new Answer("Today if you have a present", false);
        List<Answer> answers2 = new ArrayList<>();
        answers2.add(answer4);
        answers2.add(answer5);
        answers2.add(answer6);
        Question question2 = new Question("When is Jim's birthday?", answers2);
        List<Question> expectedQuestions = new ArrayList<>();
        expectedQuestions.add(question1);
        expectedQuestions.add(question2);

        Mockito.when(appProperties.getTestFileName()).thenReturn("questions.csv");

        List<Question> actualQuestions = csvQuestionDao.findAll();

        assertEquals(expectedQuestions, actualQuestions);
    }
}