package product;

import java.util.HashMap;

public class RemovedProductsLibrary {

    private  HashMap<String , Product> products_library;

    public RemovedProductsLibrary( HashMap<String , Product> products_library )
    {
        this.products_library = products_library;
    }

    public RemovedProductsLibrary(){}

    public HashMap<String , Product> getLibrary()
    {

        return products_library;

    }

    public void addProductToLibrary( Product product )
    {
        products_library.put(product.getProd_id(), product);
    }

    public void deleteProductToLibrary( Product product )
    {
        products_library.remove(product.getProd_id(), product);
    }

    public int getProductPrimaryID( String productID )
    {
        return products_library.get(productID).getPrimaryID();
    }
    
}
