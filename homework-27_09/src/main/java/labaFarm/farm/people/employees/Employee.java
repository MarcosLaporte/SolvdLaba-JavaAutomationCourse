package labaFarm.farm.people.employees;

import labaFarm.farm.people.Person;
import labaFarm.farm.people.interfaces.IFinishShift;
import labaFarm.services.LoggerService;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.function.Predicate;

public abstract class Employee extends Person implements IFinishShift<Employee> {
    public enum WorkShift {
        MORNING, EVENING, NIGHT;

        private final int value;

        WorkShift() {
            this.value = ordinal() + 1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (WorkShift an : WorkShift.values()) {
                sb.append(String.format("%d. %s\n", an.value, an));
            }

            return sb.toString();
        }

        public static WorkShift getWorkShift(int value) {
            for (WorkShift ws : WorkShift.values()) {
                if (ws.value == value)
                    return ws;
            }

            return null;
        }
    }

    public enum EmployeeType {
        CULTIVATOR, ANIMAL_CARETAKER;

        private final int value;

        EmployeeType() {
            this.value = ordinal() + 1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (EmployeeType an : EmployeeType.values()) {
                sb.append(String.format("%d. %s\n", an.value, an));
            }

            return sb.toString();
        }

        public static EmployeeType getEmployeeType(int value) {
            for (EmployeeType et : EmployeeType.values()) {
                if (et.value == value)
                    return et;
            }

            return null;
        }
    }

    private double annualSalary;
    public Queue<WorkShift> nextShifts;
    public EmployeeType type;

    private Task currentTask;

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public String getCurrentTask() {
        return this.currentTask != null ? currentTask.getTaskName() : "Idle";
    }

    public Employee(String fullName, String ssn, int age, double annualSalary, Queue<WorkShift> nextShifts, EmployeeType type) {
        super(fullName, ssn, age);
        setAnnualSalary(annualSalary);
        this.nextShifts = nextShifts;
        this.type = type;
    }

    public Employee(String fullName, String ssn, int age, Queue<WorkShift> nextShifts, EmployeeType type) {
        this(fullName, ssn, age, 65000, nextShifts, type);
    }

    public Employee(String fullName, String ssn, int age, double annualSalary, EmployeeType type) {
        this(fullName, ssn, age, annualSalary, new ArrayDeque<>(), type);
        this.setRandomWorkShift(10);
    }

    public Employee(String fullName, String ssn, int age, EmployeeType type) {
        this(fullName, ssn, age, 65000, new ArrayDeque<>(), type);
        this.setRandomWorkShift(10);
    }

    protected void setRandomWorkShift(int amount) {
        Random random = new Random();
        for (int i = 0; i < amount; i++) {
            int index = random.nextInt(1, WorkShift.values().length) - 1;
            this.nextShifts.add(WorkShift.values()[index]);
        }
    }

    @Override
    public abstract String toString();

    public static String toTable(Collection<Employee> employees) {
        if (employees.isEmpty()) return "No employees in the list.\n";

        StringBuilder sb = new StringBuilder();
        for (Employee emp : employees) {
            sb.append(emp);
        }

        return String.format("| %-16s | %-15s | %-11s | %-10s | Vet |\n", "Type", "Full name", "SSN", "Salary") +
                "+------------------+-----------------+-------------+------------+-----+\n" +
                sb +
                "+------------------+-----------------+-------------+------------+-----+\n";
    }

    private transient final IFinishShift<CropsCultivator> iFinishShift = () -> this.nextShifts.remove();

    @Override
    public void finishShift() {
        iFinishShift.finishShift();
    }

    public static Predicate<Employee> cultivatorPredicate = employee -> employee.type == EmployeeType.CULTIVATOR;
    public static Predicate<Employee> caretakerPredicate = employee -> employee.type == EmployeeType.ANIMAL_CARETAKER;
    public static Predicate<Employee> idlePredicate = employee -> employee.currentTask == null;

    public synchronized void assignTask(String taskName, int durationMs) {
        if (this.currentTask != null) {
            LoggerService.log(Level.INFO, this.fullName + " is already working on a task.");
        } else {
            this.currentTask = new Task(this, taskName, durationMs);
            Thread taskThread = new Thread(this.currentTask, this.getSsn());
            taskThread.start();
        }
    }

    public synchronized void clearCurrentTask() {
        this.currentTask = null;
    }

}
