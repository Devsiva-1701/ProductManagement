package user.customer;


import product.Product;
import product.ProductsLibrary;
import user.seller.Seller;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.bson.Document;

import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;

import Mongo.ClientConnect;
import PostgreSQLClient.PostgreSQLClient;

public class Customer implements CustomerInterface , CustomerCartUpdate{

    private String customer_name;
    private int primary_ID;
    private String customer_ID;
    private String customer_password;
    private String customer_address;
    private String customer_PhNo;
    protected HashMap< String , Integer > customer_cart;
    private HashMap< String , HashMap<String , Integer>> purchase_history = new HashMap<String , HashMap<String ,Integer>>();
    private ProductsLibrary prod_lib;
    // private Seller seller;
    String cartID;

    public Customer(String customer_name , String customer_password , String customer_address ,
                    HashMap<String , Integer> customer_cart , String customer_ID , int primary_ID , String customer_PhNo )
    {
        this.customer_name = customer_name;
        this.customer_password = customer_password;
        this.customer_address = customer_address;
        this.customer_cart = customer_cart;
        this.customer_ID = customer_ID;
        this.primary_ID = primary_ID;
        this.customer_PhNo = customer_PhNo;
        cartID= "C"+customer_ID+"R";
        
    }


    @Override
    public String getName() {
        return customer_name;
    }

    @Override
    public String getCustomer_ID() {
        return customer_ID;
    }

    @Override
    public String getCustomer_PhNo() {
        return customer_PhNo;
    }

    @Override
    public String getPass() {
        return customer_password;
    }

    @Override
    public String getAddress() {
        return customer_address;
    }

    @Override
    public String getCustomerDetails() {
        return ("Primary_ID : "+ getPrimaryID()+"\n"+"Customer_ID : "+ getCustomer_ID()+"\n"+"Name : "+ getName()+"\n"+"Password : "+getPass()+"\n"+
                "Name : "+ getCustomer_PhNo()+"\n"+"Address : "+getAddress());
    }

    public String getCustomerCartString()
    {
        String result = "";
        for(Map.Entry<String , Integer> customer_cart_entry : customer_cart.entrySet() )
        {
            result += customer_cart_entry.getKey()+","+String.valueOf(customer_cart_entry.getValue());
        }
        return result;
    }

