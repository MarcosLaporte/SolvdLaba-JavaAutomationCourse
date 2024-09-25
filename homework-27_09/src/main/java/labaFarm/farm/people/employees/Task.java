package labaFarm.farm.people.employees;

import labaFarm.services.FileService;
import labaFarm.services.LoggerService;
import org.apache.logging.log4j.Level;

import java.time.LocalDateTime;

public class Task implements Runnable {
    private final Employee employee;
    private final String taskName;
    private final int duration;

    public String getTaskName() {
        return taskName;
    }

    public Task(Employee employee, String taskName, int duration) {
        this.employee = employee;
        this.taskName = taskName;
        this.duration = duration;
    }

    @Override
    public void run() {
        LoggerService.log(Level.INFO, this.employee.fullName + " is starting task: " + taskName);

        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            LoggerService.log(Level.INFO, this.employee.fullName + " was interrupted during the task.");
            return;
        }

        LoggerService.log(Level.INFO, this.employee.fullName + " has completed the task: " + taskName);
        FileService.writeFile("tasks.txt", String.format("%s - %s: %s", LocalDateTime.now(), this.employee.fullName, this.taskName));
        this.employee.clearCurrentTask();
    }
}
