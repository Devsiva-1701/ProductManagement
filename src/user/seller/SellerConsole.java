package user.seller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Scanner;
import Mongo.ClientConnect;
import product.Product;
import product.ProductCategories;
import product.ProductsLibrary;
import product.RemovedProductsLibrary;
import user.RemovedUsers;
import user.Users;

public class SellerConsole {
    private RemovedUsers<Seller> removed_seller_DB;
    private Users<Seller> seller_DB;
    private Seller current_seller;
    private boolean seller_active = true;
    private ProductsLibrary prod_lib;
    private RemovedProductsLibrary removed_prod_lib;
    ClientConnect client;
    // private Product product;

    public SellerConsole( ClientConnect client ,
        RemovedUsers<Seller> removed_seller_DB , Users<Seller> seller_DB, RemovedProductsLibrary removed_prod_lib ,
     Seller current_seller , ProductsLibrary prod_lib )
    {
        this.removed_seller_DB = removed_seller_DB;
        this.current_seller = current_seller;
        this.prod_lib = prod_lib;
        this.seller_DB = seller_DB;
        this.removed_prod_lib = removed_prod_lib;
        this.client = client;
    }

    public void start_seller_console( Scanner input )
    {
        while( seller_active )
        {
            int option;
            System.out.println(" Enter the options (number) : \n 1.View your selling products \n 2.View one of your selling product \n 3.Update Price \n 4.Add Stock \n 5.Reduce Stock \n 6.Add Product \n 7.Delete Product \n 8.LogOut \n" + //
                                " 9.Delete Account");
            option = input.nextInt();

            switch(option)
            {
                case 1:
                    current_seller.viewAllProducts( client , current_seller.getSeller_ID() );
                    break;
                
                case 2:
                    String prod_ID_Single; 
                    System.out.println("Enter the product ID : ");
                    prod_ID_Single = input.nextLine();
                    prod_ID_Single = input.nextLine();
                    current_seller.viewProduct(prod_ID_Single , client);
                    break;

                case 3:
                    client.getProductLibrary_Mongo();
                    String prod_ID_Price;
                    System.out.println("Enter the product ID : ");
                    prod_ID_Price = input.nextLine();
                    prod_ID_Price = input.nextLine();
                    current_seller.updatePrice(prod_ID_Price , client);   
                    break;
                    
                case 4:
                    client.getProductLibrary_Mongo();
                    String prod_ID_StockAdd;
                    System.out.println("Enter the product ID : ");
                    prod_ID_StockAdd = input.nextLine();
                    prod_ID_StockAdd = input.nextLine();
                    current_seller.udpateAdditonOfStock(prod_ID_StockAdd , client);
                    break;

                case 5:
                    client.getProductLibrary_Mongo();
                    String prod_ID_StockSub;
                    System.out.println("Enter the product ID : ");
                    prod_ID_StockSub = input.nextLine();
                    prod_ID_StockSub = input.nextLine();
                    current_seller.updateSubtractionOfStock(prod_ID_StockSub , client);
                    break;

                case 6:
                    setProduct(input);
                    break;

                case 7:
                    client.getProductLibrary_Mongo();
                    String prod_ID_delete;
                    System.out.println("Enter the product ID : ");
                    prod_ID_delete = input.nextLine();
                    prod_ID_delete = input.nextLine();
                    current_seller.deleteProductFromStore(prod_ID_delete , client);
                    break;

                case 8:
                    try 
                    {
                        System.err.println("From console : "+prod_lib.getLibrary());
                        System.err.println("From console : "+ removed_prod_lib.getLibrary());
                        BufferedWriter writer = new BufferedWriter( new FileWriter("seller.txt") );

                        for( Map.Entry<String , Seller> seller_entry : seller_DB.getDB().entrySet() )
                        {
                            System.err.println(seller_DB.getDB());
                            Seller sellerFromMap = seller_entry.getValue();
                            System.out.println(sellerFromMap.getSellerDetailsFile());
                            writer.write( sellerFromMap.getSellerDetailsFile());
                            System.out.println("Writing in seller");
                            writer.newLine();
                        }
                        writer.close();
                        BufferedWriter writer_2 = new BufferedWriter(new FileWriter("products.txt"));
                        
                        if(!prod_lib.getLibrary().isEmpty())
                        {
                            for( Map.Entry<String , Product> product_entry : prod_lib.getLibrary().entrySet() )
                            {
                                Product productFromMap = product_entry.getValue();
                                writer_2.write( productFromMap.getDetailsFile());
                                writer_2.newLine();
                            }
                        }
                        
                        writer_2.close();
                        BufferedWriter writer_3 = new BufferedWriter(new FileWriter("removed_products.txt"));
                        
                        if(!removed_prod_lib.getLibrary().isEmpty())
                        {
                            for(Map.Entry<String ,Product> removed_prod_entry : removed_prod_lib.getLibrary().entrySet() )
                            {
                                Product productFromMap = removed_prod_entry.getValue();
                                writer_3.write( productFromMap.getDetailsFile() );
                                System.out.println("Writing in removed prod");
                                writer_3.newLine();
                            }
                        }
                        writer_3.close();
                        System.out.println("File Created...");
                        
                    } catch (Exception e) {
                        System.err.println("File creation failed...");
                        e.printStackTrace();
                    }
                    seller_active = false;
                    break;
 
                case 9:
                    removed_seller_DB.addRemovedUser(current_seller.getSeller_ID());
                    seller_DB.deleteUser(current_seller.getSeller_ID());
                    System.out.println("Your Account has been deleted..");
                    seller_active = false;
                    break;

                default:
                System.out.println("Invalid Input...");
                    break;


            }

            
        }

        // input.close();
        
    }

    // This will create the Product

    public void setProduct( Scanner input )
    {

        String prod_name;
        int Primary_ID_1 = 1;
        // int Primary_ID_2 = 1;
        int prod_price;
        String prod_ID = current_seller.getSeller_ID();
        byte prod_rating = 0;
        int prod_stock;
        ProductCategories category = ProductCategories.Electronics;
        short prod_category_16Bit;
        boolean valid_category =  false;
        ProductCategories[] category_list = ProductCategories.values();

        System.out.println("Enter the product Name : ");
        prod_name = input.nextLine();
        prod_name = input.nextLine();
        System.out.println("Enter the price : ");
        prod_price = input.nextInt();
        System.out.println("Enter the Product stock : ");
        prod_stock = input.nextInt();
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
        prod_rating , prod_stock , current_seller.getName() , current_seller.getSeller_ID() , category , true
        );

        client.insertIntoCollection(product);
        current_seller.addProductToStore(product);
        current_seller.addSellerProducts();

        System.out.println("Product Added to the Store Successfully...");
        System.out.println(prod_lib.getLibrary());
        


    } 

}

