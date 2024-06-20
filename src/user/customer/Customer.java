package user.customer;


import product.Product;
import product.ProductsLibrary;
import user.seller.Seller;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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
    private Seller seller;

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

    public String getCustomerPurchaseHistoryString()
    {
        String result = "";
        for(Map.Entry<String , Integer> customer_cart_entry : customer_cart.entrySet() )
        {
            result += customer_cart_entry.getKey()+","+String.valueOf(customer_cart_entry.getValue());
        }
        return result;
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

    public void showPurchaseHistory()
    {
        String final_str = "";
        for(Map.Entry<String , HashMap<String , Integer>> historyMap_entry : purchase_history.entrySet() )
        {
            
               final_str += historyMap_entry.getKey()+" : { \n" + MapToString(historyMap_entry.getValue()) + " } ";
            
        }

        System.out.println(final_str); 

        // System.out.println(purchase_history.values());

    }

    @Override
    public void addProduct(String prod_ID) {
        Scanner input = new Scanner(System.in);
        int quantity = 0;
        boolean validValue = false;
        try {
            Product product = prod_lib.getLibrary().get(prod_ID);
            while(!validValue)
            {
                System.out.println("Enter the quantity of products : ");
                quantity = input.nextInt();
                if(customer_cart.containsKey(product.getProd_id())) quantity += customer_cart.get(product.getProd_id());
                if (quantity > product.getProd_stock()) System.out.println("The value must within the stock quantity of this product this is also adds with the existing product count ");
                else validValue = true; 
            }

            try {

                addToCustomeCart(product , quantity);

                    System.err.println(customer_cart);

                System.out.println("Product : " + product.getProd_name() +" added to your cart...");
//                input.close();
                
            } catch (Exception e) {
                System.err.println("The cart is empty try to add products to your cart...");
            }
            
            
            } catch (Exception e) {
                System.err.println("There is no product available...");
                e.printStackTrace();
            }
        
    }

    public void addToCustomeCart( Product product , int quantity )
    {
        customer_cart.put( (product.getProd_id()) ,
                    customer_cart.getOrDefault(product.getProd_id()+getCustomer_PhNo() , 0) + quantity
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


    public void setSeller(Seller prod_seller) {
        System.err.println("The seller inside the customer : "+seller);
        seller = prod_seller;
    }

    public Seller getSeller()
    {
        return seller;
    }


    @Override
    public void viewCart( ProductsLibrary prod_lib) {

        try{
            if(customer_cart.isEmpty()) {
                System.err.println("Cart is Empty...");
            }
            else
            {
                for( Map.Entry<String , Integer> product : customer_cart.entrySet())
            {
                System.out.println("Product_id : "+product.getKey()+"\n"+
                        "Product_Name : " + prod_lib.getProductName( product.getKey() )+"\n"+
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

    public void purchaseProduct( )
    {
        boolean success = false;

        try {
            if(customer_cart.isEmpty()) throw new Exception();
            for( Map.Entry<String , Integer> cartProducts : customer_cart.entrySet()  )
            {
                System.err.println(getSeller());
                HashMap<String , Product> product_DB = prod_lib.getLibrary();
                HashMap<String , Product> seller_prod_DB = seller.getSellerProducts();
                if(product_DB.containsKey(cartProducts.getKey()))
                {
                    int actualStock = product_DB.get(cartProducts.getKey()).getProd_stock();;
                    product_DB.get(cartProducts.getKey()).setProd_stock( actualStock - cartProducts.getValue() );
                    seller_prod_DB.get(cartProducts.getKey()).setProd_stock( actualStock - cartProducts.getValue() );
                    if(actualStock - cartProducts.getValue() == 0)
                    {
                        product_DB.get(cartProducts.getKey()).setProd_Visibility(false);
                        seller_prod_DB.get(cartProducts.getKey()).setProd_Visibility(false);
                    }
                }
            }
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
        try{
            if( prod_lib == null ) throw new Exception("No Products in the store...");
            System.out.println(prod_lib.getLibrary());
            for( Map.Entry<String , Product> product_map :  prod_lib.getLibrary().entrySet() )
            {
                if(product_map.getValue().getProd_visiblity())
                {
                    System.out.println(product_map.getValue().getDetails());
                }

            }
        }
        catch(Exception e)
        {
            System.err.println("No products are available...");
        }

    }


    @Override
    public int getPrimaryID() {
       return primary_ID;
    }
}
