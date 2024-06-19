package user.seller;

public interface SellerProductUpdate {
    
    public void updatePrice( String prod_ID );
    public void udpateAdditonOfStock( String prod_ID );
    public void updateSubtractionOfStock( String prod_ID );

}
