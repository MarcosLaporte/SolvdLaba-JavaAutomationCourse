import services.InputService;
import services.MainMenu;

public class Main {
    public static void main(String[] args) {
        char datasource = InputService.readCharInValues(
                "1. XML\n2. Database\nChoose a data source: ",
                "This option does not exist. Try again: ", new char[]{'1', '2'}
        );
        if (datasource == '1')
            MainMenu.runXml();
        else
            MainMenu.runDatabase();
    }
}
