package Utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    public static ArrayList<Map<String, Object>> executeStatement(String query) {
        Connection con = null;
        ArrayList<Map<String, Object>> table = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://thuis.noordermeer.tech:3306/pris?serverTimezone=Europe/Amsterdam", "pris", "FB13IROvBlR84WXv");

            table = new ArrayList<>();
            Statement statement = con.createStatement();
            if (statement.execute(query)) {
                try {
                    ResultSet result = statement.getResultSet();
                    ResultSetMetaData resultData = result.getMetaData();
                    while (result.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= resultData.getColumnCount(); i++) {
                            try {
                                switch (resultData.getColumnType(i)) {
                                    case 4:
                                        row.put(resultData.getColumnName(i), result.getInt(resultData.getColumnName(i)));
                                        break;
                                    case 12:
                                    case 1:
                                    case -1:
                                        row.put(resultData.getColumnName(i), result.getString(resultData.getColumnName(i)));
                                        break;
                                    default:
                                        throw new IllegalArgumentException("\u001B[31m" + "The data type of the column '" + resultData.getColumnName(i) + "' is not (yet) supported.\u001B[0m");
                                }
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getLocalizedMessage());
                            }
                            i++;
                        }
                        table.add(row);
                    }
                } catch (SQLException e) {
                    System.out.println("\u001B[31m" + "An unknown error occured while processing the data from the database:");
                    System.out.println(e.getLocalizedMessage());
                    System.out.println("\u001B[0m");
                }
            }
        } catch (SQLException e) {
            System.out.println("\u001B[31m" + "An unknown error occured while processing the SQL statement:");
            System.out.println(e.getLocalizedMessage());
            System.out.println("\u001B[0m");
        } finally {
            try {
                if (con != null && !con.isClosed()) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println("\u001B[31m" + "An unknown error occured while closing the SQL connection.");
                System.out.println(e.getLocalizedMessage());
                System.out.println("\u001B[0m");
            }
        }
        return table;
    }
}
