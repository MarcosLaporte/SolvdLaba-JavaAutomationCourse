package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.IActionManager;
import labaFarm.farm.animals.Animal;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.people.employees.AnimalCaretaker;
import labaFarm.farm.people.employees.CropsCultivator;
import labaFarm.farm.people.employees.Employee;
import labaFarm.farm.people.Owner;

import java.util.Map;
import java.util.function.Supplier;

public class ActionsService {

    private enum Entity {
        OWNER,
        ANIMAL_CARETAKER,
        CROPS_CULTIVATORS,
        ANIMALS,
        CROPS;

        private final int value;
        Entity() {
            this.value = this.ordinal()+1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Entity option : Entity.values()) {
                sb.append(String.format("%d. %s\n", option.value, option));
            }

            return sb.toString();
        }
    }

    public static void recordAction(Farm farm, Owner owner) {

        int chosenEnt = InputService.readInt(
                Entity.getAll() + "Select an entity to perform said action: ",
                "This option does not exist. Try again: ",
                1, Entity.values().length
        );

        String log = switch (Entity.values()[chosenEnt-1]) {
            case OWNER -> {
                IActionManager<Owner, String> actionManager = (e, a) -> String.format("Owner %s performed: %s", e.fullName, a);
                yield actionManager.performAction(owner, getAction.get());
            }
            case ANIMAL_CARETAKER -> {
                Map<String, Employee> map = FilterService.filterMapByValue(owner.getEmployees(), Employee.caretakerPredicate.and(Employee.idlePredicate));
                AnimalCaretaker animalCaretaker = (AnimalCaretaker) MenuService.chooseEmployee(map);

                assert animalCaretaker != null : "Animal caretaker is null";
                animalCaretaker.assignTask(getAction.get(), getDuration.get()*1000);
                yield "Animal caretaker " + animalCaretaker.fullName + " is performing: " + animalCaretaker.getCurrentTask();
            }
            case CROPS_CULTIVATORS -> {
                Map<String, Employee> map = FilterService.filterMapByValue(owner.getEmployees(), Employee.cultivatorPredicate.and(Employee.idlePredicate));
                CropsCultivator cropsCultivator = (CropsCultivator) MenuService.chooseEmployee(map);

                assert cropsCultivator != null : "Cultivator is null";
                cropsCultivator.assignTask(getAction.get(), getDuration.get()*1000);
                yield "Cultivator " + cropsCultivator.fullName + " is performing: " + cropsCultivator.getCurrentTask();
            }
            case ANIMALS -> {
                Animal animal = MenuService.chooseAnimal(farm.animals, false);
                IActionManager<Animal, String> actionManager = (e, a) -> String.format("%s performed: %s", e.species, a);
                yield actionManager.performAction(animal, getAction.get());
            }
            case CROPS -> {
                CropSector cropSector =  MenuService.chooseCrop(farm.cropSectors, false);
                IActionManager<CropSector, String> actionManager = (e, a) -> String.format("%s performed: %s", e.type, a);
                yield actionManager.performAction(cropSector, getAction.get());
            }
        };

        FileService.writeFile("actionsLog.txt", log + "\n");
    }

    private static final Supplier<String> getAction = () -> InputService.readString("Enter action or task to perform: ", 1, 255);
    private static final Supplier<Integer> getDuration = () -> InputService.readInt("Enter duration of task (sec): ", "Invalid value. Try again (1-60): " , 1, 60);
}
