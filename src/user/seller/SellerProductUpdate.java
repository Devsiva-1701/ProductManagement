package user.seller;

import Mongo.ClientConnect;

public interface SellerProductUpdate {
    
    public void updatePrice( String prod_ID , ClientConnect client );
    public void udpateAdditonOfStock( String prod_ID , ClientConnect client );
    public void updateSubtractionOfStock( String prod_ID , ClientConnect client );

}
