import farm.*;
import farm.people.*;
import services.MainMenu;
import services.MenuService;

public class Main {
    public static void main(String[] args) {
        final Farm farm = MenuService.handleFarm();
        final Owner owner = MenuService.handleOwner(farm);
        MenuService.handleAnimalsAndCrops(farm);

        MainMenu.mainMenu(owner, farm);

        System.out.println("\n\n" + farm);
    }
}