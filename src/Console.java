import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import product.Product;
import product.ProductCategories;
import product.ProductsLibrary;
import product.RemovedProductsLibrary;
import user.RemovedUsers;
import user.Users;
import user.customer.Customer;
import user.customer.CustomerConsole;
import user.seller.Seller;
import user.seller.SellerConsole;

public class Console {

    Users<Customer> customer_DB = new Users<Customer>();
    Users<Seller> seller_DB = new Users<Seller>();
    RemovedUsers<Customer> removedCustomer_DB = new RemovedUsers<Customer>();
    RemovedUsers<Seller> removedSeller_DB = new RemovedUsers<Seller>();
    ProductsLibrary prod_library = new ProductsLibrary(new HashMap<String, Product>());
    RemovedProductsLibrary removed_prod_lib = new RemovedProductsLibrary(new HashMap<String, Product>());
    private boolean running = false;
    Customer currentCustomer;
    Seller currentSeller;
    public static transient Scanner input = new Scanner(System.in);
    File file;

    public Console() {
        running = true;
    }

    void verifyFile()
    {
        try {
                file = new File("products.txt");
                if(!file.exists())file.createNewFile();
                else  fetchFromFileProducts();
                file = new File("removed_products.txt");
                if(!file.exists())file.createNewFile();
                else  fetchFromFileRemovedProducts();
                file = new File("seller.txt");
                if(!file.exists())file.createNewFile();
                else fetchFromFileSellers();
                file = new File("customer.txt");
                if(!file.exists())file.createNewFile();
            } 
        catch (IOException e) {
                System.err.println("An error occurred in file verification...");
                e.printStackTrace();
        }
    }

