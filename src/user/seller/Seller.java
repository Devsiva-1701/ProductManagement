package user.seller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import product.*;

public class Seller extends SellerProduct implements SellerInterface , SellerProductUpdate {

    private String seller_name;
    private String seller_ID;
    private int primary_ID;
    private String seller_password;
    private Long seller_PhNo;
    private ProductsLibrary prod_lib;
    private RemovedProductsLibrary removed_prod_lib;
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
        this.removed_prod_lib = removed_prod_lib;
        this.removedSellerProducts = removedSellerProducts;
        this.input = input;
    }

    @Override
    public void updatePrice( String prod_ID ) {

        try {

            HashMap<String , Product> ProductlibraryMap = prod_lib.getLibrary();
        
        if( ProductlibraryMap.containsKey(prod_ID) )
        {
            boolean validPrice = false;
            
            int productprice;
            product = ProductlibraryMap.get(prod_ID);
            productprice = product.getProd_price();
            System.out.println("The current price of the product : "+product.getProd_name()+" is "+productprice);
            while(!validPrice)
            {
                System.out.println("Enter the new price of the product : ");
                productprice = input.nextInt();
                if( productprice < 0 )
                {
                    System.out.println("Enter a valid price...");
                }
                else{
                    product.setProd_price(productprice);
                    prod_lib.addProductToLibrary(product);
                    System.out.println("Price for the Product : "+product.getProd_name()+" updated as "+ 
                                        product.getProd_price() +" successfuly...");
                    validPrice = true;
                }
                
            }
            
            
        }
        else
        {
            System.out.println("Product is not available...");
        }
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }

        

    }

    @Override
    public void udpateAdditonOfStock( String prod_ID ) {

        try {

            HashMap<String , Product> ProductlibraryMap = prod_lib.getLibrary();
        boolean validValue = false;

        if(ProductlibraryMap.containsKey(prod_ID))
        {
            int stockValue;
            product = ProductlibraryMap.get(prod_ID);
            stockValue = product.getProd_stock();
            System.out.println("The Current stock value of the product : "+product.getProd_name()+" is "+stockValue);
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
                    product.setProd_stock(stockValue+stockValueNew);
                    product.setProd_Visibility(true);
                    prod_lib.addProductToLibrary(product);
                    addSellerProducts();
                    System.out.println("Stock for the Product : "+product.getProd_name()+" updated as "+ 
                                        product.getProd_stock() +" successfuly...");
                    validValue = true;
                }
            }

            // input.close();
            

        }
        else
        {
            System.out.println("The product is not available...");
        }   
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }
          
        
    }

    @Override
    public void updateSubtractionOfStock( String prod_ID ) {

        try {

            HashMap<String , Product> ProductlibraryMap = prod_lib.getLibrary();
        boolean validValue = false;

        if(ProductlibraryMap.containsKey(prod_ID))
        {
            int stockValue;
            product = ProductlibraryMap.get(prod_ID);
            stockValue = product.getProd_stock();
            System.out.println("The Current stock value of the product : "+product.getProd_name()+" is "+stockValue);
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
                    product.setProd_stock(stockValue + stockValueNew);
                    if( product.getProd_stock() == 0 ){
                        product.setProd_Visibility(false);
                        System.out.println("This product visiblity is turned off until the stocks have been restored...");
                    }
                    prod_lib.addProductToLibrary(product);
                    addSellerProducts();
                    System.out.println("Stock for the Product : "+product.getProd_name()+" updated as "+ 
                                        product.getProd_stock() +" successfuly...");
                    validValue = true;
                }
            }

            // input.close();
            

        }
        else
        {
            System.out.println("The product is not available...");
        }
            
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



    @Override
    void addProductToStore( Product product ) {
        this.product = product;
        try {

            HashMap<String , Product> prod_Map =  prod_lib.getLibrary();
            if( !prod_Map.containsKey(product.getProd_id()) )
            {
                prod_lib.addProductToLibrary(product);
                sellerProducts.put(product.getProd_id(), product);
            }
            else
            {
                System.out.println("Product already exists...");
            }
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }
        
    }

    @Override
    void deleteProductFromStore(String prod_ID) {

        try {

            HashMap<String ,Product> temp_lib = prod_lib.getLibrary();
            removedSellerProducts.put(prod_ID , temp_lib.get(prod_ID) );
            sellerProducts.remove(prod_ID);
            removed_prod_lib.addProductToLibrary(temp_lib.get(prod_ID));
            prod_lib.deleteProductToLibrary(prod_ID);

            System.err.println("From seller : "+prod_lib.getLibrary());
            System.err.println("From seller : "+ removed_prod_lib.getLibrary());
            
        } catch (NullPointerException lib_null) {
            System.err.println("No Products avail in the store...");
        }
    
        
    }
}
