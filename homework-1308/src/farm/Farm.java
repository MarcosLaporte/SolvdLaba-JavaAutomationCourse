package farm;

import farm.animals.Animal;
import farm.crops.Crop;

import java.util.ArrayList;
import java.util.List;

public class Farm {
    public String name;
    public String location;
    public float size;
    public List<Animal> animals;
    public List<Crop> crops;

    public Farm(String name, String location, float size, List<Animal> animals, List<Crop> crops) {
        this.name = name;
        this.location = location;
        this.size = size;
        this.animals = animals;
        this.crops = crops;
    }

    public Farm(String name, String location, float size) {
        this(name, location, size, new ArrayList<>(), new ArrayList<>());
    }
}
