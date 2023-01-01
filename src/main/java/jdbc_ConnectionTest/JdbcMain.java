package jdbc_ConnectionTest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcMain {

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sundayfunday";

    public static void main(String[] args) throws SQLException {
        //1. Use DriverManager to create Connection to DB
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        //2. Using a connection object we can create Statement object
        Statement statement = connection.createStatement();
        String sqlSelectAllPlayers = "SELECT * FROM players;";
        ResultSet resultAllPlayers = statement.executeQuery(sqlSelectAllPlayers);
        List<Player> players = new ArrayList<>();
        while(resultAllPlayers.next()){
            //Nurodau stulpelio pavadinima ir gauno jo reiksme
            String firstName = resultAllPlayers.getString("first_name");
            String lastName = resultAllPlayers.getString("last_name");
            String team = resultAllPlayers.getString("team");
            int wins = resultAllPlayers.getInt("wins");
            int loses = resultAllPlayers.getInt("loses");
            double height = resultAllPlayers.getDouble("height");
            double ppg = resultAllPlayers.getDouble("ppg");
            int id = resultAllPlayers.getInt("id");
            Player player = Player.builder()
                    .id(id)
                    .firstName(firstName)
                    .lastName(lastName)
                    .team(team)
                    .wins(wins)
                    .loses(loses)
                    .height(height)
                    .ppg(ppg)
                    .build();
            players.add(player);
        }
        for (Player player : players) {
            System.out.println(player);
        }

        System.out.println();

        ResultSet spursPlayers = statement.executeQuery("SELECT * FROM players WHERE team = 'Spurs'");
        while(spursPlayers.next()){
            System.out.println(spursPlayers.getString("first_name"));
        }
        System.out.println();

        //Insert into...

        String insertSql = "INSERT INTO PLAYERS VALUES(21, 'Lebron', 'James', 'Heat', 2.11, 35.5, 56, 12)";
        int affectedRows = statement.executeUpdate(insertSql);
        System.out.println(affectedRows);

    }
}
