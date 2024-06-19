package user.seller;

import java.io.Serializable;
import java.util.HashMap;

import product.ProductsLibrary;
import product.Product;

public abstract class SellerProduct implements Serializable {

    ProductsLibrary prod_lib;
    
    SellerProduct( ProductsLibrary prod_lib )
    {
        this.prod_lib = prod_lib;
    }

    abstract void addProductToStore( Product product );
    abstract void deleteProductFromStore( String prod_ID );

    public void viewProduct( String prod_ID )
    {
        try {

            HashMap<String , Product> prod_Map = prod_lib.getLibrary();
            if(prod_Map.containsKey(prod_ID))
            {
                System.out.println(prod_Map.get(prod_ID).getDetails());
            }
            else
            {
                System.out.println("Product Not found...");
            }
            
        } catch (NullPointerException library_null) {
            System.out.println("No products in the store...");
        }
        
        
    }

    public void viewAllProducts()
    {
        try{
            for( HashMap.Entry<String , Product> product : prod_lib.getLibrary().entrySet() )
            {
                System.out.println( product.getValue().getProd_visiblity() ? "Visible " : "Not Visible ");
                System.out.println(product.getValue().getDetails());
            }
        }
        catch(Exception library_null )
        {
            System.err.println("No Products are in the store...");
        }
        
    }


}
