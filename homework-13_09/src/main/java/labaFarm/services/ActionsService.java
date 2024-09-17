package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.IActionManager;
import labaFarm.farm.animals.Animal;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.people.AnimalCaretaker;
import labaFarm.farm.people.CropsCultivator;
import labaFarm.farm.people.Employee;
import labaFarm.farm.people.Owner;

import java.util.function.Function;
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
                AnimalCaretaker animalCaretaker = (AnimalCaretaker) MenuService.chooseEmployee(FilterService.filterMapByValue(owner.getEmployees(), Employee.caretakerPredicate));
                IActionManager<AnimalCaretaker, String> actionManager = (e, a) -> String.format("Animal caretaker %s performed: %s", e.fullName, a);
                yield actionManager.performAction(animalCaretaker, getAction.get());
            }
            case CROPS_CULTIVATORS -> {
                CropsCultivator cropsCultivator = (CropsCultivator) MenuService.chooseEmployee(FilterService.filterMapByValue(owner.getEmployees(), Employee.cultivatorPredicate));
                IActionManager<CropsCultivator, String> actionManager = (e, a) -> String.format("Cultivator %s performed: %s", e.fullName, a);
                yield actionManager.performAction(cropsCultivator, getAction.get());
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

        FileService.writeFile("actionsLog.txt", log);
    }

    private static final Supplier<String> getAction = () -> InputService.readString("Enter action to perform: ", 1, 255);

}
