package labaFarm;

import labaFarm.farm.Farm;
import labaFarm.farm.people.Owner;
import labaFarm.services.JsonService;
import labaFarm.services.MainMenu;
import labaFarm.services.MenuService;

public class Main {
    public static void main(String[] args) {
//        final Farm farm = JsonService.readData("farm.json", Farm.class);
//        final Owner owner = JsonService.readData("owner.json", Owner.class);
        final Farm farm = MenuService.handleFarm();
        final Owner owner = MenuService.handleOwner(farm);
//        MenuService.handleAnimalsAndCrops(farm);

        MainMenu.mainMenu(owner, farm);
        System.out.println("\n\n" + farm + "\n\n");

        JsonService.saveData("farm.json", farm);
        JsonService.saveData("owner.json", owner);
        MainMenu.printLogs();
    }
}