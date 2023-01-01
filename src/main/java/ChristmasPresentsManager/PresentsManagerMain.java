package ChristmasPresentsManager;

import ChristmasPresentsManager.services.MainMenuManager;
import ChristmasPresentsManager.services.SQLManager;

import java.sql.SQLException;

public class PresentsManagerMain {
    public static void main(String[] args) throws SQLException {
        new SQLManager().sqlDataReader();
        new MainMenuManager().mainMenu();
    }
}
