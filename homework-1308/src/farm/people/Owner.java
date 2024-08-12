package farm.people;

import farm.Farm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Owner extends Farmer {
    Farm farm;
    private double netWorth;
    private List<Farmer> employees;

    public Owner(String fullName, String ssn, float salary, Farm farm, double netWorth, List<Farmer> employees) {
        super(fullName, ssn, salary);
        this.type = FarmerType.OWNER;
        this.farm = farm;
        setNetWorth(netWorth);
        setEmployees(employees);
    }

    public Owner(String fullName, String ssn, float salary, Farm farm, double netWorth) {
        this(fullName, ssn, salary, farm, netWorth, new ArrayList<>());
    }

    public double getNetWorth() {
        return netWorth;
    }
    public void setNetWorth(double netWorth) {
        this.netWorth = netWorth;
    }

    public List<Farmer> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Farmer> employees) {
        this.employees = employees;
    }

}
