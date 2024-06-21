package user.customer;

import java.util.InputMismatchException;
import java.util.Scanner;

import Mongo.ClientConnect;
import product.ProductsLibrary;
import user.RemovedUsers;
import user.Users;
import user.seller.Seller;

public class CustomerConsole {
    private RemovedUsers<Customer> removed_customer_DB;
    private Users<Customer> customer_DB;
    private Users<Seller> seller_DB;
    private Customer current_customer;
    private boolean customer_active = true;
    private ProductsLibrary prod_lib;
    ClientConnect clientConnect;

    public CustomerConsole( ClientConnect clientConnect,
        RemovedUsers<Customer> removed_customer_DB , Users<Customer>  customer_DB, Users<Seller> seller_DB ,
     Customer current_customer , ProductsLibrary prod_lib )
    {
        this.removed_customer_DB = removed_customer_DB;
        this.current_customer = current_customer;
        this.prod_lib = prod_lib;
        this.customer_DB = customer_DB;
        this.seller_DB = seller_DB;
        this.clientConnect = clientConnect;
    }

    public void start_customer_console()
    {
        Scanner input = new Scanner(System.in);
        while( customer_active )
        {
            int option = 0;
            System.out.println(" Enter the options (number) : \n 1.View Products \n 2.View Cart \n 3.Add product to cart \n 4.Delete product from cart \n 5.Purchase Product \n 6.LogOut \n" + //
                                " 7.Show PurchaseHistory \n 8.Delete Account");
            try{
                option = input.nextInt();
                input.nextLine();
            }
            catch( InputMismatchException input_miss_match ) {
                System.err.println("Input Miss Match enter a valid input...");
            }
            switch(option)
            {
                case 1:
                    clientConnect.getProductLibrary_Mongo();
                    // current_customer.viewProducts(prod_lib);
                    break;
                
                case 2:
                    current_customer.viewCart(prod_lib);
                    break;

                case 3:
                    String prod_ID_Add;
                    System.out.println("Enter the product ID : ");
                    prod_ID_Add = input.nextLine();
                    current_customer.setProd_lib(prod_lib);
                    System.err.println(seller_DB.getDB());
                    System.err.println(prod_lib.getLibrary());
                    current_customer.setSeller(seller_DB.getDB().get(prod_lib.getLibrary().get(prod_ID_Add).getSellerID()) );
                    current_customer.addProduct(prod_ID_Add);
                    System.err.println("Seller after the process  : "+current_customer.getSeller());   
                    break;
                    
                case 4:
                    String prod_ID_Delete;
                    System.out.println("Enter the product ID : ");
                    prod_ID_Delete = input.nextLine();
                    current_customer.deleteProduct(prod_ID_Delete , input);
                    break;

                case 5:

                    current_customer.purchaseProduct( clientConnect );
                    break;

                case 6:
                    // try 
                    // {
                    //     System.err.println("From console : "+prod_lib.getLibrary());
                    //     // System.err.println("From console : "+ removed_prod_lib.getLibrary());
                    //     BufferedWriter writer = new BufferedWriter( new FileWriter("customer.txt") );

                    //     for( Map.Entry<String , Customer> customer_entry : customer_DB.getDB().entrySet() )
                    //     {
                    //         System.err.println(customer_DB.getDB());
                    //         Customer customerFromMap = customer_entry.getValue();
                    //         System.out.println(customerFromMap.getSellerDetailsFile());
                    //         writer.write( sellerFromMap.getSellerDetailsFile());
                    //         System.out.println("Writing in customer");
                    //         writer.newLine();
                    //     }
                    //     writer.close();
                    //     BufferedWriter writer_2 = new BufferedWriter(new FileWriter("products.txt"));
                        
                    //     if(!prod_lib.getLibrary().isEmpty())
                    //     {
                    //         for( Map.Entry<String , Product> product_entry : prod_lib.getLibrary().entrySet() )
                    //         {
                    //             Product productFromMap = product_entry.getValue();
                    //             writer_2.write( productFromMap.getDetailsFile());
                    //             writer_2.newLine();
                    //         }
                    //     }
                        
                    //     writer_2.close();
                    //     BufferedWriter writer_3 = new BufferedWriter(new FileWriter("removed_products.txt"));
                        
                    //     if(!removed_prod_lib.getLibrary().isEmpty())
                    //     {
                    //         for(Map.Entry<String ,Product> removed_prod_entry : removed_prod_lib.getLibrary().entrySet() )
                    //         {
                    //             Product productFromMap = removed_prod_entry.getValue();
                    //             writer_3.write( productFromMap.getDetailsFile() );
                    //             System.out.println("Writing in removed prod");
                    //             writer_3.newLine();
                    //         }
                    //     }
                    //     writer_3.close();
                    //     System.out.println("File Created...");
                        
                    // } catch (Exception e) {
                    //     System.err.println("File creation failed...");
                    //     e.printStackTrace();
                    // }
                    customer_active = false;
                    break;

                case 7:
                    current_customer.showPurchaseHistory();
                    break;

                case 8:
                    removed_customer_DB.addRemovedUser(current_customer.getCustomer_ID());
                    customer_DB.deleteUser(current_customer.getCustomer_ID());
                    System.out.println("Your Account has been deleted..");
                    customer_active = false;
                    break;

                default:
                    System.out.println("Invalid Input...");
                    break;

            }

        }

        // input.close();
        
    }

}
