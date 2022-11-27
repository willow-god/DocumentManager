package ServerDocumentManager;
import java.util.*;
import java.sql.*;

public  class DataProcessing {

    static Connection connection;
    static Statement statement;
    static ResultSet resultset;
    static boolean connectedToDatabase=false;
    static String drivername = "com.mysql.cj.jdbc.Driver";
    static String url = "jdbc:mysql://localhost:3306/users_informations";
    static  String user = "root";
    static String password= "293617";


    static Hashtable<String, User> users;
    static Hashtable<String,Doc> docs;




    static {
        connectedToDatabase = false;
        try{
            Class.forName(drivername);
            connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        connectedToDatabase = true;
        System.out.println(true);
//        users = new Hashtable<String, User>();
//        users.put("jack", new Operator("jack","123","operator"));
//        users.put("rose", new Browser("rose","123","browser"));
//        users.put("kate", new Administrator("kate","123","administrator"));
//        Init();
//
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        docs = new Hashtable<String,Doc>();
//        docs.put("0001",new Doc("0001","jack",timestamp,"Doc Source java","Doc.java"));
    }

    public static  void Init(){
        // connect to database

        // update database connection status
//        if (Math.random()>0.2)
//            connectToDB = true;
//        else
//            connectToDB = false;

    }

    public static Doc searchDoc(String ID){
        Timestamp timestamp;
        String creator,description,filename;
        Doc doc = null;
        try {
            statement = connection.createStatement();
            resultset = statement.executeQuery("select * from doc_info where Id='"+ID+"'");
            if(resultset.next()){
                creator=resultset.getString("creator");
                timestamp=resultset.getTimestamp("timestamp");
                description=resultset.getString("description");
                filename=resultset.getString("filename");
                doc=new Doc(ID,creator,timestamp,description,filename);
                return doc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
//        if (docs.containsKey(ID)) {
//            Doc temp =docs.get(ID);
//            return temp;
//        }
//        return null;
//        本地编写的一些小玩意，不上档次，现在进行的是超级无敌嗷嗷嗷奥里给数据库编写
    }

    synchronized public static Enumeration<Doc> getAllDocs() throws SQLException{
//        Enumeration<Doc> e  = docs.elements();
//        return e;

        Vector<Doc> vec=new Vector<Doc>();
        Doc doc = null;
        Timestamp timestamp;
        String ID,creator,description,filename;
        System.out.println(connectedToDatabase);
        if(!connectedToDatabase)
            throw new SQLException("Not Connected to Database.");
        try {
            statement = connection.createStatement();
            System.out.println("连接aaa成功");
            resultset=statement.executeQuery("select * from doc_info");
            System.out.println("链接bbb成功");
            while(resultset.next()){
                ID=resultset.getString("Id");
                creator=resultset.getString("creator");
                timestamp=resultset.getTimestamp("timestamp");
                description=resultset.getString("description");
                filename=resultset.getString("filename");
                doc=new Doc(ID,creator,timestamp,description,filename);
                vec.add(doc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vec.elements();
    }

    synchronized public static boolean insertDoc(String ID, String creator, Timestamp timestamp, String description, String filename){
//        Doc doc;
//        if (docs.containsKey(ID))
//            return false;
//        else{
//            doc = new Doc(ID,creator,timestamp,description,filename);
//            docs.put(ID, doc);
//            return true;
//        }//上面是之前文件中的相关操作~现在进行数据库的相关内容
        try {
            statement = connection.createStatement();
            if(statement.executeUpdate("insert into doc_info values('"+ID+"','"+creator+"','"+timestamp+"','"+description+"','"+filename+"')") != 0)
                return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    synchronized public static User searchUser(String name){
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Query" );
//        if (users.containsKey(name)) {
//            return users.get(name);
//        }
//        return null;

        String password,role;
        User user;
        try {
            statement = connection.createStatement();
            resultset=statement.executeQuery("select * from user_info where username='"+name+"'");
            if(resultset.next()){
                password=resultset.getString("password");
                role=resultset.getString("role");
                switch(role){
                    case "administrator":
                        return user=new Administrator(name,password,role);
                    case "browser":
                        return user=new Browser(name,password,role);
                    case "operator":
                        return user=new Operator(name,password,role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    synchronized public static User search(String name, String password){
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Query" );
//        if (users.containsKey(name)) {
//            User temp =users.get(name);
//            if ((temp.getPassword()).equals(password))
//                return temp;
//        }
//        return null;

        String role;
        User user = null;
        try {
            statement = connection.createStatement();
            resultset=statement.executeQuery("select * from user_info where username='"+name+"' AND"+" password='"+password+"'");
            if(resultset.next()){
                role=resultset.getString("role");
                switch(role){
                    case "administrator":
                        return user=new Administrator(name,password,role);
                    case "browser":
                        return user=new Browser(name,password,role);
                    case "operator":
                        return user=new Operator(name,password,role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    synchronized public static Enumeration<User> getAllUser() throws SQLException{
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Query" );
//        Enumeration<User> e  = users.elements();
//        return e;

        Vector<User> vec=new Vector<User>();
        User user;
        String name,password,role;
        try {
            statement = connection.createStatement();
            resultset = statement.executeQuery("select * from user_info");
            while(resultset.next()){
                name=resultset.getString("username");
                password=resultset.getString("password");
                role=resultset.getString("role");
                switch(role){
                    case "administrator":
                        user=new Administrator(name,password,role);break;
                    case "browser":
                        user=new Browser(name,password,role);break;
                    case "operator":
                        user=new Operator(name,password,role);break;
                    default:
                        user=null;
                }
                vec.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vec.elements();
    }



    synchronized public static boolean update(String name, String password, String role){
//        User user;
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Update" );
//        if (users.containsKey(name)) {
//            if (role.equalsIgnoreCase("administrator"))
//                user = new Administrator(name,password, role);
//            else if (role.equalsIgnoreCase("operator"))
//                user = new Operator(name,password, role);
//            else
//                user = new Browser(name,password, role);
//            users.put(name, user);
//            return true;
//        }else
//            return false;

        try {
            statement = connection.createStatement();
            if((statement.executeUpdate("update user_info set password='"+password+"',role='"+role+"' where username='"+name+"'"))!=0)
                return true;

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    synchronized public static boolean insertUser(String name, String password, String role){
//        User user;

//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Insert" );

//        if (users.containsKey(name))
//            return false;
//        else{
//            if (role.equalsIgnoreCase("administrator"))
//                user = new Administrator(name,password, role);
//            else if (role.equalsIgnoreCase("operator"))
//                user = new Operator(name,password, role);
//            else
//                user = new Browser(name,password, role);
//            users.put(name, user);
//            return true;
//        }
        try {
            statement = connection.createStatement();
            if(!statement.execute("insert into user_info values('" + name + "','" + password + "','" + role + "')"))
                return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    synchronized public static boolean deleteUser(String name){
//        if ( !connectToDB )
//            throw new SQLException( "Not Connected to Database" );
//
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in excecuting Delete" );
//        if (users.containsKey(name)){
//            users.remove(name);
//            return true;
//        }else
//            return false;

        try {
            statement = connection.createStatement();
            if(!statement.execute("delete from user_info where username='" + name + "'"))
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void disconnectFromDB() {
//        if ( connectedToDatabase ){
//            // close Statement and Connection
//            try{
////                if (Math.random()>0.5)
////                    throw new SQLException( "Error in disconnecting DB" );
////            }catch ( SQLException sqlException ){
////                sqlException.printStackTrace();
//            }finally{
//                connectedToDatabase = false;
//            }
//        }

        try {

            connectedToDatabase = false;
            resultset.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

    }

}
