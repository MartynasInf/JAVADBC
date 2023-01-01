package ChristmasPresentsManager.services;

import ChristmasPresentsManager.entity.Present;
import java.util.List;

public class PrinterMethods {
    public void printingListOfPresents (List<Present> givenListOfPresents){
        for (Present givenListOfPresent : givenListOfPresents) {
            System.out.println(givenListOfPresent);
        }
    }
}
