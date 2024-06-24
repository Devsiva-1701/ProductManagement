package user.seller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.result.DeleteResult;

import Mongo.ClientConnect;
import product.*;

public class Seller extends SellerProduct implements SellerInterface , SellerProductUpdate {

    private String seller_name;
    private String seller_ID;
    private int primary_ID;
    private String seller_password;
    private Long seller_PhNo;
    private ProductsLibrary prod_lib;
    private Product product;
    protected HashMap<String , Product> sellerProducts;
    private HashMap<String , Product> removedSellerProducts;
    Scanner input;
    

    public Seller( ProductsLibrary prod_lib , RemovedProductsLibrary removed_prod_lib , String seller_name , HashMap<String , Product> sellerProducts ,
                    String seller_ID , int primary_ID , String seller_password , Long seller_PhNo , HashMap<String , Product> removedSellerProducts , Scanner input  )
    {
        super(prod_lib);
        this.seller_name = seller_name;
        this.seller_ID = seller_ID;
        this.seller_password = seller_password;
        this.seller_PhNo = seller_PhNo;
        this.prod_lib = prod_lib;
        this.sellerProducts = sellerProducts;
        this.primary_ID = primary_ID;
        this.removedSellerProducts = removedSellerProducts;
        this.input = input;
    }

    @Override
    public void updatePrice( String prod_ID , ClientConnect client ) {

        try {

            Document docs  = new Document("ProductID" ,prod_ID);

            Document producDocument = client.getProductCollection().find().filter(docs).first();

            boolean validPrice = false;
            
            int productprice; 
            productprice = producDocument.getInteger("Price");
            System.out.println("The current price of the product : "+producDocument.getString("ProductName")+" is "+productprice);
            while(!validPrice)
            {
                System.out.println("Enter the new price of the product : ");
                productprice = input.nextInt();
                if( productprice < 0 )
                {
                    System.out.println("Enter a valid price...");
                }
                else{
                    Document updateProdDoc = new  Document("$set" , new Document("Price" , productprice));
                    client.getProductCollection().updateOne(producDocument, updateProdDoc);
                    System.out.println("Price for the Product : "+producDocument.getString("ProductName")+" updated as "+ 
                                        productprice +" successfuly...");
                    validPrice = true;
                }
                
            }
            
            
        
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }

        

    }

    @Override
    public void udpateAdditonOfStock( String prod_ID , ClientConnect client ) {

        try {

            Document docs  = new Document("ProductID" ,prod_ID);

            Document producDocument = client.getProductCollection().find().filter(docs).first();
            boolean validValue = false;

            
                int stockValue;
                stockValue = producDocument.getInteger("Stock");
                System.out.println("The Current stock value of the product : "+producDocument.getString("ProductName")+" is "+stockValue);
                while( !validValue )
                {
                    int stockValueNew;
                    System.out.println("Enter the stock value to be added : ");
                    stockValueNew = input.nextInt();
                    if( stockValueNew < 0 )
                    {
                        System.out.println( "You can't add a negative value for stock addition try positive values..." );
                    }
                    // else if( stockValue+stockValueNew > 100 )
                    // {
                    //     System.out.println("Our Platform doesn't allow same producted to be stocked more that 100 please refer terms and conditions...");
                    // }
                    else
                    {
                        Document updateProdDoc = new  Document("$set" , new Document("Stock" , stockValue+stockValueNew));
                        client.getProductCollection().updateOne(producDocument, updateProdDoc);
                        // product.setProd_Visibility(true);
                        System.out.println("Stock for the Product : "+producDocument.getString("ProductName")+" updated as "+ 
                                        String.valueOf(stockValue+stockValueNew) +" successfuly...");
                        validValue = true;
                    }
                }

                // input.close();
                

              
                
            } catch (NullPointerException lib_null) {
                System.err.println("No Products avail in the store...");
            }
          
        
    }

    @Override
    public void updateSubtractionOfStock( String prod_ID , ClientConnect client ) {

        try {

            Document docs  = new Document("ProductID" ,prod_ID);

            Document producDocument = client.getProductCollection().find().filter(docs).first();
            boolean validValue = false;

            int stockValue;
            stockValue = producDocument.getInteger("Stock");
            System.out.println("The Current stock value of the product : "+producDocument.getString("ProductName")+" is "+stockValue);
            while( !validValue )
            {
                int stockValueNew;
                System.out.println("Enter the stock value to be reduced : ");
                stockValueNew = input.nextInt();
                if( stockValueNew > 0 )
                {
                    System.out.println( "You can't add a positive value for stock Subtraction try negative values..." );
                }
                else if( stockValue < stockValueNew )
                {
                    System.out.println("Negative Stocks or not possible...");
                }
                else if( stockValueNew == 0 )
                {
                    System.out.println("Bro you Droped your brain...");
                }
                else
                {

                    Document updateProdDoc = new  Document("$set" , new Document("Stock" , stockValue+stockValueNew));
                    client.getProductCollection().updateOne(producDocument, updateProdDoc);
                    // if( product.getProd_stock() == 0 ){
                    //     product.setProd_Visibility(false);
                    //     System.out.println("This product visiblity is turned off until the stocks have been restored...");
                    // }
                    System.out.println("Stock for the Product : "+producDocument.getString("ProductName")+" updated as "+ 
                                        String.valueOf(stockValue+stockValueNew) +" successfuly...");
                    validValue = true;
                }
            }

            // input.close();
            

        
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }

        
    }

