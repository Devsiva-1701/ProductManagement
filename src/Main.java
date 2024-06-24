
import java.sql.SQLException;

import Mongo.ClientConnect;
import PostgreSQLClient.PostgreSQLClient;

public class Main {

    private static String URL = "jdbc:postgresql://localhost:5432/purchasedb";
    private static String userName = "postgres";
    private static String password = "1234";
    public static void main(String[] args) {

        ClientConnect clientConnect = new ClientConnect();
        clientConnect.connect();
        PostgreSQLClient postgresClient = new PostgreSQLClient(URL , userName , password);
        try {
            postgresClient.connectClient();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            System.err.println("Failed to connecting to postgres database...");
        }
        

        Console console_1 = new Console( clientConnect , postgresClient );
        console_1.startConsole();
    }
}