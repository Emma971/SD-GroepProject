import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    static ArrayList<Map<String, String>> executeStatement(String query, String... args) {
        Connection con = null;
        ArrayList<Map<String, String>> table = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://thuis.noordermeer.tech:3306/pris?serverTimezone=Europe/Amsterdam", "pris", "7bwfn0K32eWsPOI5");

            table = new ArrayList<>();
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery(query);
            try {
                while (result.next()) {
                    Map<String, String> row = new HashMap<>();
                    for (String columnName : args) {
                        row.put(columnName, result.getString(columnName));
                    }
                    table.add(row);
                }
            } catch (SQLException e) {
                System.out.println("Er is een fout opgetreden tijdens het verwerken van de gegevens uit de database:");
                System.out.println(e.getLocalizedMessage());
            }
        } catch (SQLException e) {
            System.out.println("Er is een fout opgetreden tijdens het uitvoeren van de SQL statement:");
            System.out.println(e.getLocalizedMessage());
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("Er is een fout opgetreden tijdens het sluiten van de SQL verbinding.");
                System.out.println(e.getLocalizedMessage());
            }
        }
        return table;
    }
}
