package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestRunShellCommands {
    private final TestRunnerService testRunnerService;

    private final LocalizedIOService ioService;

    @ShellMethod(key = {"run", "r", "See Jim Run, Run Jim Run"}, value = "Run Earthworm Jim 2 Quiz")
    public void runTest() {
        testRunnerService.run();
        ioService.printLineLocalized("TestRunShellCommands.another.test");
    }
}
