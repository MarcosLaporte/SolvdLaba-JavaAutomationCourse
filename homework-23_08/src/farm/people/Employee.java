package farm.people;

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
    public WorkShift shift;
    public EmployeeType type;

    public double getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(double annualSalary) {
        this.annualSalary = annualSalary;
    }

    public Employee(String fullName, String ssn, int age, double annualSalary, WorkShift shift, EmployeeType type) {
        super(fullName, ssn, age);
        setAnnualSalary(annualSalary);
        this.shift = shift;
        this.type = type;
    }

    public Employee(String fullName, String ssn, int age, WorkShift shift, EmployeeType type) {
        this(fullName, ssn, age, 65000, shift, type);
    }

    public Employee(String fullName, String ssn, int age, double annualSalary, EmployeeType type) {
        this(fullName, ssn, age, annualSalary, WorkShift.MORNING, type);
    }

    public Employee(String fullName, String ssn, int age, EmployeeType type) {
        this(fullName, ssn, age, 65000, WorkShift.MORNING, type);
    }

    @Override
    public abstract String toString();
}
