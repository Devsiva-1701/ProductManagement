package Mongo;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import product.Product;
import product.ProductCategories;

public class ClientConnect {

    private MongoDatabase database;

    public ClientConnect() {}

    public void connect() {
        try{
            MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
            System.out.println("Client Connection Successful...");
            setDatabase(mongoClient.getDatabase("ProductManage"));
        } catch (Exception connectException) {
            connectException.printStackTrace();
            System.err.println("Failed to connect to the database...");
        }
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public MongoCollection<Document> getProductCollection()
    {
        return database.getCollection("Product");
    }

    public void insertIntoCollection( Product product )
    {

        try {
            MongoCollection<Document> OrderItemsCollection = database.getCollection("Product");
            
            Document JSONDoc = new Document("ProductID" , product.getProd_id()).
                                            append("ProductName", product.getProd_name()).
                                            append("Category", product.getCategory().toString()).
                                            append("CategoryID" , "C-"+ String.valueOf( product.getCategory().ordinal()+1)).
                                            append("Price", product.getProd_price()).
                                            append("Stock", product.getProd_stock()).
                                            append("SellerID", product.getSellerID()).
                                            append("Rating", product.getProd_rating());
            // try {
            //     OrderItemsCollection.createIndex(new Document("ProductID" , product.getProd_id()) , new IndexOptions().unique(true));  
            // } catch (Exception Duplicate) {
            //     System.err.println("The product with this ID is already presented...");
            // }
                  
            OrderItemsCollection.insertOne(JSONDoc);
            System.out.println("Insertion Successful...");
        } catch (Exception InsertionException) {
            InsertionException.printStackTrace();
            System.err.println("Failed to insert the Data...");
        }

    }

    public void insertCategoriesIntoCollection()
    {

        try {
            MongoCollection<Document> CategoryCollection = database.getCollection("Catagories");
            CategoryCollection.drop();
            Arrays.stream(ProductCategories.values()).forEach( 
                category -> {
                    Document JSONDoc = new Document().
                                    append("CategoryID", "C-"+String.valueOf(category.ordinal()+1)).
                                    append( "Category" , category.toString());
                                    CategoryCollection.insertOne(JSONDoc);
                                }
             );
            System.out.println("Insertion Successful...");
        } catch (Exception InsertionException) {
            InsertionException.printStackTrace();
            System.err.println("Failed to insert the Data...");
        }

    }

    public void getProductLibrary_Mongo()
    {
        try( MongoCursor<Document> cursor = database.getCollection("Product").find().iterator() ) {

            while(cursor.hasNext())
            {
                Document doc = cursor.next();
                System.out.println(doc);
            }
            
        } catch (Exception fetchError) {
            System.err.println("Fetching error occured...");
        }
        
    }
    
}
