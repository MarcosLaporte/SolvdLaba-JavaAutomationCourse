package farm.people;

import farm.Farm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Owner extends Person {
    public Farm farm;
    private double netWorth;
    private List<Employee> employees;

    public Owner(String fullName, String ssn, int age, Farm farm, double netWorth, List<Employee> employees) {
        super(fullName, ssn, age);
        this.farm = farm;
        setNetWorth(netWorth);
        setEmployees(employees);
    }

    public Owner(String fullName, String ssn, int age, Farm farm, double netWorth) {
        this(fullName, ssn, age, farm, netWorth, new ArrayList<>());
    }

    public double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String employeesToString() {
        Iterator<Employee> it = this.employees.iterator();

        StringBuilder crCultSb = new StringBuilder();
        StringBuilder anCareSb = new StringBuilder();
        while (it.hasNext()) {
            Employee empValue = it.next();
            if (empValue.type == Employee.EmployeeType.CULTIVATOR) {
                crCultSb.append(empValue);
            } else {
                anCareSb.append(empValue);
            }
        }

        StringBuilder mainSb = new StringBuilder();
        mainSb.append(super.toString());
        mainSb.append(String.format("| %-16s | %-15s | %-11s | %-10s |\n",
                "Type", "Full name", "SSN", "Salary"));
        mainSb.append("+------------------+-----------------+-------------+------------+ LHY +\n");
        mainSb.append(crCultSb);

        mainSb.append("+------------------+-----------------+-------------+------------+ Vet +\n");
        mainSb.append(anCareSb);

        mainSb.append("+------------------+-----------------+-------------+------------------+\n");

        return mainSb.toString();
    }
}
