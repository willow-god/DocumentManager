package ClientDocumentManager;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class Client extends JFrame {
    static JFrame jframe;
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
        try{
            connectToSerever();
            getStreams();
            processConnection();
        } catch(EOFException eofException) {
            displayMessage("\nClient termined connection");
        } catch(IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void connectToSerever() throws IOException {
        displayMessage( "Attempting connection\n" );
        client = new Socket( "127.0.0.1", 12340 );
        displayMessage( "Connected to: " + client.getInetAddress().getHostName() );
    }

    private void getStreams() throws IOException {
        output = new DataOutputStream( client.getOutputStream() );
        output.flush();

        input = new DataInputStream( client.getInputStream() );
        displayMessage( "\nGot I/O streams\n" );
    }

    private void processConnection() throws IOException {
        do
        {
            message = ( String ) input.readUTF();
            if(message.equals("LOGIN_TRUE")) {
                user_role = input.readUTF();
                MenuGUI menu = new MenuGUI();
                menu.playMenu();
                jframe.dispose();
            }
            else if(message.equals("LOGIN_FALSE")) {

                JOptionPane.showMessageDialog(null, "账号或密码错误","提示",JOptionPane.ERROR_MESSAGE);

            }
            else if(message.equals("SELFCHANGE_TRUE")) {

                JOptionPane.showMessageDialog(null, "修改成功","提示",JOptionPane.ERROR_MESSAGE);
                System.out.println("SELFCHANGE_SUCCESS");

            }
            else if(message.equals("SELFCHANGE_FALSE")) {

                JOptionPane.showMessageDialog(null, "修改失败","提示",JOptionPane.ERROR_MESSAGE);

            }
            else if(message.equals("displayedUser")) {

                int i = input.readInt();
                UserData = new String[50][3];
                for(int j=0; j<i; i++) {
                    UserData[j][0] = input.readUTF();
                    UserData[j][1] = input.readUTF();
                    UserData[j][2] = input.readUTF();

                }
                row1 = i;

            }
            else if(message.equals("diaplayedDoc")) {

                int i = input.readInt();
                DocData = new String[50][5];
                for(int j=0; j<i; j++) {
                    DocData[j][0] = input.readUTF();
                    DocData[j][1] = input.readUTF();
                    DocData[j][2] = input.readUTF();
                    DocData[j][3] = input.readUTF();
                    DocData[j][4] = input.readUTF();
                }
                row2 = i;
            }
            else if(message.equals("DELETE_TRUE")) {

                JOptionPane.showMessageDialog(null, "删除成功","提示",JOptionPane.ERROR_MESSAGE);
                jframe.dispose();
                System.out.println("DELETE_SUCCESS");

            }
            else if(message.equals("DELETE_FALSE")) {

                JOptionPane.showMessageDialog(null, "账号错误","提示",JOptionPane.ERROR_MESSAGE);

            }
            else if(message.equals("ADD_TRUE")) {

                JOptionPane.showMessageDialog(null, "添加成功","提示",JOptionPane.ERROR_MESSAGE);
                jframe.dispose();
                System.out.println("ADD_SUCCESS");

            }
            else if(message.equals("ADD_FALSE")) {

                JOptionPane.showMessageDialog(null, "添加失败","提示",JOptionPane.ERROR_MESSAGE);

            }
            else if(message.equals("UPDATE_TRUE")) {

                JOptionPane.showMessageDialog(null, "修改成功","提示",JOptionPane.ERROR_MESSAGE);
                jframe.dispose();
                System.out.print("UPDATE_SUCCESS");

            }
            else if(message.equals("UPDATE_FALSE")) {

                JOptionPane.showMessageDialog(null, "修改失败","提示",JOptionPane.ERROR_MESSAGE);

            }
            else if(message.equals("UPLOAD_TRUE")) {

                JOptionPane.showMessageDialog(null, "上传成功","提示",JOptionPane.ERROR_MESSAGE);
                jframe.dispose();
                System.out.println("UPLOAD_SUCCESS");

            }
            else if(message.equals("UPLOAD_FALSE")) {

                JOptionPane.showMessageDialog(null, "上传失败","提示",JOptionPane.ERROR_MESSAGE);

            }
            else if(message.equals("SERVER>>> CLIENT_FILE_DOWN")) {

                String filename = input.readUTF();
                long fileLength = input.readLong();
                FileOutputStream fos = new FileOutputStream("/Users/air/Documents/java/downloadfile/"+filename);

                byte[] sendBytes = new byte[1024];
                int length = 0;
                System.out.println("----开始下载文件<"+filename+">,文件大小为<"+length+">----");
                while(true) {
                    int read = 0;
                    read = input.read(sendBytes);
                    if(read == -1) break;
                    length += read;
                    System.out.println("下载文件进度"+ 100 * length * 1.0 / fileLength + "%...");

                    fos.write(sendBytes,0,read);
                    fos.flush();
                    if(length >= fileLength) break;

                }
                System.out.println("----下载文件<" + filename + ">成功----");
                JOptionPane.showMessageDialog(null, "下载成功","提示",JOptionPane.ERROR_MESSAGE);
                jframe.dispose();
            }

        } while ( !message.equals( "SERVER>>> TERMINATE" ) );
    }

    static void closeConnection() throws IOException
    {
        displayMessage( "\nClosing connection" );
        String logout = "CLIENT>>> CLIENT_LOGOUT";
        output.writeUTF(logout);
        output.flush();
        System.out.println("CLIENT>>> CLIENT_LOGOUT");

        try {
            output.close();
            input.close();
            client.close();
        } catch ( IOException ioException ) {
            ioException.printStackTrace();
        }
    }

    static void sendData( String message )
    {
        try
        {
            output.writeUTF( "CLIENT>>> " + message );
            output.flush();
            displayMessage( "\nCLIENT>>> " + message );
        }
        catch ( IOException ioException )
        {
            System.out.println( "\nError writing object" );
        }
    }

    static void displayMessage( String messageToDisplay )
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
                    {
                        System.out.println( messageToDisplay );
                    }
                }
        );
    }

    static void Login(String name,String password,JFrame frame) throws IOException {
////        String login = "CLIENT>>> CLIENT_LOGIN";
////        output.writeUTF(login);
////        System.out.println(login);
////        output.flush();
//
////        output.writeUTF(name);
//        user_name = name;
////        output.flush();
//
////        output.writeUTF(password);
//        user_password = password;
////        output.flush();
//
////        jFrame = frame;
//
//        //下面是我添加的东西，不知道对不对
//        User user = DataProcessing.search(user_name,user_password);
//        if (user != null)
//            user_role = user.getRole();
//        else {
//            JOptionPane.showMessageDialog(null,
//                    "密码和用户名不匹配，请重新输入~",
//                    "提示",
//                    JOptionPane.ERROR_MESSAGE);
//            throw new IOException();
//        }
        String login="CLIENT>>> CLIENT_LOGIN";
        output.writeUTF(login);
        System.out.println(login);
        output.flush();
        output.writeUTF(name);
        user_name = name;
        output.flush();
        output.writeUTF(password);
        user_password = password;output.flush();
        jframe = frame;
    }

    static void UpdateUser(String name,String password,String role,JFrame frame) throws IOException {
//        DataProcessing.update(name,password,role);
//        jFrame = frame;

        jframe = frame;
        output.writeUTF("USER_UPDATE");
        output.flush();
        output.writeUTF(name);
        output.flush();
        output.writeUTF(password);
        output.flush();
        output.writeUTF(role);
        output.flush();
        System.out.println("CLIENT>>> "+name+ "USER_UPDATE");
    }

    static void DelUser(String del_name,JFrame frame) throws IOException{
//        DataProcessing.deleteUser(del_name);
//        jFrame = frame;

        jframe = frame;
        if(del_name.equals(user_name)) {
            JOptionPane.showMessageDialog(null, "删除失败", "提示", JOptionPane.ERROR_MESSAGE);
        }
        else {
            output.writeUTF("USER_DELETE");
            output.flush();
            output.writeUTF(del_name);
            output.flush();
            System.out.println("CLIENT>>> " + del_name + "USER_DELETE");
        }
    }

    static void AddUser(String name, String password,String role,JFrame frame) throws IOException{
//        DataProcessing.insertUser(name,password,role);
        jframe = frame;
        output.writeUTF("USER_ADD");
        output.flush();
        output.writeUTF(name);
        output.flush();
        output.writeUTF(password);
        output.flush();
        output.writeUTF(role);
        output.flush();
        System.out.println("CLIENT>>> "+name+ "USER_ADD");
    }

    static void Upload(String ID,String Creator,
                       String description,String filename/*文件位置*/,
                       JFrame frame) throws IOException, SQLException {
//        jFrame = frame;
//        String uploadpath = "D:\\DownLoad\\javatestdocument\\uploadfile\\";
//        File srcfile=new File(filename.trim());
//        String Filename = srcfile.getName();
//        File destFile = new File(uploadpath + Filename);
//        destFile.createNewFile();
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        DataProcessing.insertDoc(ID,Creator,timestamp,description,Filename);
//
//        FileInputStream fis = new FileInputStream(srcfile);
//        FileOutputStream fos = new FileOutputStream(destFile);
//
//        byte[] buf = new byte[1024];
//        int len = 0;
//        while((len = fis.read(buf))!=-1) {
//            fos.write(buf,0,len);
//        }
//        fis.close();
//        fos.close();

        jframe=frame;
        output.writeUTF("UPLOAD");
        output.flush();
        output.writeUTF(ID);
        output.flush();
        output.writeUTF(Creator);
        output.flush();
        output.writeUTF(description);
        output.flush();
        File file=new File(filename.trim());
        String fileName=file.getName();
        output.writeUTF(fileName);
        output.flush();
        long fileLength=file.length();
        output.writeLong(fileLength);
        output.flush();
        FileInputStream fis=new FileInputStream(file);
        DataOutputStream dos=new DataOutputStream(client.getOutputStream());
        byte[] sendBytes=new byte[1024];
        int length=0;
        while((length=fis.read(sendBytes,0,sendBytes.length))>0) {
            output.write(sendBytes,0,length);
            output.flush();
        }
        System.out.println("CLIENT>>> CLIENT_FILE_UP");
    }

    static void Download(String ID,JFrame frame) throws IOException {
//        jFrame = frame;
        jframe=frame;
        output.writeUTF("DOWNLOAD");
        output.flush();
        output.writeUTF(ID);
        output.flush();
    }

    static void ChangeSelfInfo(String old_password,String new_password,String new_password2) throws IOException, SQLException {
//        if (user_password.equals(old_password)) {
//            if (new_password.equals(new_password2)){
//                DataProcessing.update(user_name,new_password,user_role);
//                JOptionPane.showMessageDialog(null,"修改成功！！！","提示",JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                JOptionPane.showMessageDialog(null,"两次密码不一致！！！","提示",JOptionPane.ERROR_MESSAGE);
//            }
//        } else {
//            JOptionPane.showMessageDialog(null,"老密码输错了！！！","提示",JOptionPane.ERROR_MESSAGE);
//        }
        if(user_password.equals(old_password)) {
            if(new_password.equals(new_password2)) {
                String changeSelfInfo="CLIENT>>> CLIENT_SELF_MOD";
                System.out.println("CLIENT>>> CLIENT_SELF_MOD");
                output.writeUTF(changeSelfInfo);
                output.flush();
                output.writeUTF(user_name);
                output.flush();
                output.writeUTF(new_password);
                output.flush();
                output.writeUTF(user_role);
                output.flush();
            }
            else {
                JOptionPane.showMessageDialog(null, "两次输入的新密码不一致", "提示", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "密码错误", "提示", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void Display_user() throws IOException {
        output.writeUTF("displayUser");
        output.flush();
    }

    static void Display_Doc() throws IOException {
        output.writeUTF("displayDoc");
        output.flush();
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
