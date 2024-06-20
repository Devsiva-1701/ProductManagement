
import Mongo.ClientConnect;

public class Main {
    public static void main(String[] args) {

        ClientConnect clientConnect = new ClientConnect();
        clientConnect.connect();

        Console console_1 = new Console( clientConnect );
        console_1.startConsole();
    }
}