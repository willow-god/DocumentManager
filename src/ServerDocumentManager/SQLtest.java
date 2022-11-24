package ServerDocumentManager;
import java.sql.*;

public class SQLtest {


    public static void main(String[] args) {
        Connection connection;
        Statement statement;
        ResultSet resultSet;
        String driverName="com.mysql.cj.jdbc.Driver";               // 没有驱动怎么办
        String url="jdbc:mysql://localhost:3306/users_informations";       // 本地URL
        String user="root";                                      //设置账户
        String password="293617";
        try{
            Class.forName(driverName);
            connection=DriverManager.getConnection(url, user, password);   // �������ݿ�����
            statement = connection.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY );
            String sql="select * from user_info";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String username=resultSet.getString("username");
                String pwd=resultSet.getString("password");
                String role=resultSet.getString("role");
                System.out.println(username+";"+pwd+";"+role);
            }
            resultSet.close();
            statement.close();
            connection.close();
        }catch(ClassNotFoundException e ){
            System.out.println("第一个报错");
        }catch(SQLException e){
            System.out.println("第二个报错");
        }
    }

}

