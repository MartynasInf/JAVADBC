package ChristmasPresentsManager.services;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainMenuManager {
    PresentManagerServices presentManagerServices = new PresentManagerServices();

    public void mainMenu() throws SQLException {
        System.out.println();
        System.out.println("1. Pamatyti dovanu sarasa");
        System.out.println("2. Prideti dovana");
        System.out.println("3. Atnaujinti dovanos informacija");
        System.out.println("4. Istrinti dovana");
        System.out.println("5. Rodyti visas jau nupirktas dovanas");
        System.out.println("6. Dovanu sarasas pagal nurodyta adresata");
        System.out.println("7. Rodyti maziausiai kainuojancia dovana");
        System.out.println("8. Rodyti brangiausiai kainuojancia dovana");
        System.out.println("9. Rodyti dovanas pagal pasirinkta parduotuve");
        System.out.println("10. Rodyti dovanas pagal nurodyta kainos rezi");
        System.out.println("11. Iseiti is programos" + '\n');
        System.out.println("Iveskite norima opcija is menu");

        Scanner scanner = new Scanner(System.in);

        try {
            switch (scanner.nextInt()) {
                case 1:
                    presentManagerServices.printListOfPresents();
                    mainMenu();
                case 2:
                    presentManagerServices.addPresent();
                    mainMenu();
                case 3:
                    presentManagerServices.updatePresentInfo();
                    mainMenu();
                case 4:
                    presentManagerServices.deletePresent();
                    mainMenu();
                case 5:
                    presentManagerServices.findAllBoughtPresents();
                    mainMenu();
                case 6:
                    presentManagerServices.findPresentsByAddressee();
                    mainMenu();
                case 7:
                    presentManagerServices.findCheapestPresent();
                    mainMenu();
                case 8:
                    presentManagerServices.findMostExpensivePresent();
                    mainMenu();
                case 9:
                    presentManagerServices.findPresentsByShop();
                    mainMenu();
                case 10:
                    presentManagerServices.findPresentsByPriceSubMenu();
                    mainMenu();
                case 11:
                    System.exit(0);
                default:
                    System.out.println("\n Ivestas pasirinkimas neegzistuoja \n");
                    mainMenu();
            }
        } catch (InputMismatchException e) {
            System.out.println("\n Ivestas menu pasirinkimas neegzistuoja \n");
            mainMenu();
        }
    }

}
