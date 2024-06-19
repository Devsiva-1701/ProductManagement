package user.customer;

import java.util.InputMismatchException;
import java.util.Scanner;

import product.ProductsLibrary;
import user.RemovedUsers;
import user.Users;

public class CustomerConsole {
    private RemovedUsers<Customer> removed_customer_DB;
    private Users<Customer> customer_DB;
    private Customer current_customer;
    private boolean customer_active = true;
    private ProductsLibrary prod_lib;

    public CustomerConsole( 
        RemovedUsers<Customer> removed_customer_DB , Users<Customer>  customer_DB,
     Customer current_customer , ProductsLibrary prod_lib )
    {
        this.removed_customer_DB = removed_customer_DB;
        this.current_customer = current_customer;
        this.prod_lib = prod_lib;
        this.customer_DB = customer_DB;
    }

    public void start_customer_console()
    {
        Scanner input = new Scanner(System.in);
        while( customer_active )
        {
            int option = 0;
            System.out.println(" Enter the options (number) : \n 1.View Products \n 2.View Cart \n 3.Add product to cart \n 4.Delete product from cart \n 5.Purchase Product \n 6.LogOut \n 7.Delete Account");
            try{
                option = input.nextInt();
            }
            catch( InputMismatchException input_miss_match ) {
                System.err.println("Input Miss Match enter a valid input...");
            }
            switch(option)
            {
                case 1:
                    current_customer.viewProducts(prod_lib);
                    break;
                
                case 2:
                    current_customer.viewCart(prod_lib);
                    break;

                case 3:
                    String prod_ID_Add;
                    System.out.println("Enter the product ID : ");
                    prod_ID_Add = input.nextLine();
                    prod_ID_Add = input.nextLine();
                    current_customer.addProduct(prod_ID_Add);   
                    break;
                    
                case 4:
                    String prod_ID_Delete;
                    System.out.println("Enter the product ID : ");
                    prod_ID_Delete = input.nextLine();
                    prod_ID_Delete = input.nextLine();
                    current_customer.deleteProduct(prod_ID_Delete);
                    break;

                case 5:
                    current_customer.purchaseProduct();
                    break;

                case 6:
                    customer_active = false;
                    break;

                case 7:
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

        input.close();
        
    }

}
