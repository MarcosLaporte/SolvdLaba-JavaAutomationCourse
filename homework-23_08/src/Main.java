import farm.*;
import farm.people.*;

public class Main {
    public static void main(String[] args) {
        Farm manorFarm = new Farm("Manor Farm", "Willingdon, England", 152.3F);
        Owner myOwner = new Owner("Mr. Jones", "243-12-5768", 74, manorFarm, 750_452.6);

        System.out.printf("\nWelcome to %s! My name is %s and I own this place. Located in %s with %.2f acres of land.\n", manorFarm.name, myOwner.fullName, manorFarm.location, manorFarm.size);

        boolean loadData = Input.readCharInValues(
                "Load data? Y/N: ",
                "This option does not exist. Try again: ",
                new char[]{'Y', 'N'}
        ) == 'Y';
        if (loadData) MenuService.loadAnimalsAndCrops(manorFarm);

        MenuService.mainMenu(manorFarm);

        System.out.println("\n\n" + manorFarm);
    }

}