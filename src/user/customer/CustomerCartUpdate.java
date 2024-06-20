package user.customer;

import java.util.HashMap;

import product.ProductsLibrary;


public interface CustomerCartUpdate {

    public void addProduct( String prod_ID );
    public void deleteProduct( String prod_ID );
    public void viewCart( ProductsLibrary prod_lib);
    public void viewProducts( ProductsLibrary prod_lib );

}
