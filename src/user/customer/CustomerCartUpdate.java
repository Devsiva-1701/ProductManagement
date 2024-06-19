package user.customer;

import product.ProductsLibrary;


public interface CustomerCartUpdate {

    public void addProduct( String prod_ID );
    public void deleteProduct( String prod_ID );
    public void viewCart( ProductsLibrary prod_lib );
    public void purchaseProduct();
    public void viewProducts( ProductsLibrary prod_lib );

}
