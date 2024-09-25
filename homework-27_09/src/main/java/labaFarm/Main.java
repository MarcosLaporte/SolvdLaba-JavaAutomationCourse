package labaFarm;

import labaFarm.farm.Farm;
import labaFarm.farm.people.Owner;
import labaFarm.services.JsonService;
import labaFarm.services.MainMenu;

public class Main {
    public static void main(String[] args) {
        final Owner owner = JsonService.readData("owner.json", Owner.class);
        if (owner == null)
            throw new RuntimeException("Object Owner is null");

        final Farm farm = owner.farm;

        MainMenu.mainMenu(owner, farm);
        System.out.println("\n\n" + farm + "\n\n");

        JsonService.saveData("owner.json", owner);
    }
}