    @Override
    public String getName() {
        return seller_name;
    }

    @Override
    public int getPrimaryID() {
       return primary_ID;
    }

    @Override
    public String getPass() {
        return seller_password;
    }

    @Override
    public String getSellerDetails() {
        return ("ID : "+ getSeller_ID()+"\n"+"Name : "+ getName()+"\n"+"Password : "+getPass()+"\n"+
        "Name : "+ getSeller_PhNo()+"\n"+"Seller Products : "+getSellerProducts());
    }
    public String getSellerDetailsFile() {
        return (getPrimaryID()+","+getSeller_ID()+","+getName()+","+getPass()+","+
         getSeller_PhNo()+","+"SellerProducts,"+getSellerProductsFile()+","+"RemovedSellerProducts,"+getRemovedSellerProductsFile());
    }

    @Override
    public String getSeller_ID() {
        return seller_ID;
    }

    @Override
    public Long getSeller_PhNo() {
        return seller_PhNo;
    }

    @Override
    public HashMap<String, Product> getSellerProducts() {
        return this.sellerProducts;
    }

    public String getSellerProductsFile()
    {
        String result = "";
        int count = 0;
        if( getSellerDetails().isEmpty() )
        {
            return result;
        }
        else
        {
            for( Map.Entry<String , Product> map_entry : getSellerProducts().entrySet() )
            {
                if(count < getSellerProducts().size()-1)
                {
                    result += map_entry.getKey()+",";
                }
                else
                {
                    result += map_entry.getKey();
                }
                count++;
                
            }

            return result;
        }
        
    }

    public String getRemovedSellerProductsFile()
    {
        String result = "";
        if( getRemovedSellerProd().isEmpty() )
        {
            return result;
        }
        else
        {
            int count = 0;
            for( Map.Entry<String , Product> map_entry : getRemovedSellerProd().entrySet() )
            {
                if(count < getRemovedSellerProd().size()-1)
                {
                    result += map_entry.getKey()+",";
                }
            else
                {
                    result += map_entry.getKey();
                }
                count++;
            }

            return result;
            }
        
    }

    public void addSellerProducts()
    {
        sellerProducts.put(product.getProd_id(), product);
    }

    public void deleteSellerProducts( String prod_ID )
    {
        sellerProducts.remove(prod_ID);
    }

    public HashMap<String , Product> getRemovedSellerProd()
    {
        return this.removedSellerProducts;
    }

    public void addRemovedSellerProducts()
    {
        removedSellerProducts.put(product.getProd_id(), product);
    }

    public void deleteRemovedSellerProducts( String prod_ID )
    {
        removedSellerProducts.remove(prod_ID);
    }



    // @Override
    // void addProductToStore( Product product ) {
    //     this.product = product;
    //     try {

    //         HashMap<String , Product> prod_Map =  prod_lib.getLibrary();
    //         if( !prod_Map.containsKey(product.getProd_id()) )
    //         {
    //             prod_lib.addProductToLibrary(product);
    //             sellerProducts.put(product.getProd_id(), product);
    //         }
    //         else
    //         {
    //             System.out.println("Product already exists...");
    //         }
            
    //     } catch (NullPointerException lib_null) {
    //         System.err.println("No Products avail in the store...");
    //     }
        
    // }

    @Override
    void deleteProductFromStore(String prod_ID , ClientConnect client) {

        try {

            Document filter  = new Document("ProductID" ,prod_ID);
            DeleteResult result = client.getProductCollection().deleteOne(filter);

            System.out.println(result);
            System.out.println("Product deleted successfully...");

            // sellerProducts.remove(prod_ID);
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }
    
        
    }

    public void setProduct( Scanner input , ClientConnect client )
    {

        String prod_name;
        int Primary_ID_1 = 1;
        // int Primary_ID_2 = 1;
        Long prod_price;
        String prod_ID = getSeller_ID();
        byte prod_rating = 0;
        Long prod_stock;
        ProductCategories category = ProductCategories.Electronics;
        short prod_category_16Bit;
        boolean valid_category =  false;
        ProductCategories[] category_list = ProductCategories.values();

        System.out.println("Enter the product Name : ");
        prod_name = input.nextLine();
        prod_name = input.nextLine();
        System.out.println("Enter the price : ");
        prod_price = input.nextLong();
        System.out.println("Enter the Product stock : ");
        prod_stock = input.nextLong();
        while(!valid_category)
        {
            System.out.println("Enter the category of this product (Enter the number) : ");
            int productCount = 1;
            for( ProductCategories prod_category : category_list )
            {
                System.out.println( String.valueOf(productCount)+". "+prod_category );
                productCount++;
            }
            prod_category_16Bit = input.nextShort();
            if(!(prod_category_16Bit > ProductCategories.values().length))
            {
                category = category_list[prod_category_16Bit-1];
                valid_category = true;
            }
            else{
                System.out.println("Enter the valid category...");
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm-ss-SS");

         LocalTime currentTime = LocalTime.now();

        Product product = new Product( Primary_ID_1 , prod_name , 
        prod_price ,(prod_ID+"P"+currentTime.format(formatter)),
        prod_rating , prod_stock , getName() , getSeller_ID() , category , true
        );

        this.product = product;

        client.insertIntoCollection(product);
        addSellerProducts();

        System.out.println("Product Added to the Store Successfully...");
        System.out.println(prod_lib.getLibrary());
        


    } 
}
