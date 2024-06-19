package user;
import java.util.HashMap;

public class RemovedUsers<T> {
    private T removedUserType;
    private HashMap< String , T > removedUser_DB;

    public RemovedUsers()
    {
        this.removedUser_DB = new HashMap<String , T>();
    }

    public void setUser( T userType )
    {
        this.removedUserType = userType;
    }

    public HashMap<String , T> getDB()
    {
        return removedUser_DB;
    }

    public void addRemovedUser( String user_id )
    {
        removedUser_DB.put( user_id , removedUserType);
    }

    public void deleteRemovedUser( String userID )
    {
        removedUser_DB.remove(userID);
    }

}
