package labaFarm.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import labaFarm.farm.Farm;
import labaFarm.farm.Good;
import labaFarm.farm.animals.*;
import labaFarm.farm.crops.Corn;
import labaFarm.farm.crops.CropSector;
import labaFarm.farm.crops.Tomato;
import labaFarm.farm.crops.Wheat;
import labaFarm.farm.people.Owner;
import labaFarm.farm.people.employees.AnimalCaretaker;
import labaFarm.farm.people.employees.CropsCultivator;
import labaFarm.farm.people.employees.Employee;
import labaFarm.myCollections.MyLinkedList;

import static labaFarm.farm.people.employees.Employee.WorkShift;
import static labaFarm.farm.people.employees.Employee.EmployeeType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class JsonService {
    private static final Gson GSON;

    static {
        GSON = new GsonBuilder()
                .registerTypeAdapter(Owner.class, new OwnerAdapter())
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

    public static <T> void saveData(String filePath, T data) {
        try {
            String jsonData = GSON.toJson(data);
            FileService.writeFile(filePath, jsonData, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class OwnerAdapter extends TypeAdapter<Owner> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Farm.class, new FarmAdapter())
            .registerTypeAdapter(Employee.class, new EmployeeAdapter())
            .create();

    @Override
    public void write(JsonWriter out, Owner owner) throws IOException {
        out.beginObject();
        out.name("fullName").value(owner.fullName);
        out.name("ssn").value(owner.getSsn());
        out.name("age").value(owner.age);
        out.name("netWorth").value(owner.getNetWorth());
        out.name("employees");
        out.beginArray();
        for (Employee employee : owner.getEmployees().values())
            GSON.getAdapter(Employee.class).write(out, employee);
        out.endArray();
        out.name("farm");
        GSON.getAdapter(Farm.class).write(out, owner.farm);
        out.endObject();
    }

    @Override
    public Owner read(JsonReader in) {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> properties = GSON.fromJson(in, type);

        String fullName = (String) properties.get("fullName");
        String ssn = (String) properties.get("ssn");
        int age = ((Double) properties.get("age")).intValue();
        Farm farm = GSON.fromJson(GSON.toJson(properties.get("farm")), Farm.class);
        double netWorth = (Double) properties.get("netWorth");

        List<Map<String, Object>> employeeList = (List<Map<String, Object>>) properties.get("employees");
        Map<String, Employee> employees = deserializeEmployees(employeeList);

        return new Owner(fullName, ssn, age, farm, netWorth, employees);
    }

    private Map<String, Employee> deserializeEmployees(List<Map<String, Object>> employeeList) {
        Map<String, Employee> employeesMap = new LinkedHashMap<>();

        for (Map<String, Object> employeeData : employeeList) {
            Employee employee = GSON.fromJson(GSON.toJson(employeeData), Employee.class);
            employeesMap.put(employee.getSsn(), employee);
        }

        return employeesMap;
    }
}

class FarmAdapter extends TypeAdapter<Farm> {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("dd-MM-YYYY")
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(Animal.class, new AnimalAdapter())
            .registerTypeAdapter(CropSector.class, new CropAdapter())
            .create();

    @Override
    public void write(JsonWriter out, Farm farm) throws IOException {
        out.beginObject();
        out.name("id").value(farm.getId());
        out.name("name").value(farm.name);
        out.name("location").value(farm.location);
        out.name("size").value(farm.size);

        out.name("animals");
        out.beginArray();
        for (Animal animal : farm.animals) {
            GSON.getAdapter(Animal.class).write(out, animal);
        }
        out.endArray();

        out.name("cropSectors");
        out.beginArray();
        for (CropSector cropSector : farm.cropSectors) {
            GSON.getAdapter(CropSector.class).write(out, cropSector);
        }
        out.endArray();

        out.name("stock");
        out.beginArray();
        for (Good good : farm.getStock().toArrayList()) {
            GSON.toJson(good, Good.class, out);
        }
        out.endArray();

        out.name("soldGoods");
        out.beginArray();
        for (Good good : farm.getSoldGoods().toArrayList()) {
            GSON.toJson(good, Good.class, out);
        }
        out.endArray();

        out.endObject();
    }

    @Override
    public Farm read(JsonReader in) throws IOException {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> properties = GSON.fromJson(in, type);

        String name = (String) properties.get("name");
        String location = (String) properties.get("location");
        float size = ((Double) properties.get("size")).floatValue();

        List<Map<String, Object>> animalList = (List<Map<String, Object>>) properties.get("animals");
        List<Animal> animals = new ArrayList<>();
        for (Map<String, Object> animalData : animalList) {
            Animal animal = GSON.fromJson(GSON.toJson(animalData), Animal.class);
            animals.add(animal);
        }

        List<Map<String, Object>> cropSectorList = (List<Map<String, Object>>) properties.get("cropSectors");
        List<CropSector> cropSectors = new ArrayList<>();
        for (Map<String, Object> cropSectorData : cropSectorList) {
            CropSector cropSector = GSON.fromJson(GSON.toJson(cropSectorData), CropSector.class);
            cropSectors.add(cropSector);
        }

        List<Map<String, Object>> stockList = (List<Map<String, Object>>) properties.get("stock");
        MyLinkedList<Good> stock = new MyLinkedList<>();
        for (Map<String, Object> stockData : stockList) {
            Good good = GSON.fromJson(GSON.toJson(stockData), Good.class);
            stock.add(good);
        }

        List<Map<String, Object>> soldGoodsList = (List<Map<String, Object>>) properties.get("soldGoods");
        MyLinkedList<Good> soldGoods = new MyLinkedList<>();
        for (Map<String, Object> soldGoodsData : soldGoodsList) {
            Good good = GSON.fromJson(GSON.toJson(soldGoodsData), Good.class);
            soldGoods.add(good);
        }

        return new Farm(name, location, size, animals, cropSectors, stock, soldGoods);
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

class EmployeeAdapter extends TypeAdapter<Employee> {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Animal.class, new AnimalAdapter())
            .registerTypeAdapter(CropSector.class, new CropAdapter())
            .registerTypeAdapter(Queue.class, new WorkShiftQueueAdapter())
            .registerTypeAdapter(TreeSet.class, new AnimalTreeSetAdapter())
            .create();

    @Override
    public void write(JsonWriter out, Employee employee) throws IOException {
        out.beginObject();
        out.name("fullName").value(employee.fullName);
        out.name("ssn").value(employee.getSsn());
        out.name("age").value(employee.age);
        out.name("annualSalary").value(employee.getAnnualSalary());
        out.name("nextShifts");
        out.beginArray();
        for (WorkShift shift : employee.nextShifts) {
            out.value(shift.name());
        }
        out.endArray();
        out.name("type").value(employee.type.name());
        if (employee instanceof CropsCultivator cultivator) {
            out.name("idCropsInCare").beginArray();
            for (Integer cropId : cultivator.idCropsInCare) {
                out.value(cropId);
            }
            out.endArray();
        } else if (employee instanceof AnimalCaretaker caretaker) {
            out.name("isVetCertified").value(caretaker.isVetCertified);
            out.name("animalsInCare");
            out.beginArray();
            for (Animal animal : caretaker.animalsInCare) {
                out.jsonValue(GSON.toJson(animal));
            }
            out.endArray();
        }
        out.endObject();
    }

    @Override
    public Employee read(JsonReader in) throws IOException {
        Type type = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> properties = GSON.fromJson(in, type);

        String fullName = (String) properties.get("fullName");
        String ssn = (String) properties.get("ssn");
        int age = ((Double) properties.get("age")).intValue();
        double annualSalary = (Double) properties.get("annualSalary");
        String employeeTypeStr = (String) properties.get("type");
        List<String> shifts = (List<String>) properties.get("nextShifts");

        EmployeeType employeeType = EmployeeType.valueOf(employeeTypeStr);
        return switch (employeeType) {
            case CULTIVATOR -> {
                List<Integer> cropsId = (List<Integer>) properties.get("idCropsInCare");
                yield new CropsCultivator(fullName, ssn, age, annualSalary, deserializeWorkShifts(shifts), deserializeCropsInCare(cropsId));
            }
            case ANIMAL_CARETAKER -> {
                List<Object> animals = (List<Object>) properties.get("animalsInCare");
                boolean isVetCertified = (Boolean) properties.get("isVetCertified");
                yield new AnimalCaretaker(fullName, ssn, age, annualSalary, deserializeWorkShifts(shifts), deserializeAnimalsInCare(animals), isVetCertified);
            }
        };
    }

    private Queue<WorkShift> deserializeWorkShifts(List<String> shiftsList) {
        return shiftsList.stream()
                .map(WorkShift::valueOf)
                .collect(Collectors.toCollection(ArrayDeque::new));
    }

    private Set<Integer> deserializeCropsInCare(List<Integer> cropsId) {
        return new HashSet<>(cropsId);
    }

    private TreeSet<Animal> deserializeAnimalsInCare(List<Object> animalsList) {
        return animalsList.stream()
                .map(animal -> GSON.fromJson(GSON.toJson(animal), Animal.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }
}

class AnimalTreeSetAdapter extends TypeAdapter<TreeSet<Animal>> {
    private static final Gson GSON = new Gson();

    @Override
    public void write(JsonWriter out, TreeSet<Animal> animals) throws IOException {
        out.beginArray();
        for (Animal animal : animals) {
            out.jsonValue(GSON.toJson(animal));
        }
        out.endArray();
    }

    @Override
    public TreeSet<Animal> read(JsonReader in) throws IOException {
        TreeSet<Animal> animals = new TreeSet<>();
        in.beginArray();
        while (in.hasNext()) {
            Animal animal = GSON.fromJson(in, Animal.class);
            animals.add(animal);
        }
        in.endArray();
        return animals;
    }
}

class WorkShiftQueueAdapter extends TypeAdapter<Queue<WorkShift>> {

    @Override
    public void write(JsonWriter out, Queue<WorkShift> shifts) throws IOException {
        out.beginArray();
        for (WorkShift shift : shifts) {
            out.value(shift.name());
        }
        out.endArray();
    }

    @Override
    public Queue<WorkShift> read(JsonReader in) throws IOException {
        Queue<WorkShift> shifts = new LinkedList<>();
        in.beginArray();
        while (in.hasNext()) {
            String shiftName = in.nextString();
            WorkShift shift = WorkShift.valueOf(shiftName);
            shifts.add(shift);
        }
        in.endArray();
        return shifts;
    }
}