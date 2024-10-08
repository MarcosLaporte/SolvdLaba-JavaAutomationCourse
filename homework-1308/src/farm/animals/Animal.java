package farm.animals;

import java.util.Random;

public abstract class Animal {
    public enum AnimalSex {F, M}

    private int id;
    public int age;
    public String food;
    public AnimalSex sex;
    public float weight;
    public float height;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Animal(int age, String food, AnimalSex sex, float weight, float height) {
        int randId = new Random().nextInt(1, 9999);
        setId(randId);
        this.age = age;
        this.food = food;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
    }

    public Animal(int id, int age, String food, AnimalSex sex, float weight, float height) {
        this(age, food, sex, weight, height);
        setId(id);
    }

    public String toString() {
        return String.format("| %-4d | %-3d | %-10s | %-3s | %-6.2f | %-6.2f |\n",
                this.id, this.age, this.food, this.sex, this.weight, this.height);
    }
}
