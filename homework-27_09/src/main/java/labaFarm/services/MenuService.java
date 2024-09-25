package labaFarm.services;

import labaFarm.farm.Good;
import labaFarm.farm.animals.*;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.people.employees.Employee;

import java.util.List;
import java.util.Map;

public class MenuService {
    public static Employee.WorkShift chooseWorkShift() {
        int shiftVal = InputService.readInt(
                Employee.WorkShift.getAll() + "Select work shift: ",
                "This option does not exist. Try again: ",
                1, Employee.WorkShift.values().length
        );

        return Employee.WorkShift.getWorkShift(shiftVal);
    }

    public static Animal chooseAnimal(List<Animal> list, boolean goBackOption) {
        if (list.isEmpty()) {
            System.out.println("No animals in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (goBackOption) sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Animal an : list) {
            sb.append(String.format("%d. %s (%s) - ID %d\n", index, an.species, an.sex, an.getId()));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "Choose an animal: ",
                "This option does not exist. Try again: ",
                goBackOption ? 0 : 1, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }
    public static Animal chooseAnimal(List<Animal> list) {
        return chooseAnimal(list, true);
    }

    public static CropSector chooseCrop(List<CropSector> list, boolean goBackOption) {
        if (list.isEmpty()) {
            System.out.println("No crops in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (goBackOption) sb.append("\n0. GO BACK\n");
        int index = 1;

        for (CropSector cr : list) {
            sb.append(String.format("%d. %s - %.2f acres\n", index, cr.type, cr.acres));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "\nChoose a crop: ",
                "This option does not exist. Try again: ",
                goBackOption ? 0 : 1, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }
    public static CropSector chooseCrop(List<CropSector> list) {
        return chooseCrop(list, true);
    }

    public static Good chooseGood(List<Good> list, boolean goBackOption) {
        if (list.isEmpty()) {
            System.out.println("No goods in the list.");
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (goBackOption) sb.append("\n0. GO BACK\n");
        int index = 1;

        for (Good good : list) {
            sb.append(String.format("%d. %s - $%.2f\n", index, good.type, good.getTotalValue()));
            index++;
        }

        int chosenOpt = InputService.readInt(
                sb + "\nChoose one of the goods from the list: ",
                "This option does not exist. Try again: ",
                goBackOption ? 0 : 1, index
        );

        if (chosenOpt == 0) return null;
        return list.get(chosenOpt - 1);
    }
    public static Good chooseGood(List<Good> list) {
        return chooseGood(list, true);
    }

    public static Employee chooseEmployee(Map<String, Employee> employeeMap) {
        if (employeeMap.isEmpty()) {
            System.out.println("No employees to show.");
            return null;
        }
        String[] employeesSSN = employeeMap.keySet().toArray(new String[0]);
        System.out.println(Employee.toTable(employeeMap.values().stream().toList()));

        String chosenSSN = InputService.readStringInValues(
                "Enter selected employee's SSN: ",
                "That value does not exist. Try again: ",
                employeesSSN
        );

        return employeeMap.get(chosenSSN);
    }
}
