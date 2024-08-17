package farm;

import farm.animals.Animal;
import farm.crops.Crop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Farm {
    public String name;
    public String location;
    public float size;
    public List<Animal> animals;
    public List<Crop> crops;
    public List<Good> stock;
    public List<Good> soldGoods;

    public Farm(String name, String location, float size, List<Animal> animals, List<Crop> crops, List<Good> stock, List<Good> soldGoods) {
        this.name = name;
        this.location = location;
        this.size = size;
        this.animals = animals;
        this.crops = crops;
        this.stock = stock;
        this.soldGoods = soldGoods;
    }

    public Farm(String name, String location, float size) {
        this(name, location, size, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public int hashCode() {
        return Objects.hash(this.name, this.location);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return this.hashCode() == obj.hashCode();
    }

    public List<Animal> addAnimal(Animal animal) {
        for (Animal an : this.animals) {
            if (an.equals(animal))
                return List.copyOf(this.animals); //TODO: Thrown exception
        }

        this.animals.add(animal);

        return List.copyOf(this.animals);
    }

    public List<Crop> addCrop(Crop crop) {
        for (Crop cr : this.crops) {
            if (cr.equals(crop))
                return List.copyOf(this.crops); //TODO: Thrown exception
        }

        this.crops.add(crop);

        return List.copyOf(this.crops);
    }

    public String toString() {
        return "Name: " + this.name + "\nLocation: " + this.location + "\nSize: " + this.size + "ac\n" +
                "\n=========CROPS=========\n" +
                Crop.toString(this.crops) +
                "\n========ANIMALS========\n" +
                Animal.toString(this.animals) +
                "\n=========GOODS=========\n" +
                Good.toString(this.stock) +
                "\n======SOLD GOODS=======\n" +
                Good.toString(this.soldGoods);
    }
}
