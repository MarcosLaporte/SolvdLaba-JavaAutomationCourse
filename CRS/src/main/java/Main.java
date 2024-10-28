import services.MainMenu;

public class Main {
    public static void main(String[] args) {
        MainMenu.Datasource datasource = MainMenu.Datasource.readDatasource();
        MainMenu.start(datasource);
    }
}
