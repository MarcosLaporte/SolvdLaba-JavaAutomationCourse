import services.InputService;
import services.MainMenu;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        int datasource = InputService.selectIndexFromList(List.of("XML", "Database", "JSON"), false);

        switch (datasource) {
            case 0 -> MainMenu.runXml();
            case 1 -> MainMenu.runDatabase();
            case 2 -> MainMenu.runJson();
        }
    }
}
