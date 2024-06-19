package product;

import java.io.Serializable;

public class Product implements Serializable {

    private int Primary_ID;
    private String prod_name;
    private int prod_price;
    private String prod_id;
    private byte prod_rating;
    private int prod_stock;
    private String seller;
    private ProductCategories category;
    private boolean isVisible;


    public Product(int Primary_ID , String prod_name, int prod_price, String prod_id,
                   byte prod_rating, int prod_stock, String seller,
                   ProductCategories category , boolean isVisible) {

        this.Primary_ID = Primary_ID;
        this.prod_name = prod_name;
        this.prod_price = prod_price;
        this.prod_id = prod_id;
        this.prod_rating = prod_rating;
        this.prod_stock = prod_stock;
        this.seller = seller;
        this.category = category;
        this.isVisible = isVisible;

    }

    public Product(){}

    public int getPrimaryID()
    {
        return Primary_ID;
    }

    public String getProd_name() {
        return prod_name;
    }

    public int getProd_price() {
        return prod_price;
    }

    public String getProd_id() {
        return prod_id;
    }

    public void setProd_price(int prod_price) {
        this.prod_price = prod_price;
    }

    public void setProd_rating(byte prod_rating) {
        this.prod_rating = prod_rating;
    }

    public void setProd_Visibility(boolean isVisible)
    {
        this.isVisible = isVisible;
    }

    public void setProd_stock(int prod_stock) {
        this.prod_stock = prod_stock;
    }

    public byte getProd_rating() {
        return prod_rating;
    }

    public boolean getProd_visiblity()
    {
        return isVisible;
    }

    public int getProd_stock() {
        return prod_stock;
    }

    public String getSeller() {
        return seller;
    }

    public ProductCategories getCategory() {
        return category;
    }

    public String getDetails() {
        return
                "primary_id : " + Primary_ID + '\n' +
                "prod_name : " + prod_name + '\n' +
                "prod_price : " + prod_price + '\n' +
                "prod_id : " + prod_id + '\n' +
                "prod_rating : " + prod_rating + '\n' +
                "prod_stock : " + prod_stock + '\n' +
                "seller : " + seller + '\n' +
                "category : " + category + '\n' ;
                
    }

    public String getDetailsFile() {
        return
                String.valueOf(Primary_ID) + ',' +
                prod_name + ',' +
                prod_price + ','  +
                prod_id + ',' +
                prod_rating + ','  +
                prod_stock + ','  +
                seller + ','  +
                category.toString()+','+ 
                (isVisible ? "true" : "false") + ',' ;
                
    }

}
