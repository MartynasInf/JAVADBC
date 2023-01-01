package ChristmasPresentsManager.services;

import ChristmasPresentsManager.entity.Present;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PresentManagerServices {
    PrinterMethods printerMethods = new PrinterMethods();
    SQLManager sqlManager = new SQLManager();

    /**
     * Prints out all presents from database
     */
    public void printListOfPresents() {
        System.out.println("\n-------------Dovanu sarasas-------------\n");
        printerMethods.printingListOfPresents(SQLManager.presentsList);
        System.out.println("\n----------------------------------------\n");
    }

    /**
     * Adds a present to a database.
     */
    public void addPresent() throws SQLException {
        boolean presentIsBoughtStatus;
        double price = 0;
        Scanner newPresentInfoInput = new Scanner(System.in);
        System.out.println("Kad prideti dovana, reikia ivesti siuos duomenis: \n");
        System.out.println("Dovana, parduotuve, kaina, adresatas, ar dovana nupirkta? (true/false). Duomenis atskirti per ;");
        String givenPresentInfo = newPresentInfoInput.next();
        String[] presentData = givenPresentInfo.split(";");
        String presentName = presentData[0];
        String shopName = presentData[1];
        String priceStr = presentData[2];
        if (doubleFormatParserChecker(priceStr)) {
            price = Double.parseDouble(priceStr);
        } else {
            return;
        }
        String addressee = presentData[3];
        String isBought = presentData[4];
        if (booleanFormatParserChecker(isBought)) {
            presentIsBoughtStatus = Boolean.parseBoolean(isBought);
        } else {
            return;
        }
        int nextId = findLowestUnusedId();
        if (!presentName.isEmpty() && !shopName.isEmpty() && price != 0 && !addressee.isEmpty()) {
            String addPresentQuery = nextId + ", '"
                    + presentName + "', '"
                    + shopName + "', "
                    + price + ", '"
                    + addressee + "', "
                    + presentIsBoughtStatus;
            sqlManager.addPresentToDB(addPresentQuery);
            System.out.println("\n Dovana prideta i sarasa sekmingai! \n");
        } else {
            System.out.println("Informacija ivesta nepilnai arba netinkamai");
        }
    }

    /**
     * Updates present info by given instructions
     */
    public void updatePresentInfo() {
        basicInfoForUser();
        System.out.println("\n Pasirinkite prekes ID, kurios duomenis noresite keisti: \n");
        int selectedId = getSelectedId();
        boolean isIdValid = idCheckForExistence(selectedId);
        if (isIdValid) {
            updatePresentSubMenu(selectedId);
        } else {
            System.out.println("Pasirinktas id neegzistuoja");
        }
    }

    /**
     * Deletes present by chosen id
     */
    public void deletePresent() throws SQLException {
        basicInfoForUser();
        System.out.println("\n Pasirinkite prekes id, kuria norite istrinti: \n");
        int selectedId = getSelectedId();
        boolean isIdValid = idCheckForExistence(selectedId);
        if (isIdValid) {
            sqlManager.deletePresentFromDB(selectedId);
            System.out.println("\n Pasirinkta dovana istrinta sekmingai \n");
        } else {
            System.out.println("Pasirinktas id neegzistuoja");
        }

    }

    /**
     *
     */
    public void findAllBoughtPresents() {
        printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                .filter(Present::isBought)
                .collect(Collectors.toList()));
    }

    /**
     *
     */
    public void findPresentsByAddressee() {
        Scanner addresseInput = new Scanner(System.in);
        System.out.println("Iveskite adresata, kurio dovanas norite matyti");
        String addressee = addresseInput.next();
        if (addresseeCheckForExistence(addressee)) {
            printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                    .filter(v -> v.getAddressee().equalsIgnoreCase(addressee))
                    .collect(Collectors.toList()));
        } else {
            System.out.println("Dovanu su pasirinktu adresatu sistemoje nera");
        }
    }

    /**
     *
     */
    public void findCheapestPresent() {
        OptionalDouble smallestPresentPrice = SQLManager.presentsList.stream()
                .mapToDouble(Present::getPresentPrice)
                .min();
        printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                .filter(v -> v.getPresentPrice() == smallestPresentPrice.getAsDouble())
                .collect(Collectors.toList()));
    }

    /**
     *
     */
    public void findMostExpensivePresent() {
        OptionalDouble biggestPresentPrice = SQLManager.presentsList.stream()
                .mapToDouble(Present::getPresentPrice)
                .max();
        printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                .filter(v -> v.getPresentPrice() == biggestPresentPrice.getAsDouble())
                .collect(Collectors.toList()));
    }

    /**
     *
     */
    public void findPresentsByShop() {
        Scanner shopInput = new Scanner(System.in);
        System.out.println("Iveskite parduotuve, kurios dovanas norite matyti");
        String shop = shopInput.next();
        if (shopCheckForExistence(shop)) {
            printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                    .filter(v -> v.getShopName().equalsIgnoreCase(shop))
                    .collect(Collectors.toList()));
        } else {
            System.out.println("Dovanu is pasirinktos parduotuves nera");
        }
    }

    public void findPresentsByPriceSubMenu() {
        System.out.println();
        System.out.println("Pasirinktite pagal koki kainos aspekta bus ieskoma dovanu:");
        System.out.println("1. Rasti dovanas brangesnes nei nurodoma kaina");
        System.out.println("2. Rasti dovanas pigesnes nei nurodoma kaina");
        System.out.println("3. Rasti dovanas dvieju nurodytu kainu intervale");

        Scanner subMenuChoice = new Scanner(System.in);

        try {
            switch (subMenuChoice.nextInt()) {
                case 1:
                    findPresentMoreExpensiveThanGivenPrice();
                    return;
                case 2:
                    findPresentsCheaperThanGivenPrice();
                    return;
                case 3:
                    findPresentsBetweenPrices();
                    return;
                default:
                    System.out.println("Ivestas pasirinkimas neegzistuoja");
            }
        } catch (InputMismatchException e) {
            System.out.println("Ivestas netinkamas pasirinkimo formatas");
        }
    }

    /**
     *
     */
    private void findPresentMoreExpensiveThanGivenPrice() {
        System.out.println("Iveskite kaina");
        Scanner priceInput = new Scanner(System.in);
        String givenPrice = priceInput.next();
        if (doubleFormatParserChecker(givenPrice)) {
            printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                    .filter(v -> v.getPresentPrice() > Double.parseDouble(givenPrice))
                    .collect(Collectors.toList()));
        }
    }

    /**
     *
     */
    private void findPresentsCheaperThanGivenPrice() {
        System.out.println("Iveskite kaina");
        Scanner priceInput = new Scanner(System.in);
        String givenPrice = priceInput.next();
        if (doubleFormatParserChecker(givenPrice)) {
            printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                    .filter(v -> v.getPresentPrice() < Double.parseDouble(givenPrice))
                    .collect(Collectors.toList()));
        }
    }


    private void findPresentsBetweenPrices() {
        double priceFrom;
        double priceUntil;
        System.out.println("Iveskite kaina nuo:");
        Scanner priceFromInput = new Scanner(System.in);
        String priceFromStr = priceFromInput.next();
        System.out.println("Iveskite kaina iki:");
        Scanner priceUntilInput = new Scanner(System.in);
        String priceUntilStr = priceUntilInput.next();
        if (doubleFormatParserChecker(priceFromStr) && doubleFormatParserChecker(priceUntilStr)) {
            priceFrom = Double.parseDouble(priceFromStr);
            priceUntil = Double.parseDouble(priceUntilStr);
            printerMethods.printingListOfPresents(SQLManager.presentsList.stream()
                    .filter(v -> v.getPresentPrice() > priceFrom)
                    .filter(v -> v.getPresentPrice() < priceUntil)
                    .collect(Collectors.toList()));
        }
    }

    /**
     * @param selectedId
     */
    private void updatePresentSubMenu(int selectedId) {
        try {
            Scanner optionToChange = new Scanner(System.in);
            Scanner newInfo = new Scanner(System.in);
            System.out.println("Kuria informacija is pasirinktos prekes norime pakeisti:");
            System.out.println("1. Dovana");
            System.out.println("2. Parduotuve");
            System.out.println("3. Kaina");
            System.out.println("4. Adresatas");
            System.out.println("5. Ar dovana nupirkta ?");
            System.out.println("\n Iveskite pasirinkima \n");
            switch (optionToChange.nextInt()) {
                case 1:
                    updatePresentName(selectedId, newInfo);
                    return;
                case 2:
                    updatePresentShopName(selectedId, newInfo);
                    return;
                case 3:
                    updatePresentPrice(selectedId, newInfo);
                    return;
                case 4:
                    updatePresentAddressee(selectedId, newInfo);
                    return;
                case 5:
                    updatePresentIsBoughtStatus(selectedId, newInfo);
                    return;
                default:
                    System.out.println("Pasirinkimas neegzistuoja");
            }
        } catch (InputMismatchException e) {
            System.out.println("\n Ivestas pasirinkimas yra ne skaicius arba ivestas neteisingu formatu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param selectedId
     * @param newInfo
     * @throws SQLException
     */
    private void updatePresentIsBoughtStatus(int selectedId, Scanner newInfo) throws SQLException {
        System.out.println("Ar dovana buvo nupirkta ? (true/false)");
        boolean isBought;
        try {
            isBought = newInfo.nextBoolean();
        } catch (InputMismatchException e) {
            System.out.println("Ivesta neteisinga reiksme");
            return;
        }
        boolean isBoughtCheckResult = isBoughtChecker(selectedId);
        if (isBought != isBoughtCheckResult) {
            String generatedQuery = " isBought=" + isBought + " WHERE id=" + selectedId;
            sqlManager.updatePresentDataToDb(generatedQuery);
            System.out.println("\n Dovanos statusas atnaujinta sekmingai \n");
        } else {
            System.out.println("Ivedete jau esama reiksme, todel pakeitimai neatliekami");
        }
    }

    /**
     * @param selectedId
     * @param newInfo
     * @throws SQLException
     */
    private void updatePresentAddressee(int selectedId, Scanner newInfo) throws SQLException {
        System.out.println("Iveskite nauja adresata");
        String newAddressee = newInfo.next();
        String generatedQuery = " addressee=" + "'" + newAddressee + "' WHERE id=" + selectedId;
        sqlManager.updatePresentDataToDb(generatedQuery);
        System.out.println("\n Adresatas atnaujinta sekmingai \n");
    }

    /**
     * @param selectedId
     * @param newInfo
     * @throws SQLException
     */
    private void updatePresentPrice(int selectedId, Scanner newInfo) throws SQLException {
        System.out.println("Iveskite nauja kaina. Centai vedami po tasko");
        String newPrice = newInfo.next();
        if (doubleFormatParserChecker(newPrice)) {
            double newPriceDouble = Double.parseDouble(newPrice);
            String generatedQuery = " price=" + newPriceDouble + " WHERE id=" + selectedId;
            sqlManager.updatePresentDataToDb(generatedQuery);
            System.out.println("\n Kaina atnaujinta sekmingai \n");
        }
    }

    /**
     * @param selectedId
     * @param newInfo
     * @throws SQLException
     */
    private void updatePresentShopName(int selectedId, Scanner newInfo) throws SQLException {
        System.out.println("Iveskite nauja parduotuve");
        String newShopName = newInfo.next();
        String generatedQuery = " shopName=" + "'" + newShopName + "' WHERE id=" + selectedId;
        sqlManager.updatePresentDataToDb(generatedQuery);
        System.out.println("\n Parduotuve atnaujinta sekmingai \n");
    }

    /**
     * @param selectedId
     * @param newInfo
     * @throws SQLException
     */
    private void updatePresentName(int selectedId, Scanner newInfo) throws SQLException {
        System.out.println("Iveskite nauja dovana");
        String newPresent = newInfo.next();
        String generatedQuery = " present=" + "'" + newPresent + "' WHERE id=" + selectedId;
        sqlManager.updatePresentDataToDb(generatedQuery);
        System.out.println("\n Dovana atnaujinta sekmingai \n");
    }

    /**
     * @param
     * @param
     * @return
     */
    private int getSelectedId() {
        int selectedId = 0;
        Scanner selectId = new Scanner(System.in);
        try {
            selectedId = selectId.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("\n Ivestas neteisingas formatas \n");
        }
        return selectedId;
    }

    /**
     *
     */
    private void basicInfoForUser() {
        Scanner listPrinting = new Scanner(System.in);
        System.out.println("Informacijos apie dovana pakeitimui, reikes nurodyti dovanos id");
        System.out.println("Ar reikia parodyti dovanu sarasa? (yes/no) \n");
        String answerForListPrinting = listPrinting.next();
        if (answerForListPrinting.equalsIgnoreCase("yes")) {
            printListOfPresents();
        } else if (answerForListPrinting.equalsIgnoreCase("no")) {
            System.out.println();
        } else {
            System.out.println("Nepasirinktas nei vienas is nurodytu variantu, todel sarasas nebus spausdinamas");
        }
    }

    /**
     * @param doubleString
     * @return
     */
    private boolean doubleFormatParserChecker(String doubleString) {
        boolean canBeParsed = false;
        try {
            Double.parseDouble(doubleString);
            canBeParsed = true;
        } catch (NumberFormatException s) {
            System.out.println("Ivesta kaina turi buti su tasku");
        }
        return canBeParsed;
    }

    private boolean booleanFormatParserChecker(String givenBooleanString) {
        boolean booleanStatus = false;
        if (givenBooleanString.equalsIgnoreCase("true") || givenBooleanString.equalsIgnoreCase("false")) {
            booleanStatus = true;
        } else {
            System.out.println("Dovanos nupirkimo statusas nurodytas neteisingai");
        }
        return booleanStatus;
    }

    /**
     * @return
     */
    private int findLowestUnusedId() {
        int freeId = 1;
        for (Present present : SQLManager.presentsList) {
            if (present.getId() == freeId) {
                freeId++;
            } else {
                return freeId;
            }
        }
        OptionalInt idFromStream = SQLManager.presentsList.stream().mapToInt(Present::getId).max();
        return idFromStream.getAsInt() + 1;
    }

    /**
     * @param givenID
     * @return
     */
    private boolean idCheckForExistence(int givenID) {
        for (Present present : SQLManager.presentsList) {
            if (present.getId() == givenID) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param givenAddressee
     * @return
     */
    private boolean addresseeCheckForExistence(String givenAddressee) {
        boolean isAddresseeValid = false;
        for (Present present : SQLManager.presentsList) {
            if (present.getAddressee().equalsIgnoreCase(givenAddressee)) {
                isAddresseeValid = true;
                break;
            }
        }
        return isAddresseeValid;
    }

    /**
     * @param givenShop
     * @return
     */
    private boolean shopCheckForExistence(String givenShop) {
        boolean isShopValid = false;
        for (Present present : SQLManager.presentsList) {
            if (present.getShopName().equalsIgnoreCase(givenShop)) {
                isShopValid = true;
                break;
            }
        }
        return isShopValid;
    }

    /**
     * @param givenID
     * @return
     */
    private boolean isBoughtChecker(int givenID) {
        boolean checkResult = false;
        for (Present present : SQLManager.presentsList) {
            if (present.getId() == givenID) {
                checkResult = present.isBought();
            }
        }
        return checkResult;
    }
}
