package labaFarm.farm.animals.interfaces;

import labaFarm.farm.animals.Animal;

@FunctionalInterface
public interface IAnimalAction<T extends Animal> {
    void perform(T animal);
}
