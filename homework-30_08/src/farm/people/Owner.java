package farm.people;

import farm.Farm;
import farm.exceptions.RepeatedInstanceException;

import java.util.*;

public class Owner extends Person {
    public Farm farm;
    private double netWorth;
    private Map<String, Employee> employees;

    public double getNetWorth() {
        return netWorth;
    }
    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public Map<String, Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(Map<String, Employee> employees) {
        this.employees = employees;
    }
    public void setEmployees(Collection<Employee> employees) {
        this.employees.clear();
        for (Employee em : employees) {
            this.employees.put(em.getSsn(), em);
        }
    }

    public Owner(String fullName, String ssn, int age, Farm farm, double netWorth, Map<String, Employee> employees) {
        super(fullName, ssn, age);
        this.farm = farm;
        setNetWorth(netWorth);
        setEmployees(employees);
    }

    public Owner(String fullName, String ssn, int age, Farm farm, double netWorth) {
        this(fullName, ssn, age, farm, netWorth, new HashMap<>());
    }

    public String employeesToString() {
        StringBuilder crCultSb = new StringBuilder();
        StringBuilder anCareSb = new StringBuilder();
        employees.forEach((key, value) -> {
            if (value.type == Employee.EmployeeType.CULTIVATOR) {
                crCultSb.append(value);
            } else {
                anCareSb.append(value);
            }
        });

        return super.toString() +
                String.format("| %-16s | %-15s | %-11s | %-10s |\n", "Type", "Full name", "SSN", "Salary") +
                "+------------------+-----------------+-------------+------------+\n" +
                crCultSb +
                "+------------------+-----------------+-------------+------------+ Vet +\n" +
                anCareSb +
                "+------------------+-----------------+-------------+------------------+\n";
    }


    public void addEmployee(Employee employee) throws RepeatedInstanceException {
        if (employees.containsKey(employee.getSsn()))
            throw new RepeatedInstanceException("Employee already exists in list.");

        this.employees.put(employee.getSsn(), employee);
    }
}
