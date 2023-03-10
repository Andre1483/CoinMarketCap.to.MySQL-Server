package coin.market.cap.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Andre1483
 * @version 10032023.2248
 */
public class SQLConnection {

    private static final String _host = "***.***.***.*";    // paste host-IP-address here
    private static final String _port = "3306";             // paste port-number here
    private static final String _database = "*********";    // paste database-name here
    private static final String _username = "*******";      // paste mysql-username here
    private static final String _password = "*****";        // paste password of mysql-username here

    private static Connection _con;

    public static void main( String[] args ) throws ClassNotFoundException {
        System.out.println("EXECUTION");

        CoinMarketCapApi.main(args);

        SQLConnection.update("SELECT database();");
        SQLConnection.update("USE CoinMarketCap;");
        SQLConnection.update("INSERT INTO Request_data (json_data) VALUES ('" + CoinMarketCapApi.JSON_String + "');");
        SQLConnection.update("INSERT INTO Bitcoin (JSONdata) SELECT json_data->'$.data[0]' FROM Request_data ORDER BY idRequest_data DESC LIMIT 0, 1;");
        SQLConnection.update("INSERT INTO Ethereum (JSONdata) SELECT json_data->'$.data[1]' FROM Request_data ORDER BY idRequest_data DESC LIMIT 0, 1;");

        System.out.println("json got called and inserted in mysql database");
    }

    public static boolean isConnected() {
        return (_con == null ? false : true);
    }

    public static void connect() throws ClassNotFoundException {
        if (!isConnected()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");

                _con = DriverManager.getConnection("jdbc:mysql://" + _host + ":" + _port + "/" + _database, _username, _password);
                System.out.println("[MYSQL] connected!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect() {
        if (isConnected()) {
            try {
                _con.close();
                System.out.println("[MYSQL] Connection closed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        }
    }

    public static void update(String qry) {
        try {
            PreparedStatement ps = _con.prepareStatement(qry);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
