package ChristmasPresentsManager.services;

import ChristmasPresentsManager.entity.Present;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class SQLManager {

    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/presentshomework";
    public static List<Present> presentsList = new ArrayList<>();

    private void executeUpdate(String givenQuery) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement statement = connection.createStatement();
        statement.executeUpdate(givenQuery);
        connection.close();
    }

    private ResultSet executeQuery(String givenQuery) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(givenQuery);
        connection.isClosed();
        return resultSet;

    }

    public void sqlDataReader () throws SQLException {
        presentsList.clear();
        String selectFromTable = "SELECT * FROM presents ORDER BY id";
        ResultSet listOfPresents = executeQuery(selectFromTable);
        while (listOfPresents.next()){
            int id = listOfPresents.getInt("id");
            String presentName = listOfPresents.getString("present");
            String shopName = listOfPresents.getString("shopName");
            double presentPrice = listOfPresents.getDouble("price");
            String addressee = listOfPresents.getString("addressee");
            boolean isBought = listOfPresents.getBoolean("isBought");
            Present present = Present.builder()
                    .id(id)
                    .presentName(presentName)
                    .shopName(shopName)
                    .presentPrice(presentPrice)
                    .addressee(addressee)
                    .isBought(isBought)
                    .build();
            presentsList.add(present);
        }
    }
    public void addPresentToDB (String givenStringForQuery) throws SQLException {
        String insertQuery = "INSERT INTO presents VALUES(" + givenStringForQuery + ")";
        executeUpdate(insertQuery);
        sqlDataReader();
    }
    public void updatePresentDataToDb (String givenStringForQuery) throws SQLException {
        String updateQuery = "UPDATE presents SET" + givenStringForQuery;
        executeUpdate(updateQuery);
        sqlDataReader();
    }

    public void deletePresentFromDB (int givenID) throws SQLException {
        String deleteQuery = "DELETE FROM presents WHERE id=" + givenID;
        executeUpdate(deleteQuery);
        sqlDataReader();
    }

}
