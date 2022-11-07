package DocumentManager;
import java.util.*;
import java.sql.*;

public  class DataProcessing {
    private static boolean connectToDB=false;

    static Hashtable<String, User> users;

    static {
        users = new Hashtable<String, User>();
        users.put("jack", new Operator("jack","123","operator"));
        users.put("rose", new Browser("rose","123","browser"));
        users.put("kate", new Administrator("kate","123","administrator"));
        Init();
    }

    public static  void Init(){
        // connect to database

        // update database connection status
        if (Math.random()>0.2)
            connectToDB = true;
        else
            connectToDB = false;

    }

    public static User searchUser(String name) throws SQLException{
        if ( !connectToDB )
            throw new SQLException( "Not Connected to Database" );
        double ranValue=Math.random();
        if (ranValue>0.5)
            throw new SQLException( "Error in excecuting Query" );

        if (users.containsKey(name)) {
            return users.get(name);
        }
        return null;
    }

    public static User search(String name, String password) throws SQLException {
        if ( !connectToDB )
            throw new SQLException( "Not Connected to Database" );
        double ranValue=Math.random();
        if (ranValue>0.5)
            throw new SQLException( "Error in excecuting Query" );

        if (users.containsKey(name)) {
            User temp =users.get(name);
            if ((temp.getPassword()).equals(password))
                return temp;
        }
        return null;
    }

    public static Enumeration<User> getAllUser() throws SQLException{
        if ( !connectToDB )
            throw new SQLException( "Not Connected to Database" );

        double ranValue=Math.random();
        if (ranValue>0.5)
            throw new SQLException( "Error in excecuting Query" );

        Enumeration<User> e  = users.elements();
        return e;
    }



    public static boolean update(String name, String password, String role) throws SQLException{
        User user;
        if ( !connectToDB )
            throw new SQLException( "Not Connected to Database" );

        double ranValue=Math.random();
        if (ranValue>0.5)
            throw new SQLException( "Error in excecuting Update" );

        if (users.containsKey(name)) {
            if (role.equalsIgnoreCase("administrator"))
                user = new Administrator(name,password, role);
            else if (role.equalsIgnoreCase("operator"))
                user = new Operator(name,password, role);
            else
                user = new Browser(name,password, role);
            users.put(name, user);
            return true;
        }else
            return false;
    }

    public static boolean insert(String name, String password, String role) throws SQLException{
        User user;

        if ( !connectToDB )
            throw new SQLException( "Not Connected to Database" );

        double ranValue=Math.random();
        if (ranValue>0.5)
            throw new SQLException( "Error in excecuting Insert" );

        if (users.containsKey(name))
            return false;
        else{
            if (role.equalsIgnoreCase("administrator"))
                user = new Administrator(name,password, role);
            else if (role.equalsIgnoreCase("operator"))
                user = new Operator(name,password, role);
            else
                user = new Browser(name,password, role);
            users.put(name, user);
            return true;
        }
    }

    public static boolean delete(String name) throws SQLException{
        if ( !connectToDB )
            throw new SQLException( "Not Connected to Database" );

        double ranValue=Math.random();
        if (ranValue>0.5)
            throw new SQLException( "Error in excecuting Delete" );

        if (users.containsKey(name)){
            users.remove(name);
            return true;
        }else
            return false;

    }

    public void disconnectFromDB() {
        if ( connectToDB ){
            // close Statement and Connection
            try{
                if (Math.random()>0.5)
                    throw new SQLException( "Error in disconnecting DB" );
            }catch ( SQLException sqlException ){
                sqlException.printStackTrace();
            }finally{
                connectToDB = false;
            }
        }
    }


    public static void main(String[] args) {

    }

}
