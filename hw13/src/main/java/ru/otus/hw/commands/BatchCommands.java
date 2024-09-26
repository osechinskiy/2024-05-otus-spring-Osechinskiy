package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.h2.tools.Console;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@RequiredArgsConstructor
@ShellComponent
public class BatchCommands {

    private final Job importFromDatabaseJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "startMigrationJob", key = "smj")
    public void startMigrationJobWithJobLauncher() {
        try {
             jobLauncher.run(importFromDatabaseJob, new JobParameters());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @ShellMethod(value = "H2 console", key = "h2c")
    public void openConsole() {
        try {
            Console.main();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
