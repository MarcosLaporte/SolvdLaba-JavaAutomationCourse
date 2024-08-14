package farm.people;

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

    public String toString() {
        return String.format("| %15s | %11s | $%3d |\n",
                this.fullName, this.ssn, this.age);
    }
}