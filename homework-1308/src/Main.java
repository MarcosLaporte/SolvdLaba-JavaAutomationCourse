import farm.Farm;

import farm.animals.Animal;
import farm.animals.Cow;
import farm.animals.Horse;
import farm.animals.Pig;

import farm.crops.Corn;
import farm.crops.Crop;
import farm.crops.Tomato;
import farm.crops.Wheat;

import farm.people.AnimalCaretaker;
import farm.people.CropsCultivator;
import farm.people.Farmer;
import farm.people.Owner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Farm manorFarm = new Farm("Manor Farm", "Willingdon, England", 152.3F);

        /* CROPS */
        List<Crop> crops1 = new ArrayList<>();
        crops1.add(new Wheat(5, 90, Crop.GrowthStage.FLOWERING, Wheat.WheatVariety.HARD_WHITE, 6000));
        crops1.add(new Wheat(4, 100, Wheat.WheatVariety.DURUM));
        crops1.add(new Corn(130, Crop.GrowthStage.FRUITING, Corn.KernelType.FLINT, 7.3F));
        crops1.add(new Corn(120, Corn.KernelType.DENT));
        crops1.add(new Tomato(6, 70, Crop.GrowthStage.HARVEST, Tomato.TomatoVariety.BIG_BEEF, 50));

        List<Crop> crops2 = new ArrayList<>();
        crops2.add(new Wheat(3, 85, Wheat.WheatVariety.HARD_RED_SPRING, 7500));
        crops2.add(new Wheat(7, 95, Crop.GrowthStage.FLOWERING, Wheat.WheatVariety.SOFT_RED_WINTER));
        crops2.add(new Corn(12, 140, Crop.GrowthStage.HARVEST, Corn.KernelType.POP, 8.0F));
        crops2.add(new Corn(9, 115, Corn.KernelType.SWEET));
        crops2.add(new Tomato(5, 65, Crop.GrowthStage.FRUITING, Tomato.TomatoVariety.BRANDYWINE, 48));

        List<Crop> crops3 = new ArrayList<>();
        crops3.add(new Wheat(6, 92, Crop.GrowthStage.SEEDLING, Wheat.WheatVariety.HARD_RED_WINTER));
        crops3.add(new Wheat(8, 105, Crop.GrowthStage.VEGETATIVE, Wheat.WheatVariety.HARD_WHITE, 7300));
        crops3.add(new Corn(11, 135, Corn.KernelType.WAXY, 7.8F));
        crops3.add(new Corn(7, 125, Corn.KernelType.FLOUR, 8.5F));
        crops3.add(new Tomato(9, 75, Crop.GrowthStage.MATURITY, Tomato.TomatoVariety.CHERRY));

        List<Crop> crops4 = new ArrayList<>();
        crops4.add(new Wheat(2, 78, Wheat.WheatVariety.SOFT_WHITE));
        crops4.add(new Wheat(10, 110, Crop.GrowthStage.MATURITY, Wheat.WheatVariety.DURUM, 8000));
        crops4.add(new Corn(15, 145, Crop.GrowthStage.FLOWERING, Corn.KernelType.FLINT, 7.5F));
        crops4.add(new Corn(14, 150, Corn.KernelType.DENT));
        crops4.add(new Tomato(6, 68, Tomato.TomatoVariety.BETTER_BOY, 47));

        /* ANIMALS */
        List<Animal> animals1 = new ArrayList<>();
        animals1.add(new Cow(5, "Grass", Animal.AnimalSex.F, 500.0F, 1.5F, true, 3000));
        animals1.add(new Pig(2, "Corn", Animal.AnimalSex.M, 150.0F, 0.9F, true, Pig.PigPen.NORTH_BARN));
        animals1.add(new Horse(7, "Hay", Animal.AnimalSex.M, 700.0F, 1.8F, true, 75.0F, false));
        animals1.add(new Cow(3, "Grass", Animal.AnimalSex.F, 450.0F, 1.4F, false));
        animals1.add(new Pig(1, "Slop", Animal.AnimalSex.F, 100.0F, 0.8F, false));

        List<Animal> animals2 = new ArrayList<>();
        animals2.add(new Horse(4, "Oats", Animal.AnimalSex.M, 600.0F, 1.6F, true, 60.0F));
        animals2.add(new Pig(3, "Corn", Animal.AnimalSex.F, 160.0F, 0.95F, true, Pig.PigPen.SOUTH_BARN));
        animals2.add(new Cow(6, "Hay", Animal.AnimalSex.F, 480.0F, 1.5F, true, 2750));
        animals2.add(new Horse(5, "Grass", Animal.AnimalSex.F, 650.0F, 1.7F, false, 70.0F));
        animals2.add(new Pig(2, "Grains", Animal.AnimalSex.M, 140.0F, 0.9F, true));

        List<Animal> animals3 = new ArrayList<>();
        animals3.add(new Cow(8, "Grass", Animal.AnimalSex.M, 520.0F, 1.6F, true, 3100));
        animals3.add(new Pig(4, "Vegetables", Animal.AnimalSex.F, 170.0F, 1.0F, true, Pig.PigPen.WEST_PASTURE));
        animals3.add(new Horse(6, "Hay", Animal.AnimalSex.M, 720.0F, 1.9F, true, 80.0F));
        animals3.add(new Cow(7, "Hay", Animal.AnimalSex.F, 470.0F, 1.45F, false));
        animals3.add(new Pig(5, "Slop", Animal.AnimalSex.M, 160.0F, 0.85F, false));

        List<Animal> animals4 = new ArrayList<>();
        animals4.add(new Horse(3, "Oats", Animal.AnimalSex.F, 590.0F, 1.6F, false, 55.0F));
        animals4.add(new Pig(6, "Corn", Animal.AnimalSex.M, 180.0F, 1.05F, true, Pig.PigPen.EAST_FIELD));
        animals4.add(new Cow(5, "Grass", Animal.AnimalSex.F, 490.0F, 1.55F, true, 2900));
        animals4.add(new Horse(7, "Hay", Animal.AnimalSex.M, 730.0F, 1.85F, true, 78.0F, true));
        animals4.add(new Pig(4, "Vegetables", Animal.AnimalSex.F, 165.0F, 0.9F, true));

        /* FARMERS */
        List<CropsCultivator> cropsCultivators = new ArrayList<>();
        cropsCultivators.add(new CropsCultivator("John Doe", "123-45-6789", 72000, crops1, Farmer.FarmerShift.MORNING));
        cropsCultivators.add(new CropsCultivator("Jane Smith", "987-65-4321", crops2, Farmer.FarmerShift.EVENING));
        cropsCultivators.add(new CropsCultivator("Jim Beam", "112-23-3445", 67000, crops3));
        cropsCultivators.add(new CropsCultivator("Anna Belle", "223-34-4556", crops4));

        List<AnimalCaretaker> animalCaretakers = new ArrayList<>();
        animalCaretakers.add(new AnimalCaretaker("Tom Farmer", "334-55-6677", 68000, animals1, Farmer.FarmerShift.MORNING));
        animalCaretakers.add(new AnimalCaretaker("Sara Green", "445-66-7788", animals2, Farmer.FarmerShift.EVENING));
        animalCaretakers.add(new AnimalCaretaker("Bob White", "556-77-8899", 71000, animals3));
        animalCaretakers.add(new AnimalCaretaker("Linda Blue", "667-88-9900", animals4));

        List<Farmer> employees = new ArrayList<>();
        employees.addAll(cropsCultivators);
        employees.addAll(animalCaretakers);

        Owner owner = new Owner("Mr. Jones", "243-12-5768", 90_000, manorFarm, 750_452.6, employees);

        System.out.println("=================================================================\n");
        System.out.println(owner.employeesToString());
        System.out.println("=================================================================\n");
        System.out.println(cropsCultivators.get(new Random().nextInt(0, cropsCultivators.size())).cropsToString());
        System.out.println("=================================================================\n");
        System.out.println(animalCaretakers.get(new Random().nextInt(0, animalCaretakers.size())).animalsToString());
    }
}