package farm;

import farm.animals.Animal;
import farm.crops.Crop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Farm {
    public String name;
    public String location;
    public float size; //
    public List<Animal> animals;
    public List<Crop> crops;
    public List<Good> stock;
    public List<Good> soldGoods;

    public float getTotalValueSold() {
        float total = 0;
        for (Good gd : this.soldGoods) {
            total += gd.getTotalValue();
        }

        return total;
    }

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

    @Override
    public String toString() {
        return "Name: " + this.name + "\nLocation: " + this.location + "\nSize: " + this.size + " acres\n" +
                "\n=========CROPS=========\n" +
                Crop.toTable(this.crops) +
                "\n========ANIMALS========\n" +
                Animal.toTable(this.animals) +
                "\n=========GOODS=========\n" +
                Good.toTable(this.stock) +
                "\n======SOLD GOODS=======\n" +
                Good.toTable(this.soldGoods) +
                "Total sold: $" + this.getTotalValueSold();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.location);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        return this.hashCode() == obj.hashCode();
    }

    public List<Animal> addAnimal(Animal animal) {
        for (Animal an : this.animals) {
            if (an.equals(animal))
                return List.copyOf(this.animals); //TODO: Throw exception
        }

        this.animals.add(animal);

        return List.copyOf(this.animals);
    }

    public List<Crop> addCrop(Crop crop) {
        for (Crop cr : this.crops) {
            if (cr.equals(crop))
                return List.copyOf(this.crops); //TODO: Throw exception
        }

        this.crops.add(crop);

        return List.copyOf(this.crops);
    }

    public String toString() {
        return "Name: " + this.name + "\nLocation: " + this.location + "\nSize: " + this.size + " acres\n" +
                "\n=========CROPS=========\n" +
                Crop.toTable(this.crops) +
                "\n========ANIMALS========\n" +
                Animal.toTable(this.animals) +
                "\n=========GOODS=========\n" +
                Good.toString(this.stock) +
                "\n======SOLD GOODS=======\n" +
                Good.toString(this.soldGoods) +
                "Total sold: $" + this.getTotalValueSold();
    }

    public float sellGood(Good goodToSell) throws Exception {
        if (!this.stock.contains(goodToSell))
            throw new Exception("This good does not exist in farm's stock.");

        this.soldGoods.add(goodToSell);
        this.stock.remove(goodToSell);

        return goodToSell.getTotalValue();
    }
}
