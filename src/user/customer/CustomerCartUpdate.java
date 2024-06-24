package user.customer;

import java.util.Scanner;

import Mongo.ClientConnect;
import product.ProductsLibrary;


public interface CustomerCartUpdate {

    public void addProduct( String prod_ID , ClientConnect mongoClient);
    public void deleteProduct( String prod_ID ,Scanner input );
    public void viewCart( );
    public void viewProducts( ProductsLibrary prod_lib );

}
