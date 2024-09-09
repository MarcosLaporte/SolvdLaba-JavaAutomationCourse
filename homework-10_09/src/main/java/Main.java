import org.apache.logging.log4j.Level;
import services.LoggerService;
import services.MainMenu;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        LoggerService.log(Level.DEBUG, "Start: " + LocalDateTime.now());
        MainMenu.mainMenu();
        LoggerService.log(Level.DEBUG, "Finish: " + LocalDateTime.now() + "\n");
    }
}
