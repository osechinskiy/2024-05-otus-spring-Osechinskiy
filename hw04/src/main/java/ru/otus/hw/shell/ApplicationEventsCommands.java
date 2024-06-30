package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.security.LoginContext;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.StudentService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Application Testing Student Command")
@RequiredArgsConstructor
public class ApplicationEventsCommands {
    private final LoginContext loginContext;

    private final StudentService studentService;

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService ioService;

    @ShellMethod(value = "Login Command", key = {"l", "L", "Login", "login"})
    public String login() {
        var student = studentService.determineCurrentStudent();
        loginContext.login(student);
        return ioService.getMessage("LoginContext.login.passed", student.getFullName());
    }

    @ShellMethod(value = "Start testing", key = {"s", "S", "Start", "start", "t", "T", "Test", "test"})
    @ShellMethodAvailability(value = "isStatTestingCommandAvailability")
    public void isStatTesting() {
        var student = loginContext.getStudent();
        testRunnerService.run(student);
    }

    private Availability isStatTestingCommandAvailability() {
        return loginContext.isStudentLoggedIn()
                ? Availability.available()
                : Availability.unavailable(ioService.getMessage("LoginContext.login.fail"));
    }
}
