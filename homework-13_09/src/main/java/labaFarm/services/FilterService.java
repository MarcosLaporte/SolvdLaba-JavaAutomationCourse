package labaFarm.services;

import labaFarm.farm.Farm;
import labaFarm.farm.animals.Animal;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterService {

    private enum AnimalFilterField {
        SPECIES, DATE_OF_BIRTH, SEX, WEIGHT, HEIGHT;

        private final int value;
        AnimalFilterField() {
            this.value = this.ordinal()+1;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (AnimalFilterField option : AnimalFilterField.values()) {
                sb.append(String.format("%d. %s\n", option.value, option));
            }

            return sb.toString();
        }
    }

    public static void filterAnimals(Farm farm) {
        int fieldChosen = InputService.readInt(
                "\nSelect a field to filter animals by.\n" + AnimalFilterField.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                1, AnimalFilterField.values().length
        );

        Stream<Animal> animalStream = farm.animals.stream();
        List<Animal> finalList = switch (AnimalFilterField.values()[fieldChosen - 1]) {
            case SPECIES -> filterAnimalsBySpecies(animalStream);
            case DATE_OF_BIRTH -> filterAnimalsByDoB(animalStream);
            case SEX -> filterAnimalsBySex(animalStream);
            case WEIGHT -> filterAnimalsByWeight(animalStream);
            case HEIGHT -> filterAnimalsByHeight(animalStream);
        };

        System.out.println(Animal.toTable(finalList));
    }

    public static <K, V> Map<K, V> filterMapByValue(Map<K, V> map, Predicate<V> predicate) {
        Objects.requireNonNull(map);
        Objects.requireNonNull(predicate);

        return map.entrySet()
                .stream()
                .filter(item -> predicate.test(item.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static List<Animal> filterAnimalsBySpecies(Stream<Animal> animalStream) {
        int chosenSpecies = InputService.readInt(
                "\nSelect an species to filter.\n" + Animal.Species.getAll() + "Choose an option: ",
                "This option does not exist. Try again: ",
                1, Animal.Species.values().length
        );

        Animal.Species species = Animal.Species.values()[chosenSpecies - 1];
        return animalStream.filter(animal -> animal.species == species).toList();
    }

    private static List<Animal> filterAnimalsByDoB(Stream<Animal> animalStream) {
        char enterFromDate = InputService.readCharInValues(
                "Enter date to filter FROM (1) or skip (2)?",
                "Invalid option. Try again: ",
                new char[]{'1', '2'}
        );
        LocalDate dobFrom = enterFromDate == '1' ? InputService.readValidDate() : LocalDate.MIN;
        char enterToDate = InputService.readCharInValues(
                "Enter date to filter UP TO (1) or skip (2)?",
                "Invalid option. Try again: ",
                new char[]{'1', '2'}
        );
        LocalDate dobTo = enterToDate == '1' ? InputService.readValidDate() : LocalDate.MAX;

        return animalStream.filter(animal -> animal.getDateOfBirth().isAfter(dobFrom) && animal.getDateOfBirth().isBefore(dobTo)).toList();
    }

    private static List<Animal> filterAnimalsBySex(Stream<Animal> animalStream) {
        char sexChar = InputService.readCharInValues(
                "Enter animal sex to filter (F/M): ",
                "This option does not exist. Try again: ",
                new char[]{'F', 'M'}
        );

        Animal.AnimalSex animalSex = Animal.AnimalSex.valueOf(String.valueOf(sexChar));
        return animalStream.filter(animal -> animal.sex == animalSex).toList();
    }

    private static List<Animal> filterAnimalsByWeight(Stream<Animal> animalStream) {
        char enterFromWeight = InputService.readCharInValues(
                "Enter weight to filter FROM (1) or skip (2)?",
                "Invalid option. Try again: ",
                new char[]{'1', '2'}
        );
        float weightFrom = enterFromWeight == '1' ? InputService.readFloat(
                "Enter weight in Kg to filter FROM: ",
                "Enter a valid value (0.001 - 99,999): ",
                0.001F, 99_999
        ) : 0.001F;

        char enterToWeight = InputService.readCharInValues(
                "Enter weight to filter TO (1) or skip (2)?",
                "Invalid option. Try again: ",
                new char[]{'1', '2'}
        );
        float weightTo = enterToWeight == '1' ? InputService.readFloat(
                "Enter weight in Kg to filter TO: ",
                String.format("Enter a valid value (%.2f - 99,999): ", weightFrom),
                weightFrom, 99_999
        ) : 99_999;

        return animalStream.filter(animal -> animal.validWeight.test(weightFrom, weightTo)).toList();
    }

    private static List<Animal> filterAnimalsByHeight(Stream<Animal> animalStream) {
        char enterFromHeight = InputService.readCharInValues(
                "Enter height to filter FROM (1) or skip (2)?",
                "Invalid option. Try again: ",
                new char[]{'1', '2'}
        );
        float heightFrom = enterFromHeight == '1' ? InputService.readFloat(
                "Enter height in cm to filter FROM: ",
                "Enter a valid value (0.1 - 9,999): ",
                0.1F, 9_999
        ) : 0.1F;

        char enterToHeight = InputService.readCharInValues(
                "Enter height to filter TO (1) or skip (2)?",
                "Invalid option. Try again: ",
                new char[]{'1', '2'}
        );
        float heightTo = enterToHeight == '1' ? InputService.readFloat(
                "Enter height in cm to filter TO: ",
                String.format("Enter a valid value (%.2f - 9,999): ", heightFrom),
                heightFrom, 9_999
        ) : 9_999;

        return animalStream.filter(animal -> animal.validHeight.test(heightFrom, heightTo)).toList();
    }


}