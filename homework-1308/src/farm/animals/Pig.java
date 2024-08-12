package farm.animals;

public class Pig extends Animal {
    public enum PigPen {NORTH_BARN, SOUTH_BARN, EAST_FIELD, WEST_PASTURE, CENTRAL_STABLE}

    boolean isTrained;
    PigPen penLocation;

    public Pig(int age, String food, AnimalSex sex, float weight, float height, boolean isTrained, PigPen penLocation) {
        super(age, food, sex, weight, height);
        this.isTrained = isTrained;
        this.penLocation = penLocation;
    }
    public Pig(int age, String food, AnimalSex sex, float weight, float height, boolean isTrained) {
        this(age, food, sex, weight, height, isTrained, PigPen.CENTRAL_STABLE);
    }

    public Pig(int id, int age, String food, AnimalSex sex, float weight, float height, boolean isTrained, PigPen penLocation) {
        super(id, age, food, sex, weight, height);
        this.isTrained = isTrained;
        this.penLocation = penLocation;
    }
    public Pig(int id, int age, String food, AnimalSex sex, float weight, float height, boolean isTrained) {
        this(id, age, food, sex, weight, height, isTrained, PigPen.CENTRAL_STABLE);
    }
}
