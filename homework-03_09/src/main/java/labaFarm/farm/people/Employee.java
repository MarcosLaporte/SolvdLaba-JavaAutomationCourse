package labaFarm.farm.people;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public abstract class Employee extends Person {
    public enum WorkShift {
        MORNING, EVENING, NIGHT;

        private final int value;
        WorkShift() {
            this.value = ordinal()+1;
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
            this.value = ordinal()+1;
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


    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
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
            int index = random.nextInt(1, WorkShift.values().length)-1;
            this.nextShifts.add(WorkShift.values()[index]);
        }
    }

    @Override
    public abstract String toString();
}
