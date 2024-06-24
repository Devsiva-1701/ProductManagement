package PostgreSQLClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLClient {

    private String URL;
    private String userName;
    private String password;
    protected Connection connection;

    public PostgreSQLClient(String URL, String userName, String password) {
        this.URL = URL;
        this.userName = userName;
        this.password = password;
    }

    public void connectClient() throws SQLException
    {
        connection = DriverManager.getConnection(URL, userName, password);
    }

    public Connection getConnectInstance()
    {
        return connection;
    }

    public String getProductDetails( ResultSet result ) throws SQLException
    {
        return "-----------------------------"+
                    "\nProductName : "+result.getString("ProductName") +
                    "\nCategory : "+result.getString("Category") + 
                    "\nProductID : "+result.getString("ProductID") + 
                    "\nCategoryID : "+result.getString("CategoryID") + 
                    "\nPrice : "+result.getInt("Price") +
                    "\nStock : "+ ((result.getLong("stock") == 0l) ? "Out of Stock" : result.getLong("Stock"));
    }
}
