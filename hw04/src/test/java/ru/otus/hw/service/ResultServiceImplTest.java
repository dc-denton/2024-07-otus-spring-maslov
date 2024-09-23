package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;


@SpringBootTest(classes = ResultServiceImpl.class)
class ResultServiceImplTest {
    @MockBean
    private TestConfig testConfig;

    @MockBean
    private LocalizedIOService ioService;

    @Autowired
    private ResultServiceImpl resultServiceImplTest;

    @Test
    @DisplayName("Show positive result: Test passed")
    public void showResultPass() {
        Student student = new Student("Name", "Surname");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(11);

        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(9);
        Mockito.doNothing().when(ioService).printLineLocalized(Mockito.anyString());

        resultServiceImplTest.showResult(testResult);

        Mockito.verify(ioService).printLineLocalized("ResultService.passed.test");
    }

    @Test
    @DisplayName("Show negative result: Test passed")
    public void showResultFailed() {
        Student student = new Student("Name", "Surname");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(7);

        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(9);
        Mockito.doNothing().when(ioService).printLineLocalized(Mockito.anyString());

        resultServiceImplTest.showResult(testResult);

        Mockito.verify(ioService).printLineLocalized("ResultService.fail.test");
    }
}