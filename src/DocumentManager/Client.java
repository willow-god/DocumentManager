package DocumentManager;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;

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

    static void UpdateUser(String name,String password,String role,JFrame frame) throws IOException {
        try {
            DataProcessing.update(name,password,role);
        } catch (SQLException e) {
            System.out.println("Client|update出问题啦！！！");
        }
        jFrame = frame;
    }

    static void DelUser(String del_name,JFrame frame) throws IOException{
        try {
            DataProcessing.deleteUser(del_name);
        } catch (SQLException e) {
            System.out.println("Client|delete出问题啦！！！");
        }
        jFrame = frame;
    }

    static void AddUser(String name, String password,String role,JFrame frame) throws IOException{
        try {
            DataProcessing.insertUser(name,password,role);
        } catch (SQLException e) {
            System.out.println("Client|Add出问题啦！！！");
        }
    }

    static void Upload(String ID,String Creator,
                       String description,String filename/*文件位置*/,
                       JFrame frame) throws IOException, SQLException {
        jFrame = frame;
        String uploadpath = "D:\\DownLoad\\javatestdocument\\uploadfile\\";
        File srcfile=new File(filename.trim());
        String Filename = srcfile.getName();
        File destFile = new File(uploadpath + Filename);
        destFile.createNewFile();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DataProcessing.insertDoc(ID,Creator,timestamp,description,filename);

        FileInputStream fis = new FileInputStream(srcfile);
        FileOutputStream fos = new FileOutputStream(destFile);

        byte[] buf = new byte[1024];
        int len = 0;
        while((len = fis.read(buf))!=-1) {
            fos.write(buf,0,len);
        }
        fis.close();
        fos.close();
    }

    static void Download(String ID,JFrame frame) throws IOException {
        jFrame = frame;
    }

    static void ChangeSelfInfo(String old_password,String new_password,String new_password2) throws IOException, SQLException {
        if (user_password.equals(old_password)) {
            if (new_password.equals(new_password2)){
                DataProcessing.update(user_name,new_password,user_role);
                JOptionPane.showMessageDialog(null,"修改成功！！！","提示",JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,"两次密码不一致！！！","提示",JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null,"老密码输错了！！！","提示",JOptionPane.ERROR_MESSAGE);
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
