package user;
import java.util.HashMap;

public class Users<T> {

    private T userType;
    private HashMap< String , T > user_DB;

    public Users()
    {
        this.user_DB = new HashMap<String , T>();
    }

    public void setUser( T userType )
    {
        this.userType = userType;
    }

    public HashMap<String , T> getDB()
    {
        return user_DB;
    }

    public boolean existingUser( String user_id )
    {
        if( user_DB.containsKey(user_id) )
        {
            return true;
        }
        else{
            return false;
        }
    }

    public T getUser( String user_id )
    {
        return user_DB.get(user_id);
    }

    public void addUser( String user_id )
    {
        user_DB.put( user_id , userType);
    }

    public void deleteUser( String userID )
    {
        user_DB.remove(userID);
    }



}
