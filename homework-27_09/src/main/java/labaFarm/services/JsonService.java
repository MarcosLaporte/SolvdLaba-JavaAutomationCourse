package labaFarm.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import labaFarm.farm.animals.*;
import labaFarm.farm.crops.Corn;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.crops.Tomato;
import labaFarm.farm.crops.Wheat;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class JsonService {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .setDateFormat("dd-MM-YYYY")
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(Animal.class, new AnimalAdapter())
                .registerTypeAdapter(CropSector.class, new CropAdapter())
                .create();
    }

    public static <T> T readData(String filePath, Class<T> clazz) {
        try {
            String jsonData = FileService.readFile(filePath);
            return GSON.fromJson(jsonData, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> readDataList(String filePath, Class<T> clazz) {
        try {
            String jsonData = FileService.readFile(filePath);
            Type listType = TypeToken.getParameterized(List.class, clazz).getType();
            return GSON.fromJson(jsonData, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void saveData(String filePath, T data) {
        try {
            String jsonData = GSON.toJson(data);
            FileService.writeFile(filePath, jsonData, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class LocalDateAdapter extends TypeAdapter<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        if (value != null) {
            out.value(value.format(formatter));
        } else {
            out.nullValue();
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in != null && in.peek() != null) {
            return LocalDate.parse(in.nextString(), formatter);
        }
        return null;
    }
}

class AnimalAdapter extends TypeAdapter<Animal> {
    private static final Gson GSON = new Gson();

    @Override
    public void write(JsonWriter out, Animal animal) throws IOException {
        out.beginObject();
        out.name("species").value(animal.species.name());
        out.name("id").value(animal.getId());
        out.name("dateOfBirth").value(animal.getDateOfBirth().toString());
        out.name("food").value(animal.food);
        out.name("sex").value(animal.sex.name());
        out.name("weightInKg").value(animal.weightInKg);
        out.name("heightInCm").value(animal.heightInCm);


        switch (animal) {
            case Cattle cattle -> out.name("breed").value(cattle.breed.name());
            case Sheep sheep -> {
                out.name("isTrained").value(sheep.isTrained);
                out.name("woolType").value(sheep.woolType.name());
            }
            case Horse horse -> {
                out.name("isForCompetition").value(horse.isForCompetition);
                out.name("speed").value(horse.speed);
                out.name("isRideable").value(horse.isRideable);
            }
            case Goat goat -> out.name("mohairType").value(goat.mohairType.name());
            case Chicken chicken -> {
                out.name("eggPerDay").value(chicken.getEggPerDay());
                String eggSize = chicken.getEggSize() != null ? chicken.getEggSize().name() : null;
                out.name("eggSize").value(eggSize);
                out.name("coopLocation").value(chicken.coopLocation.name());
            }
            default -> {}
        }

        out.endObject();
    }

    @Override
    public Animal read(JsonReader in) {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> properties = GSON.fromJson(in, type);

        String speciesStr = (String) properties.get("species");
        Animal.Species species = Animal.Species.valueOf(speciesStr);
        int id = ((Double) properties.get("id")).intValue();
        LocalDate dateOfBirth = LocalDate.parse((String) properties.get("dateOfBirth"));
        String food = (String) properties.get("food");
        Animal.AnimalSex sex = Animal.AnimalSex.valueOf((String) properties.get("sex"));
        float weightInKg = ((Double) properties.get("weightInKg")).floatValue();
        float heightInCm = ((Double) properties.get("heightInCm")).floatValue();

        switch (species) {
            case CATTLE:
                Cattle.CattleBreed breed = Cattle.CattleBreed.valueOf((String) properties.get("breed"));
                return new Cattle(id, dateOfBirth, food, sex, weightInKg, heightInCm, breed);
            case SHEEP:
                boolean isTrained = (Boolean) properties.get("isTrained");
                Sheep.FurType woolType = Sheep.FurType.valueOf((String) properties.get("woolType"));
                return new Sheep(id, dateOfBirth, food, sex, weightInKg, heightInCm, isTrained, woolType);
            case HORSE:
                boolean isForCompetition = (Boolean) properties.get("isForCompetition");
                float speed = ((Double) properties.get("speed")).floatValue();
                boolean isRideable = (Boolean) properties.get("isRideable");
                return new Horse(id, dateOfBirth, food, sex, weightInKg, heightInCm, isForCompetition, speed, isRideable);
            case GOAT:
                Goat.FurType mohairType = Goat.FurType.valueOf((String) properties.get("mohairType"));
                return new Goat(id, dateOfBirth, food, sex, weightInKg, heightInCm, mohairType);
            case CHICKEN:
                int eggPerDay = ((Double) properties.get("eggPerDay")).intValue();
                String eggSizeValue = (String) properties.get("eggSize");
                Chicken.EggSize eggSize = eggSizeValue != null ? Chicken.EggSize.valueOf(eggSizeValue) : null;
                Chicken.CoopLocation coopLocation = Chicken.CoopLocation.valueOf((String) properties.get("coopLocation"));
                return new Chicken(id, dateOfBirth, food, sex, weightInKg, heightInCm, eggPerDay, eggSize, coopLocation);
            default:
                throw new IllegalArgumentException("Unknown species: " + species);
        }
    }
}

class CropAdapter extends TypeAdapter<CropSector> {
    private static final Gson GSON = new Gson();

    @Override
    public void write(JsonWriter out, CropSector cropSector) throws IOException {
        out.beginObject();
        out.name("type").value(cropSector.type.name());
        out.name("id").value(cropSector.getId());
        out.name("acres").value(cropSector.acres);
        out.name("daysToGrow").value(cropSector.daysToGrow);
        out.name("currentGrowthStage").value(cropSector.currentGrowthStage.name());

        switch (cropSector) {
            case Corn corn -> {
                out.name("kernelType").value(corn.kernelType.name());
                out.name("avgKernelSize").value((corn.avgKernelSize));
            }
            case Tomato tomato -> {
                out.name("variety").value(tomato.variety.name());
                out.name("yieldPerPlant").value(tomato.yieldPerPlant);
            }
            case Wheat wheat -> {
                out.name("variety").value(wheat.variety.name());
                out.name("mgOfGluten").value(wheat.mgOfGluten);
            }
            default -> {}
        }

        out.endObject();
    }

    @Override
    public CropSector read(JsonReader in) {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> properties = GSON.fromJson(in, type);

        String typeStr = (String) properties.get("type");
        CropSector.CropType cropType = CropSector.CropType.valueOf(typeStr);
        int id = ((Double) properties.get("id")).intValue();
        float acres = ((Double) properties.get("acres")).floatValue();
        int daysToGrow = ((Double) properties.get("daysToGrow")).intValue();
        String currentGrowthStageStr = (String) properties.get("currentGrowthStage");
        CropSector.GrowthStage currentGrowthStage = CropSector.GrowthStage.valueOf(currentGrowthStageStr);

        switch (cropType) {
            case CORN:
                String kernelTypeStr = (String) properties.get("kernelType");
                Corn.KernelType kernelType = Corn.KernelType.valueOf(kernelTypeStr);
                float avgKernelSize = ((Double) properties.get("avgKernelSize")).floatValue();
                return new Corn(id, acres, daysToGrow, currentGrowthStage, kernelType, avgKernelSize);
            case TOMATO:
                String tomatoVarietyStr = (String) properties.get("variety");
                Tomato.TomatoVariety tomatoVariety = Tomato.TomatoVariety.valueOf(tomatoVarietyStr);
                int yieldPerPlant = ((Double) properties.get("yieldPerPlant")).intValue();
                return new Tomato(id, acres, daysToGrow, currentGrowthStage, tomatoVariety, yieldPerPlant);
            case WHEAT:
                String wheatVarietyStr = (String) properties.get("variety");
                Wheat.WheatVariety wheatVariety = Wheat.WheatVariety.valueOf(wheatVarietyStr);
                int mgOfGluten = ((Double) properties.get("mgOfGluten")).intValue();
                return new Wheat(id, acres, daysToGrow, currentGrowthStage, wheatVariety, mgOfGluten);
            default:
                throw new IllegalArgumentException("Unknown crop type: " + cropType);
        }
    }
}