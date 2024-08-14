package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        showQuiz(questions);
    }

    private void showQuiz(List<Question> questions) {
        for (Question question : questions) {
            showQuestion(question);
            showAnswers(question.answers());
            ioService.printLine("");
        }
    }

    private void showQuestion(Question question) {
        ioService.printLine(question.text());
    }

    private void showAnswers(List<Answer> answers) {
        answers.forEach((s) -> ioService.printFormattedLine("\t- %s", s.text()));
    }
}
