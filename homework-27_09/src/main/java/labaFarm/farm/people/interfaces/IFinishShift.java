package labaFarm.farm.people.interfaces;

import labaFarm.farm.people.employees.Employee;

@FunctionalInterface
public interface IFinishShift <T extends Employee> {
    void finishShift();
}
