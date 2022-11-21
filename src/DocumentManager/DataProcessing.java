package DocumentManager;
import java.util.*;
import java.sql.*;

public  class DataProcessing {

    static Connection connection;
    static Statement statement;
    static ResultSet resultSet;
    static boolean connectedToDatabase=false;
    static String drivername = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/users_informations";
    static  String user = "root";
    static String password= "293617";
    static Hashtable<String, User> users;
    static Hashtable<String,Doc> docs;


    static {
        connectedToDatabase = false;
        users = new Hashtable<String, User>();
        users.put("jack", new Operator("jack","123","operator"));
        users.put("rose", new Browser("rose","123","browser"));
        users.put("kate", new Administrator("kate","123","administrator"));
        Init();

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        docs = new Hashtable<String,Doc>();
        docs.put("0001",new Doc("0001","jack",timestamp,"Doc Source java","Doc.java"));
    }

    public static  void Init(){
        // connect to database

        // update database connection status
//        if (Math.random()>0.2)
//            connectToDB = true;
//        else
//            connectToDB = false;

    }

    public static Doc searchDoc(String ID) throws SQLException {
        if (docs.containsKey(ID)) {
            Doc temp =docs.get(ID);
            return temp;
        }
        return null;
    }

    public static Enumeration<Doc> getAllDocs() throws SQLException{
        Enumeration<Doc> e  = docs.elements();
        return e;
    }

    public static boolean insertDoc(String ID, String creator, Timestamp timestamp, String description, String filename) throws SQLException{
        Doc doc;

        if (docs.containsKey(ID))
            return false;
        else{
            doc = new Doc(ID,creator,timestamp,description,filename);
            docs.put(ID, doc);
            return true;
        }
    }


    public static User searchUser(String name) throws SQLException{
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Query" );

        if (users.containsKey(name)) {
            return users.get(name);
        }
        return null;
    }

    public static User search(String name, String password) throws SQLException {
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Query" );

        if (users.containsKey(name)) {
            User temp =users.get(name);
            if ((temp.getPassword()).equals(password))
                return temp;
        }
        return null;
    }

    public static Enumeration<User> getAllUser() throws SQLException{
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Query" );

        Enumeration<User> e  = users.elements();
        return e;
    }



    public static boolean update(String name, String password, String role) throws SQLException{
        User user;
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Update" );

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

    public static boolean insertUser(String name, String password, String role) throws SQLException{
        User user;

//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Insert" );

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

    public static boolean deleteUser(String name) throws SQLException{
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Delete" );

        if (users.containsKey(name)){
            users.remove(name);
            return true;
        }else
            return false;

    }

    public void disconnectFromDB() {
        if ( connectedToDatabase ){
            // close Statement and Connection
            try{
//                if (Math.random()>0.5)
//                    throw new SQLException( "Error in disconnecting DB" );
//            }catch ( SQLException sqlException ){
//                sqlException.printStackTrace();
            }finally{
                connectedToDatabase = false;
            }
        }
    }


    public static void main(String[] args) {

    }

}
