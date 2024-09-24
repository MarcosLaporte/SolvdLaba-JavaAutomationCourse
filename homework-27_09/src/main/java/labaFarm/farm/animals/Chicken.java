package labaFarm.farm.animals;

import labaFarm.farm.Good;
import labaFarm.farm.animals.interfaces.IAnimalFilter;
import labaFarm.farm.animals.interfaces.IEggLayer;
import labaFarm.farm.exceptions.IncompatibleBreedingException;
import labaFarm.farm.exceptions.UnableToProduceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

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

    public Chicken(int id, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, int eggPerDay, EggSize eggSize, CoopLocation coopLocation) {
        super(Species.CHICKEN, id, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.setEggPerDay(eggPerDay);
        this.setEggSize(eggSize);
        this.coopLocation = coopLocation;
    }
    private Chicken(Integer id, LocalDate dateOfBirth, String food, AnimalSex sex, Float weightInKg, Float heightInCm, Integer eggPerDay, EggSize eggSize, CoopLocation coopLocation) {
        this(id.intValue(), dateOfBirth, food, sex, weightInKg.floatValue(), heightInCm.floatValue(), eggPerDay.intValue(), eggSize, coopLocation);
    }

    public Chicken(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, float weightInKg, float heightInCm, int eggPerDay, EggSize eggSize, CoopLocation coopLocation) {
        super(Species.CHICKEN, existingAnimals, dateOfBirth, food, sex, weightInKg, heightInCm);
        this.setEggPerDay(eggPerDay);
        this.setEggSize(eggSize);
        this.coopLocation = coopLocation;
    }
    private Chicken(List<Animal> existingAnimals, LocalDate dateOfBirth, String food, AnimalSex sex, Float weightInKg, Float heightInCm, Integer eggPerDay, EggSize eggSize, CoopLocation coopLocation) {
        this(existingAnimals, dateOfBirth, food, sex, weightInKg.floatValue(), heightInCm.floatValue(), eggPerDay.intValue(), eggSize, coopLocation);
    }

    @Override
    public Chicken breedWith(Animal partner, List<Animal> animalList) throws IncompatibleBreedingException {
        if (!compatibleForBreeding.test(this, partner))
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

        Chicken newChicken = new Chicken(animalList, LocalDate.now(), "Grain", sex, weight, height, 0, null, mother.coopLocation);
        newChicken.setFather(father);

        newChicken.setMother(mother);

        return newChicken;
    }

    private transient final Supplier<Integer> eggCountSupplier = () -> new Random().nextInt(9, 12);
    @Override
    public Good getEggs() throws UnableToProduceException {
        if (this.sex == AnimalSex.M)
            throw new UnableToProduceException("Roosters do not lay eggs.");

        final Random RANDOM = new Random();
        return new Good(
                "Eggs",
//                RANDOM.nextInt(9, 12),
                eggCountSupplier.get(),
                "Units",
                Good.GoodsQuality.randomQualitySupplier.get(),
                RANDOM.nextFloat(0.25F, 0.7F)
        );
    }

    public static IAnimalFilter<Chicken> createEggSizeFilter(EggSize eggSize) {
        return chicken -> chicken.eggSize == eggSize;
    }
}
