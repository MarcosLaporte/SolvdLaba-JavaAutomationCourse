package labaFarm;

import labaFarm.farm.Farm;
import labaFarm.farm.people.Owner;
import labaFarm.services.MainMenu;
import labaFarm.services.MenuService;

public class Main {
    public static void main(String[] args) {
        final Farm farm = MenuService.handleFarm();
        final Owner owner = MenuService.handleOwner(farm);
        MenuService.handleAnimalsAndCrops(farm);

        MainMenu.mainMenu(owner, farm);
        System.out.println("\n\n" + farm + "\n\n");

        MainMenu.printLogs();
    }
}