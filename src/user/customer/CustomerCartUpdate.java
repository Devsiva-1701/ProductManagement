package user.customer;

import java.util.Scanner;

import product.ProductsLibrary;


public interface CustomerCartUpdate {

    public void addProduct( String prod_ID );
    public void deleteProduct( String prod_ID ,Scanner input );
    public void viewCart( ProductsLibrary prod_lib);
    public void viewProducts( ProductsLibrary prod_lib );

}
