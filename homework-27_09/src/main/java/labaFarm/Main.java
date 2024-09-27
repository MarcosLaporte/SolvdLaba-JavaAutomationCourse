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


        MainMenu mainMenu = new MainMenu(owner, farm);
        Thread menuThread = new Thread(mainMenu, "Menu-Thread");

        try {
            menuThread.start();
            menuThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted while waiting for menuThread to finish.");
        }

        System.out.println("\n\n" + farm + "\n\n");

        JsonService.saveData("owner.json", owner);
    }
}