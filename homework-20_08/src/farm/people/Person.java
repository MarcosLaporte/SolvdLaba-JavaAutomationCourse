package farm.people;

import java.util.Objects;

public abstract class Person {
    public String fullName;
    private String ssn;
    public int age;

    public String getSsn() {
        return ssn;
    }
    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Person(String fullName, String ssn, int age) {
        this.fullName = fullName;
        setSsn(ssn);
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("| %15s | %11s | $%3d |\n",
                this.fullName, this.ssn, this.age);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Employee emp = (Employee) obj;
        return this.ssn.equals(emp.getSsn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.ssn);
    }
}