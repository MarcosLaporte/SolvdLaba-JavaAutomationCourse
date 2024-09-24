package labaFarm.farm.people;

import labaFarm.farm.Farm;
import labaFarm.farm.exceptions.RepeatedInstanceException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Owner extends Person {
    public Farm farm;
//    public int farmId;
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
//    public Owner(String fullName, String ssn, int age, int farmId, double netWorth, Map<String, Employee> employees) {
        super(fullName, ssn, age);
        this.farm = farm;
//        this.farmId = farmId;
        setNetWorth(netWorth);
        setEmployees(employees);
    }

    public Owner(String fullName, String ssn, int age, Farm farm, double netWorth) {
//    public Owner(String fullName, String ssn, int age, int farmId, double netWorth) {
        this(fullName, ssn, age, farm, netWorth, new HashMap<>());
//        this(fullName, ssn, age, farmId, netWorth, new HashMap<>());
    }

    public void addEmployee(Employee employee) throws RepeatedInstanceException {
        if (employees.containsKey(employee.getSsn()))
            throw new RepeatedInstanceException("Employee already exists in list.");

        this.employees.put(employee.getSsn(), employee);
    }
}
