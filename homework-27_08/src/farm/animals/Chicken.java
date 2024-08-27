package farm.animals;

import farm.Good;
import farm.animals.interfaces.IEggLayer;
import farm.exceptions.IncompatibleBreedingException;
import farm.exceptions.UnableToProduceException;

import java.time.LocalDate;
import java.util.Random;

public final class Chicken extends Animal implements IEggLayer {
    public enum CoopLocation {
        CENTRAL, HILLTOP, LAKESIDE, BARNYARD;

        private final int value;
        CoopLocation() {
            this.value = this.ordinal()+1;
        }

        public static CoopLocation getCoopLocation(int value) {
            for (CoopLocation cl : CoopLocation.values()) {
                if (cl.value == value)
                    return cl;
            }

            return null;
        }

        public static String getAll() {
            StringBuilder cl = new StringBuilder();
            for (CoopLocation es : CoopLocation.values()) {
                cl.append(String.format("%d. %s\n", es.value, es));
            }

            return cl.toString();
        }
    }

    private int eggPerDay;
    private EggSize eggSize;
    public CoopLocation coopLocation;

    public int getEggPerDay() {
        return eggPerDay;
    }
    public void setEggPerDay(int eggPerDay) {
        this.eggPerDay = this.sex == AnimalSex.F ? eggPerDay : 0;
    }

    public EggSize getEggSize() {
        return eggSize;
    }
    public void setEggSize(EggSize eggSize) {
        this.eggSize = this.sex == AnimalSex.F ? eggSize : null;
    }

    public Chicken(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, int eggPerDay, EggSize eggSize, CoopLocation coopLocation) {
        super(Species.CHICKEN, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.setEggPerDay(eggPerDay);
        this.setEggSize(eggSize);
        this.coopLocation = coopLocation;
    }
    public Chicken(LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, CoopLocation coopLocation) {
        this(dateOfBirth, food, sex, weightInKg, heightInCm, 0, null, coopLocation);
    }

    public Chicken(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, int eggPerDay, EggSize eggSize, CoopLocation coopLocation) {
        super(Species.CHICKEN, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.setEggPerDay(eggPerDay);
        this.setEggSize(eggSize);
        this.coopLocation = coopLocation;
    }
    public Chicken(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, CoopLocation coopLocation) {
        this(id, dateOfBirth, food, sex, weightInKg, heightInCm, 0, null, coopLocation);
    }

    @Override
    public Chicken breedWith(Animal partner) throws IncompatibleBreedingException {
        if (!Animal.compatibleForBreeding(this, partner))
            throw new IncompatibleBreedingException(this, partner);

        Chicken father, mother;
        if (this.sex == AnimalSex.M) {
            father = this;
            mother = (Chicken) partner;
        } else {
            father = (Chicken) partner;
            mother = this;
        }

        final Random RANDOM = new Random();
        AnimalSex sex = RANDOM.nextInt(1, 2) == 1 ? AnimalSex.M : AnimalSex.F;
        float weight = RANDOM.nextFloat(.003F, .0045F);
        float height = RANDOM.nextFloat(5F, 7F);

        Chicken newChicken = new Chicken(LocalDate.now(), "Grain", sex, weight, height, mother.coopLocation);
        newChicken.setFather(father);

        newChicken.setMother(mother);

        return newChicken;
    }

    @Override
    public Good getEggs() throws UnableToProduceException {
        if (this.sex == AnimalSex.M)
            throw new UnableToProduceException("Roosters do not lay eggs.");

        final Random RANDOM = new Random();
        return new Good(
                "Eggs",
                RANDOM.nextInt(9, 12),
                "Units",
                Good.GoodsQuality.getRandomQuality(),
                RANDOM.nextFloat(0.25F, 0.7F)
        );
    }
}
