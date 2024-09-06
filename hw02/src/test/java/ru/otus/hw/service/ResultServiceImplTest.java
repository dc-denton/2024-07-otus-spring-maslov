package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.TestConfig;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;


@ExtendWith(MockitoExtension.class)
class ResultServiceImplTest {
    @Mock
    private TestConfig testConfig;

    @Mock
    private IOService ioService;

    @InjectMocks
    private ResultServiceImpl resultServiceImplTest;

    @Test
    public void showResultPass() {
        Student student = new Student("Name", "Surname");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(11);

        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(9);
        Mockito.doNothing().when(ioService).printLine(Mockito.anyString());

        resultServiceImplTest.showResult(testResult);

        Mockito.verify(ioService).printLine("Congratulations! You passed test!");
    }

    @Test
    public void showResultFailed() {
        Student student = new Student("Name", "Surname");
        TestResult testResult = new TestResult(student);
        testResult.setRightAnswersCount(7);

        Mockito.when(testConfig.getRightAnswersCountToPass()).thenReturn(9);
        Mockito.doNothing().when(ioService).printLine(Mockito.anyString());

        resultServiceImplTest.showResult(testResult);

        Mockito.verify(ioService).printLine("Sorry. You fail test.");
    }
}