package farm;

import farm.animals.Animal;
import farm.crops.CropSector;
import farm.exceptions.MissingGoodException;
import farm.exceptions.RepeatedInstanceException;
import myCollections.MyLinkedList;

import java.util.*;

public final class Farm {
    public String name;
    public String location;
    public float size;
    private List<Animal> animals;
    private List<CropSector> cropSectors;
    private MyLinkedList<Good> stock;
    private MyLinkedList<Good> soldGoods;

    public List<Animal> getAnimals() {
        return List.copyOf(animals);
    }

    public void setAnimals(List<Animal> animals) {
        this.animals = animals;
    }

    public List<CropSector> getCrops() {
        return List.copyOf(cropSectors);
    }

    public void setCrops(List<CropSector> cropSectors) {
        this.cropSectors = cropSectors;
    }

    public MyLinkedList<Good> getStock() {
        return stock.clone();
    }

    public MyLinkedList<Good> getSoldGoods() {
        return soldGoods.clone();
    }

    public float getTotalValueSold() {
        float total = 0;
        for (Good gd : this.soldGoods) {
            total += gd.getTotalValue();
        }

        return total;
    }

    public Farm(String name, String location, float size, List<Animal> animals, List<CropSector> cropSectors, MyLinkedList<Good> stock, MyLinkedList<Good> soldGoods) {
        this.name = name;
        this.location = location;
        this.size = size;
        this.animals = animals;
        this.cropSectors = cropSectors;
        this.stock = stock;
        this.soldGoods = soldGoods;
    }

    public Farm(String name, String location, float size) {
        this(name, location, size, new ArrayList<>(), new ArrayList<>(), new MyLinkedList<>(), new MyLinkedList<>());
    }

    @Override
    public String toString() {
        return "Name: " + this.name + "\nLocation: " + this.location + "\nSize: " + this.size + " acres\n" +
                "\n=========CROPS=========\n" +
                CropSector.toTable(this.cropSectors) +
                "\n========ANIMALS========\n" +
                Animal.toTable(this.animals) +
                "\n=========GOODS=========\n" +
                Good.toTable(this.stock.toArrayList()) +
                "\n======SOLD GOODS=======\n" +
                Good.toTable(this.soldGoods.toArrayList()) +
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

    public void addCrop(CropSector cropSector) throws RepeatedInstanceException {
        if (this.cropSectors.contains(cropSector))
            throw new RepeatedInstanceException("Crop sector already exists in list.");

        this.cropSectors.add(cropSector);
    }

    private void addGood(Good good, MyLinkedList<Good> goodsList) throws RepeatedInstanceException {
        if (goodsList.contains(good))
            throw new RepeatedInstanceException("Good already exists in list.");

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