    void fetchFromFileProducts()
    {
        try( BufferedReader reader = new BufferedReader(new FileReader("products.txt")) )
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] part_prod = line.split(",");
                boolean visiblity;
                if(part_prod[9].equals("true"))
                {
                    visiblity = true;
                }
                else{
                    visiblity  = false;
                }
                prod_library.addProductToLibrary(
                    new Product(Integer.parseInt(part_prod[0]), part_prod[1], Integer.parseInt(part_prod[2]), 
                    part_prod[3], Byte.parseByte(part_prod[4]), Integer.parseInt(part_prod[5]), part_prod[6], part_prod[7] , 
                    ProductCategories.valueOf(part_prod[8]), visiblity));
            }

        }
        catch( Exception file_exception )
        {
            System.err.println(file_exception);
        }
        
    }

    void fetchFromFileRemovedProducts()
    {
        try( BufferedReader reader = new BufferedReader(new FileReader("removed_products.txt")) )
        {
            String line;
            while((line = reader.readLine()) != null)
            {
                String[] part_prod = line.split(",");
                boolean visiblity;
                if(part_prod[8].equals("true"))
                {
                    visiblity = true;
                }
                else{
                    visiblity  = false;
                }
                removed_prod_lib.addProductToLibrary(
                    new Product(Integer.parseInt(part_prod[0]), part_prod[1], Integer.parseInt(part_prod[2]), 
                    part_prod[3], Byte.parseByte(part_prod[4]), Integer.parseInt(part_prod[5]), part_prod[6], part_prod[7] ,
                    ProductCategories.valueOf(part_prod[8]), visiblity));
            }

        }
        catch( Exception file_exception )
        {
            System.err.println(file_exception);
        }
    }

    void fetchFromFileSellers()
    {
        try( BufferedReader reader = new BufferedReader(new FileReader("seller.txt")) )
        {
            String line;
            HashMap<String , Product> seller_prod = new HashMap<String , Product>();
            HashMap<String , Product> removed_seller_prod = new HashMap<String , Product>();
            boolean isProduct = false;
            while((line = reader.readLine()) != null)
            {
                int count_prod = 6;
                String[] part_prod = line.split(",");
                for( String str : part_prod )
                {
                    System.err.println( str );
                }
                if(!part_prod[count_prod].equals("RemovedSellerProducts"))
                {
                    isProduct = true;
                }
                while(isProduct)
                {
                    System.err.println("before : "+count_prod);
                    seller_prod.put(part_prod[count_prod], prod_library.getLibrary().get(part_prod[count_prod]));
                    count_prod++;
                    if(part_prod[count_prod].equals("RemovedSellerProducts")) isProduct = false;
                    System.err.println("After : "+count_prod);
                }
                if(!(count_prod == part_prod.length-1))
                {
                    isProduct = true;
                }
                while(isProduct)
                {
                    removed_seller_prod.put(part_prod[count_prod], removed_prod_lib.getLibrary().get(part_prod[count_prod]));
                    count_prod++;
                    if(count_prod == part_prod.length) isProduct = false;
                }
                seller_DB.setUser( new Seller(prod_library, removed_prod_lib, part_prod[2], seller_prod, 
                part_prod[1], Integer.parseInt(part_prod[0]), part_prod[3], Long.parseLong(part_prod[4]), removed_seller_prod, input) ); 
                seller_DB.addUser(part_prod[1]);         
                
            }

        }
        catch( Exception file_exception )
        {
            System.err.println("From sellers : "+file_exception);
        }
        
    }

    void startConsole() {
        verifyFile();
        while (running) {
            System.out.println("CustomerDB : " + customer_DB.getDB());
            System.out.println("SellerDB : " + seller_DB.getDB());
            System.out.println("Console Started Successfully...");
            System.out.println("Enter the Login Type (Type Text) .\n 1.Customer \n 2.Seller \n 3.CloseConsole(CC)");
            int auth_type;

                auth_type = input.nextInt();
                // Consume the newline character


            switch (auth_type) {
                case 1:
                    System.out.println("Login or SignUp (Type Text) .\n 1.Login \n 2.SignUp");
                    int customer_auth;

                    customer_auth = input.nextInt();

                    switch (customer_auth) {
                        case 1:
                            login("customer" , input);
                            break;
                        case 2:
                            signUp("customer" , input);
                            break;
                        default:
                            System.err.println("Enter a valid Input...");
                            break;
                    }
                    break;

                case 2:
                    System.out.println("Login or SignUp (Type Text) .\n 1.Login \n 2.SignUp");
                    int seller_auth;
                    if (input.hasNextInt()) {
                        seller_auth = input.nextInt();
                        input.nextLine(); // Consume the newline character
                    } else {
                        input.nextLine(); // Clear the invalid input
                        System.out.println("Invalid Input...");
                        continue;
                    }

                    switch (seller_auth) {
                        case 1:
                            login("seller" , input);
                            break;
                        case 2:
                            signUp("seller" , input);
                            break;
                        default:
                            System.err.println("Enter a valid Input...");
                            break;
                    }
                    break;

                case 3:
                    running = false;
                    break;

                default:
                    System.out.println("Invalid Input...");
                    break;
            }
        }
    }

    void signUp(String user , Scanner input) {


        switch (user) {
            case "customer":
                String customer_name;
                String customer_pass;
                String customer_address;
                Long customer_phNo;
                String customer_ID;
                int CustomerPrimaryID = 0;

                if (removedCustomer_DB.getDB().isEmpty()) {
                    if (customer_DB.getDB().isEmpty()) {
                        CustomerPrimaryID = 1;
                    } else {
                        int loop = 1;
                        for (Map.Entry<String, Customer> customer_entry : customer_DB.getDB().entrySet()) {
                            if (loop == customer_DB.getDB().size() - 1)
                                CustomerPrimaryID = customer_entry.getValue().getPrimaryID();
                            loop++;
                        }
                    }
                } else {
                    for (Map.Entry<String, Customer> removed_customer_entry : removedCustomer_DB.getDB().entrySet()) {
                        CustomerPrimaryID = removed_customer_entry.getValue().getPrimaryID();
                        removedCustomer_DB.deleteRemovedUser(removed_customer_entry.getValue().getCustomer_ID());
                        break;
                    }
                }

                System.out.println("Enter your Name : ");
                customer_name = input.nextLine();
                customer_name = input.nextLine();
                System.out.println("Enter the Password : ");
                customer_pass = input.nextLine();
                System.out.println("Enter the Address : ");
                customer_address = input.nextLine();
                System.out.println("Enter the Phone Number : ");
                customer_phNo = input.nextLong();

                customer_ID = (customer_phNo.toString() + customer_name + String.valueOf(CustomerPrimaryID));

                customer_DB.setUser(new Customer(customer_name, customer_pass, customer_address, new HashMap<String, Integer>(), customer_ID, CustomerPrimaryID , String.valueOf(customer_phNo)));

                if (customer_DB.existingUser(customer_ID)) {
                    System.out.println("User already exists...");
                } else {
                    customer_DB.addUser(customer_ID);
                    System.out.println("Registration for : " + customer_name + " is successful...");
                    System.out.println("Your User ID is : " + customer_ID);
                }
                break;

            case "seller":
                String seller_name;
                String seller_pass;
                Long seller_phNo;
                String seller_ID;
                int SellerPrimary_ID = 0;

                if (removedSeller_DB.getDB().isEmpty()) {
                    if (seller_DB.getDB().isEmpty()) {
                        SellerPrimary_ID = 1;
                    } else {
                        int loop = 1;
                        for (Map.Entry<String, Seller> seller_entry : seller_DB.getDB().entrySet()) {
                            if (loop == seller_DB.getDB().size() - 1)
                                SellerPrimary_ID = seller_entry.getValue().getPrimaryID();
                            loop++;
                        }
                    }
                } else {
                    for (Map.Entry<String, Seller> removed_seller_entry : removedSeller_DB.getDB().entrySet()) {
                        SellerPrimary_ID = removed_seller_entry.getValue().getPrimaryID();
                        removedSeller_DB.deleteRemovedUser(removed_seller_entry.getValue().getSeller_ID());
                        break;
                    }
                }

                System.out.println("Enter your Name : ");
                seller_name = input.nextLine();
                System.out.println("Enter the Password : ");
                seller_pass = input.nextLine();
                System.out.println("Enter the Phone Number : ");
                seller_phNo = input.nextLong();

                seller_ID = (seller_phNo.toString() + seller_name + String.valueOf(SellerPrimary_ID));

                seller_DB.setUser(new Seller(prod_library, removed_prod_lib, seller_name, new HashMap<String, Product>(), seller_ID, SellerPrimary_ID, seller_pass, seller_phNo, new HashMap<String, Product>() , input));

                if (seller_DB.existingUser(seller_ID)) {
                    System.out.println("User already exists...");
                } else {
                    seller_DB.addUser(seller_ID);
                    System.out.println("Registration for : " + seller_name + " is successful...");
                }
                break;

            default:
                System.out.println("Invalid User...");
                break;
        }
    }

    void login(String user , Scanner input) {

        switch (user) {
            case "customer":
                String customerID;
                String customer_pass;
                System.out.println("Enter the user ID : ");
                customerID = input.nextLine();
                customerID = input.nextLine();
                System.out.println("Enter your Password : ");
                customer_pass = input.nextLine();

                currentCustomer = customer_DB.getUser(customerID);

                if (currentCustomer == null) {
                    System.out.println("User is not available...");
                } else if (!customer_pass.equals(currentCustomer.getPass())) {
                    System.out.println("Incorrect Password...");
                } else {
                    System.out.println("Login In Successful...");
                    CustomerConsole customerConsole = new CustomerConsole(removedCustomer_DB, customer_DB, seller_DB, currentCustomer, prod_library);
                    customerConsole.start_customer_console();
                    running = false;
                }
                break;

            case "seller":
                String sellerID;
                String seller_pass;
                System.out.println("Enter the user ID : ");
                sellerID = input.nextLine();
                System.out.println("Enter your Password : ");
                seller_pass = input.nextLine();

                currentSeller = seller_DB.getUser(sellerID);

                if (currentSeller == null) {
                    System.out.println("User is not available...");
                } else if (!seller_pass.equals(currentSeller.getPass())) {
                    System.out.println("Incorrect Password...");
                } else {
                    System.out.println("Login In Successful...");
                    SellerConsole sellerConsole = new SellerConsole(removedSeller_DB, seller_DB, removed_prod_lib, currentSeller, prod_library);
                    sellerConsole.start_seller_console(input);
                    running = false;
                }
                break;

            default:
                System.out.println("Enter a valid Input...");
                break;
        }
    }
}
