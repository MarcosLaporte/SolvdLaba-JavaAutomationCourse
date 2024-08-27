package farm.exceptions;

import farm.animals.Animal;

public class IncompatibleBreedingException extends RuntimeException {
    public IncompatibleBreedingException(String message) {
        super(message);
    }

    public IncompatibleBreedingException(Animal animal1, Animal animal2) {
        super(String.format("Can't breed %s-%s with %s-%s", animal1.species, animal1.sex, animal2.species, animal2.sex));
    }
}
