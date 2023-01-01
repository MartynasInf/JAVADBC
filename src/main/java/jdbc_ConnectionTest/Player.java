package jdbc_ConnectionTest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Player {
    private int id;
    private String firstName;
    private String lastName;
    private String team;
    private double ppg;
    private double height;
    private int wins;
    private int loses;
}
