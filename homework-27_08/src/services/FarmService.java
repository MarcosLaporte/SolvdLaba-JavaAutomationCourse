package services;

import farm.Farm;
import farm.exceptions.RepeatedInstanceException;
import farm.people.AnimalCaretaker;
import farm.people.CropsCultivator;
import farm.people.Employee;
import farm.people.Owner;

import java.util.ArrayList;


public class FarmService {
    public static Farm createFarm() {
        String auxName = InputService.readString("Enter name of the farm: ", 1, 25);
        String auxLocation = InputService.readString("Enter location of the farm: ", 1, 25);
        float auxSize = InputService.readFloat(
                "Enter size in acres: ",
                "Invalid value. Try again: ",
                0.5F, 99999
        );

        return new Farm(auxName, auxLocation, auxSize);
    }

    public static void createNewEmployee(Owner owner) {
        int innerMenuOption = InputService.readInt(
                "\nCreate new instance of what type of employee?\n0. GO BACK\n" + Employee.EmployeeType.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                0, Employee.EmployeeType.values().length
        );

        if (innerMenuOption == 0) {
            System.out.println("Back to main menu...");
            return;
        }

        Employee newEmployee = switch (Employee.EmployeeType.values()[innerMenuOption-1]) {
            case CULTIVATOR -> initCropsCultivator();
            case ANIMAL_CARETAKER -> initAnimalCare();
        };

        try {
            owner.addEmployee(newEmployee);
            System.out.println(newEmployee.type + " created successfully!");
        } catch (RepeatedInstanceException e) {
            LoggerService.warn(e.getMessage());
        }
    }

    private record PersonData(
            String fullName,
            String ssn,
            int age
    ) {
    }

    private static PersonData initPerson() {
        String auxName = InputService.readString("Enter full name: ", 1, 20);
        String auxSsn = InputService.readString("Enter SSN: ", 1, 20);
        int auxAge = InputService.readInt(
                "Enter age: ",
                "Invalid value. Try again: ",
                0, 125
        );

        return new PersonData(auxName, auxSsn, auxAge);
    }

    public static Owner initOwner(Farm farm) {
        System.out.println("Let's create an owner!");
        PersonData personData = initPerson();

        double auxNetWorth = InputService.readFloat(
                "Enter owner's net worth: ",
                "Invalid value. Try again: ",
                1, 99_999_999
        );

        return new Owner(personData.fullName, personData.ssn, personData.age, farm, auxNetWorth);
    }

    private record EmployeeData(
            double annualSalary,
            Employee.WorkShift shift,
            Employee.EmployeeType type
    ) {
    }

    private static EmployeeData initEmployee(Employee.EmployeeType empType) {
        double auxSalary = InputService.readFloat(
                "Enter annual salary: ",
                "Invalid value. Try again: ",
                1000, 800_000
        );

        int shiftVal = InputService.readInt(
                Employee.WorkShift.getAll() + "Select work shift: ",
                "This option does not exist. Try again: ",
                1, Employee.WorkShift.values().length
        );
        Employee.WorkShift auxWorkShift = Employee.WorkShift.getWorkShift(shiftVal);

        return new EmployeeData(auxSalary, auxWorkShift, empType);
    }

    public static AnimalCaretaker initAnimalCare() {
        System.out.println("Let's create an animal caretaker!");
        PersonData personData = initPerson();
        EmployeeData employeeData = initEmployee(Employee.EmployeeType.ANIMAL_CARETAKER);

        char charIsVet = InputService.readCharInValues(
                "Is vet certified? Y/N ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        );
        boolean auxIsVetCertified = charIsVet == 'Y';

        System.out.println("Animals in care list is empty, make sure to add them later on!");
        return new AnimalCaretaker(personData.fullName, personData.ssn, personData.age, employeeData.annualSalary, employeeData.shift, new ArrayList<>(), auxIsVetCertified);
    }

    public static CropsCultivator initCropsCultivator() {
        System.out.println("Let's create an crops cultivator!");
        PersonData personData = initPerson();
        EmployeeData employeeData = initEmployee(Employee.EmployeeType.CULTIVATOR);

        System.out.println("Crops in care list is empty, make sure to add them later on!");
        return new CropsCultivator(personData.fullName, personData.ssn, personData.age, employeeData.annualSalary, employeeData.shift, new ArrayList<>());
    }

}
