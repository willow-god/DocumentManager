package DocumentManager;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class Client extends JFrame {
    static JFrame jFrame;
    static DataOutputStream output;
    static DataInputStream input;
    static String message = "";
    static Socket client;
//    打开，读写，关闭就是Socket的实现，一切皆可Socket
//    是一种特殊的文件
//    简单点来说，就是将web服务器和浏览器进行相连的一个接口
//    它可以进行本地的链接，也可以进行网络host域名链接
    static String user_name;
    static String user_password;
    static String user_role;
    static String[][] UserData;
    static String[][] DocData;
    static int row1;
    static int row2;

    public Client(String host) {
        super("Client");
    }

    public void runClient() throws IOException {

    }

    private void connectToSerever() throws IOException {

    }

    private void getStreams() throws IOException {
    }

    private void processConnection() throws IOException {

    }

    static void Login(String name,String password,JFrame frame) throws IOException {
//        String login = "CLIENT>>> CLIENT_LOGIN";
//        output.writeUTF(login);
//        System.out.println(login);
//        output.flush();

//        output.writeUTF(name);
        user_name = name;
//        output.flush();

//        output.writeUTF(password);
        user_password = password;
//        output.flush();

//        jFrame = frame;

        //下面是我添加的东西，不知道对不对
        try {
            User user = DataProcessing.search(user_name,user_password);
            assert user != null;
            user_role = user.getRole();
        } catch (SQLException e) {
            System.out.println("Client中查找个人角色不对");
        }


    }

    static int get_Rows() {
        return row1;
    }
    static int get_Rows2() {
        return row2;
    }
    static String[][] get_Docs(){
        return DocData;
    }
    static String[][] get_Users(){
        return UserData;
    }
    static String get_Name() {
        return user_name;
    }
    static String get_Role() {
        return user_role;
    }
}
