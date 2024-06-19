package user.seller;

import java.util.HashMap;

import product.Product;

public interface SellerInterface {
    
    public int getPrimaryID();
    public String getName();
    public String getPass();
    public String getSellerDetails();
    public String getSeller_ID();
    public Long getSeller_PhNo();
    public HashMap<String , Product> getSellerProducts();


}
