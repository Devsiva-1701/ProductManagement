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
    protected Map< String , Integer > customer_cart;
    private Map< String , HashMap<String , Integer>> purchase_history;
    private ProductsLibrary prod_lib;
    private Seller seller;

    public Customer(String customer_name , String customer_password , String customer_address ,
                    HashMap<String , Integer> customer_cart , String customer_ID , int primary_ID )
    {
        this.customer_name = customer_name;
        this.customer_password = customer_password;
        this.customer_address = customer_address;
        this.customer_cart = (HashMap<String , Integer>) customer_cart;
        this.customer_ID = customer_ID;
        this.primary_ID = primary_ID;
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

                customer_cart.put( (product.getProd_id()+ getCustomer_PhNo()) ,
                    customer_cart.getOrDefault(product.getProd_id()+getCustomer_PhNo() , 0) + quantity
                    );

                System.out.println("Product : " + product.getProd_name() +" added to your cart...");
                input.close();
                
            } catch (Exception e) {
                System.err.println("The cart is empty try to add products to your cart...");
            }
            
            
            } catch (Exception e) {
                System.err.println("There is no product available...");
            }
        
    }

    @Override
    public void deleteProduct(String prod_ID) {

        try{
            Product product = prod_lib.getLibrary().get(prod_ID);
            try {
                customer_cart.put( (product.getProd_id()+getCustomer_PhNo()) ,
                customer_cart.get(product.getProd_id()+getCustomer_PhNo()) - 1
            ); 
            } catch (Exception e2) {
                System.err.println("The cart is empty try to add products to your cart...");
            }
            
        }catch( Exception e1 )
        {
            System.err.println("There is no product in the store...");
        }

        

    }

    public void setProd_lib(ProductsLibrary prod_lib) {
        this.prod_lib = prod_lib;
    }


    public void setSeller(Seller seller) {
        this.seller = seller;
    }


    @Override
    public void viewCart( ProductsLibrary prod_lib ) {

        try{
            if(customer_cart.isEmpty()) throw new NumberFormatException("Null");
            for( Map.Entry<String , Integer> product : customer_cart.entrySet())
            {
                System.out.println("Product_id : "+product.getKey()+"\n"+
                        "Product_Name" + prod_lib.getProductName( product.getKey() )+"\n"+
                        "Number of this items in the cart : " + product.getValue()
                );
            }
        }
        catch(Exception e)
        {
            System.err.println("The cart is empty try to add products to your cart...");
        }


    }

    @Override 
    public void purchaseProduct()
    {
        boolean success = false;

        try {
            if(customer_cart.isEmpty()) throw new Exception();
            for( Map.Entry<String , Integer> cartProducts : customer_cart.entrySet()  )
            {
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
            System.out.println("Changing the success...");
            success = true;
        } catch (Exception e) {
            System.err.println("The cart is empty try to add products to your cart...");
            success = false;
        }

        if(success)
        {
            System.out.println("Purchase successfull...");
            purchase_history.put(String.valueOf(purchase_history.size()+1), (HashMap<String , Integer>)customer_cart);
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
