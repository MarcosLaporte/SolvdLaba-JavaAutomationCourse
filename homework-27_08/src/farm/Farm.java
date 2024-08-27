package farm;

import farm.animals.Animal;
import farm.crops.Crop;
import farm.exceptions.MissingGoodException;
import farm.exceptions.RepeatedInstanceException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Farm {
    public String name;
    public String location;
    public float size;
    private List<Animal> animals;
    private List<Crop> crops;
    private List<Good> stock;
    private List<Good> soldGoods;

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }
    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public List<Crop> getCrops() {
        return List.copyOf(crops);
    }
    public void setCrops(List<Crop> crops) {
        this.crops = crops;
    }

    public List<Good> getSoldGoods() {
        return List.copyOf(soldGoods);
    }

    public List<Good> getStock() {
        return List.copyOf(stock);
    }

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

    public void addAnimal(Animal animal) throws RepeatedInstanceException {
        for (Animal an : this.animals) {
            if (an.equals(animal))
                throw new RepeatedInstanceException("Animal already exists in list.");
        }

        this.animals.add(animal);
    }

    public void addCrop(Crop crop) throws RepeatedInstanceException {
        for (Crop cr : this.crops) {
            if (cr.equals(crop))
                throw new RepeatedInstanceException("Crop already exists in list.");
        }

        this.crops.add(crop);
    }

    private void addGood(Good good, List<Good> goodsList) throws RepeatedInstanceException {
        for (Good gd : goodsList) {
            if (gd.equals(good))
                throw new RepeatedInstanceException("Good already exists in list.");
        }

        goodsList.add(good);
    }

    public void addGoodToStock(Good good) throws RepeatedInstanceException {
        this.addGood(good, this.stock);
    }

    public void sellGood(Good goodToSell) throws MissingGoodException {
        if (!this.stock.contains(goodToSell))
            throw new MissingGoodException("This good does not exist in farm's stock.");

        addGood(goodToSell, this.soldGoods);
        this.stock.remove(goodToSell);
    }
}
