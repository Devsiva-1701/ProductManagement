package user.seller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

import Mongo.ClientConnect;
import product.ProductsLibrary;

public abstract class SellerProduct implements Serializable {

    ProductsLibrary prod_lib;
    
    SellerProduct( ProductsLibrary prod_lib )
    {
        this.prod_lib = prod_lib;
    }

    // abstract void addProductToStore( Product product );
    abstract void deleteProductFromStore( String prod_ID , ClientConnect client );

    public void viewProduct( String prod_ID , ClientConnect client )
    {
        try {

            Document filter = new Document("ProductID" , prod_ID);
            Document product = client.getProductCollection().find(filter).first();

            System.out.println(product.toJson());

            
        } catch (NullPointerException library_null) {
            System.out.println("No products in the store...");
        }
        
        
    }

    public void viewAllProducts( ClientConnect client , String sellerID )
    {
        try{
            Document filter = new Document("SellerID", sellerID);
            FindIterable<Document> products = client.getProductCollection().find(filter);

            List<Document> productList = new ArrayList<>();

            MongoCursor<Document> iterator = products.iterator();

            while( iterator.hasNext() )
            {
                Document product = iterator.next();
                productList.add(product);
            }

            // products.iterator().forEachRemaining(productList::add);
            productList.forEach(prod -> System.out.println(prod.toJson()));

            iterator.close();


        }
        catch(Exception library_null )
        {
            System.err.println("No Products are in the store...");
        }
        
    }


}
