package labaFarm.farm.animals.interfaces;

import labaFarm.farm.animals.Animal;

@FunctionalInterface
public interface IAnimalFilter<T extends Animal> {
    boolean filter(T animal);
}
