package farm.animals;

public class Horse extends Animal {
    boolean isForCompetition;
    float speed;
    boolean isRideable;

    public Horse(int age, String food, AnimalSex sex, float weight, float height, boolean isForCompetition, float speed, boolean isRideable) {
        super(age, food, sex, weight, height);
        this.isForCompetition = isForCompetition;
        this.speed = speed;
        this.isRideable = isRideable;
    }
    public Horse(int age, String food, AnimalSex sex, float weight, float height, boolean isForCompetition, float speed) {
        this(age, food, sex, weight, height, isForCompetition, speed, true);
    }

    public Horse(int id, int age, String food, AnimalSex sex, float weight, float height, boolean isForCompetition, float speed, boolean isRideable) {
        super(id, age, food, sex, weight, height);
        this.isForCompetition = isForCompetition;
        this.speed = speed;
        this.isRideable = isRideable;
    }
    public Horse(int id, int age, String food, AnimalSex sex, float weight, float height, boolean isForCompetition, float speed) {
        this(id, age, food, sex, weight, height, isForCompetition, speed, true);
    }
}
