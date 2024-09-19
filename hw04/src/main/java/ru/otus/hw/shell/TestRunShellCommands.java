package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunShellCommands {
    private final TestRunnerService testRunnerService;

    @ShellMethod(key = {"run", "r", "See Jim Run, Run Jim Run"}, value = "Run Earthworm Jim 2 Quiz")
    public String runTest() {
        testRunnerService.run();
        return "\nПройти тест еще раз?";
    }
}
