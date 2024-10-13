package ru.otus.hw.commands;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.sql.SQLException;

@ShellComponent
public class H2Commands {

    @ShellMethod(value = "Show H2 console", key = {"cc", "h2"})
    void showConsole(@ShellOption(defaultValue = "")String... args) throws SQLException {
        Console.main(args);
    }
}
