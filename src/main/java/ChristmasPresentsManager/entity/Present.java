package ChristmasPresentsManager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Present {

    private int id;
    private String presentName;
    private String shopName;
    private double presentPrice;
    private String addressee;
    private boolean isBought;


}

