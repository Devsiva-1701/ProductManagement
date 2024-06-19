package product;
import java.io.Serializable;
import java.util.HashMap;

public class ProductsLibrary implements Serializable {

    private  HashMap<String , Product> products_library;

    public ProductsLibrary( HashMap<String , Product> products_library )
    {
        this.products_library = products_library;
    }

    public ProductsLibrary(){}

    public HashMap<String , Product> getLibrary()
    {

        return products_library;

    }

    public void addProductToLibrary( Product product )
    {
        products_library.put(product.getProd_id(), product);
    }

    public void deleteProductToLibrary( String prod_ID )
    {
        products_library.remove(prod_ID);
    }

    public String getProductName( String productID )
    {
        return products_library.get(productID).getProd_name();
    }

    public int getPrimaryID( String productID )
    {
        return products_library.get(productID).getPrimaryID();
    }




}