    public void showCustomerPurchaseHistory( PostgreSQLClient postgresClient )
    {
        String queryPurchaseHistory = "SELECT * FROM orders";

        try {
            
            Statement ph = postgresClient.getConnectInstance().createStatement();

            ResultSet result = ph.executeQuery(queryPurchaseHistory);

            System.out.println("Purchase history : ");

            while (result.next()) {
                System.out.println(
                    "--------------------------------"+
                    "\nOrder ID : "+result.getString("orderid")+
                    "\nCart ID : "+result.getString("cartid")+
                    "\nNumber of Products : "+result.getString("productcount")+
                    "\nTotal cost : "+result.getString("totalcost")+
                    "\nTotal Quantity : "+result.getString("totalquantity")
                );
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public String getSellerDetailsFile()
    {
        return(
            getPrimaryID()+","+getCustomer_ID()+","+getName()+","+getPass()+","+getCustomer_PhNo()+","+getAddress()+",customer_cart,"+getCustomerCartString()
        );
    }

    public String MapToString( HashMap<String , Integer> map )
    {
        System.err.println(map);
        String result = "";
        for(Map.Entry<String , Integer> map_entry : map.entrySet())
        {
            result += prod_lib.getLibrary().get(map_entry.getKey()).getProd_name()+ " : " + String.valueOf(map_entry.getValue())+"\n";
        }
        return result;
    }

    // public void showPurchaseHistory()
    // {
    //     String final_str = "";
    //     for(Map.Entry<String , HashMap<String , Integer>> historyMap_entry : purchase_history.entrySet() )
    //     {
            
    //            final_str += historyMap_entry.getKey()+" : { \n" + MapToString(historyMap_entry.getValue()) + " } ";
            
    //     }

    //     System.out.println(final_str); 

    //     // System.out.println(purchase_history.values());

    // }

    @Override
    public void addProduct(String prod_ID , ClientConnect mongoClient) {
        Scanner input = new Scanner(System.in);
        int quantity = 0;
        boolean validValue = false;
        try {
            // Product product = prod_lib.getLibrary().get(prod_ID);

            Document product = mongoClient.getProductCollection().find(Filters.eq("ProductID" , prod_ID)).first();

            


            while(!validValue)
            {
                System.out.println("Enter the quantity of products : ");
                quantity = input.nextInt();
                if(customer_cart.containsKey(prod_ID)) quantity += customer_cart.get(prod_ID);
                if (quantity > product.getLong("Stock")) System.out.println("The value must within the stock quantity of this product this is also adds with the existing product count ");
                else validValue = true; 
            }

            try {

                addToCustomeCart(product , quantity);

                System.err.println(customer_cart);

                System.out.println("Product : " + (product.getString("ProductName")) +" added to your cart...");
//                input.close();
                
            } catch (Exception e) {
                System.err.println("The cart is empty try to add products to your cart...");
            }
            
            
            } catch (Exception e) {
                System.err.println("There is no product available...");
                e.printStackTrace();
            }
        
    }

    public void addToCustomeCart( Document product , int quantity )
    {
        customer_cart.put( (product.getString("ProductID")) ,
                    customer_cart.getOrDefault((product.getString("ProductID"))+getCustomer_PhNo() , 0) + quantity
                    );
    }

    @Override
    public void deleteProduct(String prod_ID , Scanner input) {


        try{
            Product product = prod_lib.getLibrary().get(prod_ID);
            try {

                int quantity;

                System.out.println("Enter the quantity to be removed : ");
                quantity = input.nextInt();
                int balance = customer_cart.get(prod_ID) ;
                if( quantity >= balance )
                {
                    boolean valid = false;
                    while(!valid)
                    {
                        String proceed;
                        System.out.println("The quantity is same or more that available quantity in the cart it would remove the whole quantity from youe cart want to proceed further ? (Y/N)");
                        proceed = input.nextLine().toLowerCase().trim();
                        switch (proceed) {
                            case "y":
                                customer_cart.remove(prod_ID);
                                valid = true;
                                break;

                            case "n":
                                System.out.println("Process Canclled...");
                                valid = true;
                                break;
                        
                            default:
                                System.out.println("Enter a valid input...");
                                break;
                        }
                    }
                    
                }
                else
                {
                    customer_cart.put( (prod_ID) , balance - quantity); 
                    System.out.println( "Available quantity of product : "+product.getProd_name()+" is : "+customer_cart.get(prod_ID) );
                }
                
            } catch (Exception e2) {
                e2.printStackTrace();
                System.err.println("The cart is empty try to add products to your cart...");
            }
            
        }catch( Exception e1 )
        {
            System.err.println("There is no product in the store...");
            e1.printStackTrace();
        }

        

    }

    public void setProd_lib(ProductsLibrary prod_lib) {
        this.prod_lib = prod_lib;
    }


    // public void setSeller(Seller prod_seller) {
    //     System.err.println("The seller inside the customer : "+seller);
    //     seller = prod_seller;
    // }

    // public Seller getSeller()
    // {
    //     return seller;
    // }


    @Override
    public void viewCart() {

        try{
            if(customer_cart.isEmpty()) {
                System.err.println("Cart is Empty...");
            }
            else
            {
                for( Map.Entry<String , Integer> product : customer_cart.entrySet())
            {
                System.out.println("Product_id : "+product.getKey()+"\n"+
                        "Number of this items in the cart : " + product.getValue()
                );
            }
            }
            
        }
        catch(Exception e)
        {
            System.err.println("The cart is empty try to add products to your cart...");
            e.printStackTrace();
        }


    }

    public HashMap<String , Integer> getCustomerCart()
    {
        return this.customer_cart;
    }

    public void purchaseProduct( ClientConnect client , PostgreSQLClient postgresClient )
    {
        boolean success = false;
        String queryAddOrderitems = "INSERT INTO order_items (orderid , productid , quantity , price)"+
                                "VALUES ( ? , ? , ? , ?)"; 
        String queryAddOrder = "INSERT INTO orders (orderid , cartid , productcount , totalcost , totalquantity)"+
                                "VALUES ( ? , ? , ? , ? , ?)"; 

        String queryUpdateOrders = "UPDATE orders SET productcount = ? , totalcost = ? , totalquantity = ? WHERE orderid = ?";

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm-ss-SS");
            if(customer_cart.isEmpty()) throw new Exception();
            Long totalCost = 0l;
            Long totalQuantity = 0l;
            Long productCount = 0l;
            String time = formatter.format(LocalTime.now());
            String orderID = "O"+time+"R";
            PreparedStatement st;
            try {
                st = postgresClient.getConnectInstance().prepareStatement(queryAddOrder);
                System.out.println("Order ID in orderitems");
                st.setString(1, orderID);
                st.setString(2, cartID);
                st.setLong(3, 0);
                st.setLong(4, 0);
                st.setLong(5, 0);
                

                st.executeUpdate();
                System.out.println("First order added/.......");
                // st.setLong(3, null);
            } catch (SQLException sqlException) {
                System.err.println("Purchase Failed code - 1F");
            }

            

            for( Map.Entry<String , Integer> cartProducts : customer_cart.entrySet()  )
            {

                Document docs  = new Document("ProductID" ,cartProducts.getKey());

                Document producDocument = client.getProductCollection().find().filter(docs).first();

                Long stock = producDocument.getLong("Stock");
                if( stock > 0 || cartProducts.getValue() <= stock)
                {
                    Long price = producDocument.getLong("Price");
                    Long actualStock = producDocument.getLong("Stock");
                    Document updateProdDoc = new  Document("$set" , new Document("Stock" , (actualStock - cartProducts.getValue())));
                    client.getProductCollection().updateOne(producDocument, updateProdDoc);

                    st = postgresClient.getConnectInstance().prepareStatement(queryAddOrderitems);
                    System.out.println("Order ID in orderitems");
                    st.setString(1, orderID);
                    st.setString(2, cartProducts.getKey());
                    st.setLong(3, cartProducts.getValue());
                    st.setLong(4, price);
                    

                    st.executeUpdate();
                    
                    totalCost = price * cartProducts.getValue();
                    totalQuantity += cartProducts.getValue();
                    productCount++;




                    // if(actualStock - cartProducts.getValue() == 0)
                    // {
                    //     product_DB.get(cartProducts.getKey()).setProd_Visibility(false);
                    //     seller_prod_DB.get(cartProducts.getKey()).setProd_Visibility(false);
                    // }
                }
                else
                {
                    System.out.println("The product is out of stock Consider buying another product...");
                }
            }
            st = postgresClient.getConnectInstance().prepareStatement(queryUpdateOrders);
            st.setLong(1, productCount);
            st.setLong(2, totalCost);
            st.setLong(3, totalQuantity);
            st.setString(4, orderID);

            st.executeUpdate();

            System.out.println("Changing the success variable...");
            success = true;
        } catch (Exception e) {
            System.err.println("The cart is empty try to add products to your cart...");
            e.printStackTrace();
            success = false;
        }

        if(success)
        {
            System.out.println("Purchase successfull...");
            HashMap<String , Integer> map_copy = new HashMap<>(customer_cart);
            purchase_history.put(String.valueOf(purchase_history.size()+1), map_copy);
            System.err.println("In the first : "+purchase_history.values());
            customer_cart.clear();
        }

    }


    @Override
    public void viewProducts(ProductsLibrary prod_lib) {
        // try{
        //     if( prod_lib == null ) throw new Exception("No Products in the store...");
        //     System.out.println(prod_lib.getLibrary());
        //     for( Map.Entry<String , Product> product_map :  prod_lib.getLibrary().entrySet() )
        //     {
        //         if(product_map.getValue().getProd_visiblity())
        //         {
        //             System.out.println(product_map.getValue().getDetails());
        //         }

        //     }
        // }
        // catch(Exception e)
        // {
        //     System.err.println("No products are available...");
        // }

    }


    @Override
    public int getPrimaryID() {
       return primary_ID;
    }
}
