import services.InputService;
import services.MainMenu;

public class Main {
    public static void main(String[] args) {
        char datasource = InputService.readCharInValues(
                "1. XML\n2. Database\n3. JSON\nChoose a data source: ",
                "This option does not exist. Try again: ", new char[]{'1', '2', '3'}
        );

        switch (datasource) {
            case '1' -> MainMenu.runXml();
            case '2' -> MainMenu.runDatabase();
            case '3' -> MainMenu.runJson();
        }
    }
}
