import com.mongodb.MongoClient;


public class Main {
    public static void main(String[] args) {

        MongoClient client = new MongoClient( "localhost" , 27017 );
        System.out.println("Client Connected Successfully...");



        Console console_1 = new Console();
        console_1.startConsole();
    }
}