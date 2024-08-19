package farm.animals;

import farm.Good;

import java.time.LocalDate;
import java.util.Random;

public class Sheep extends Animal {
    public enum WoolType {
        FINE, DOWN, MEDIUM, LONG, DOUBLE_COATED;

        private final int value;
        WoolType() {
            this.value = this.ordinal()+1;
        }

        public static Sheep.WoolType getWoolType(int value) {
            for (Sheep.WoolType wt : Sheep.WoolType.values()) {
                if (wt.value == value)
                    return wt;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder sb = new StringBuilder();
            for (Sheep.WoolType wt : Sheep.WoolType.values()) {
                sb.append(String.format("%d. %s\n", wt.value, wt));
            }

            return sb.toString();
        }
    }

    public boolean isTrained;
    public WoolType woolType;
    protected final static String producedGoods = "WOOL";

    @Override
    public String getProducedGoods() {
        return producedGoods;
    }

    public Sheep(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained, WoolType woolType) {
        super(Species.SHEEP, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isTrained = isTrained;
        this.woolType = woolType;
    }
    public Sheep(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained) {
        this(dateOfBirth, food, sex, weightInKg, heightInCm, isTrained, WoolType.FINE);
    }

    public Sheep(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained, WoolType woolType) {
        super(Species.SHEEP, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.isTrained = isTrained;
        this.woolType = woolType;
    }
    public Sheep(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, boolean isTrained) {
        this(id, dateOfBirth, food, sex, weightInKg, heightInCm, isTrained, WoolType.FINE);
    }

    @Override
    public Sheep breed(Animal partner) throws Exception {
        if (!Animal.compatibleForBreeding(this, partner))
            throw new Exception(
                String.format("Can't breed %s-%s with %s-%s", this.species, this.sex, partner.species, partner.sex)
            );

        Sheep father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Sheep) partner;
        } else {
            father = (Sheep) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(1F, 1.8F);
        float height = RANDOM.nextFloat(20F, 30F);

        Sheep newSheep = new Sheep(LocalDate.now(), "Milk", sex, weight, height, false, mother.woolType);
        newSheep.setFather(father);
        newSheep.setMother(mother);

        return newSheep;
    }

    @Override
    public Good produceGoods() {
        final Random RANDOM = new Random();

        return new Good(
                this.getProducedGoods(),
                RANDOM.nextFloat(2, 4),
                "Kg",
                Good.GoodsQuality.getRandomQuality(),
                LocalDate.now(),
                RANDOM.nextFloat(800, 1750)
        );
    }
}
