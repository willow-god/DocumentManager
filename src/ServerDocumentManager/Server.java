package ServerDocumentManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Objects;
import javax.swing.SwingUtilities;

public class Server {
//    private Socket connection;
//    private int counter = 1;
    static ServerSocket server;
    static User user;

    public Server() throws IOException{
        InetAddress liushen = InetAddress.getLocalHost();
        String s = liushen.getHostAddress();
        System.out.println("地址为："+s);
        server = new ServerSocket( 12345, 100 );
        int count=1;
        while(true){
            displayMessage( "正在等待链接。。。\n" );
            Socket connection = server.accept(); // allow server to accept connection
            displayMessage( "连接 " + count + " 已经从此处建立连接：" +
                    connection.getInetAddress().getHostName() );
               new ServerThread(connection,"Thread"+count++);//直接实现了多线程
        }
    }

    class ServerThread extends Thread{
        Socket connection;
        String clientname;
        DataOutputStream output;
        DataInputStream input;
        ServerThread(Socket con,String clientname) throws IOException{
            connection=con;
            this.clientname=clientname;
            output = new DataOutputStream( connection.getOutputStream() );
            output.flush();
            input = new DataInputStream( connection.getInputStream() );
            displayMessage( "\nGot I/O streams\n" );
            start();
        }

        private void sendData( String message )
        {
            try
            {
                output.writeUTF( "SERVER>>> " + message );
                output.flush();
                displayMessage( "\nSERVER>>> " + message );
            }
            catch ( IOException ioException )
            {
                System.out.println( "\nError writing object" );
            }
        }

        public void run() {
            String message = "Connection successful";
            sendData( message );
            try {
                do {
                    message = input.readUTF();
                    switch (message) {
                        case "CLIENT>>> CLIENT_LOGIN" -> {//这条信息就是传递的参数，我们需要得到这条信息在服务端，然后服务端将数据发出
                            String name = input.readUTF();
                            String password = input.readUTF();
                            if (DataProcessing.search(name, password) != null) {
                                output.writeUTF("LOGIN_TRUE");
                                output.flush();
                                String role = DataProcessing.search(name, password).getRole();
                                output.writeUTF(role);
                                output.flush();
                                System.out.println(message);
                                System.out.println(name);
                                System.out.println(role);
                                System.out.println("SERVER>>> SERVER_LOGIN");

                            } else {
                                output.writeUTF("LOGIN_FALSE");
                                output.flush();
                            }
                            break;
                        }
                        case "CLIENT>>> CLIENT_SELF_MOD" -> {
                            String name = input.readUTF();
                            String password = input.readUTF();
                            String role = input.readUTF();
                            System.out.println("CLIENT_SELF_MOD");
                            if (DataProcessing.update(name, password, role)) {
                                output.writeUTF("SELFCHANGE_TRUE");
                                output.flush();
                                output.writeUTF(password);
                                output.flush();
                                System.out.println("SERVER>>> SERVER_SELF_MOD");
                            } else {
                                output.writeUTF("SELFCHANGE_FALSE");
                                output.flush();
                            }

                            break;
                        }
                        case "displayUser" -> {
                            Enumeration<User> e = DataProcessing.getAllUser();
                            String[][] rowData = new String[50][3];
                            User user;
                            int i = 0;
                            while (e.hasMoreElements()) {
                                user = e.nextElement();
                                rowData[i][0] = user.getName();
                                rowData[i][1] = user.getPassword();
                                rowData[i][2] = user.getRole();
                                i++;
                            }
                            output.writeUTF("displayedUser");
                            output.flush();
                            output.writeInt(i);
                            output.flush();
                            for (int j = 0; j < i; j++) {
                                output.writeUTF(rowData[j][0]);
                                output.flush();
                                output.writeUTF(rowData[j][1]);
                                output.flush();
                                output.writeUTF(rowData[j][2]);
                                output.flush();
                            }

                            break;
                        }
                        case "displayDoc" -> {
                            Enumeration<Doc> e = DataProcessing.getAllDocs();
                            String[][] rowData = new String[50][5];
                            Doc doc;
                            int i = 0;
                            while (e.hasMoreElements()) {
                                doc = e.nextElement();
                                rowData[i][0] = doc.getID();
                                rowData[i][1] = doc.getCreator();
                                rowData[i][2] = doc.getTimestamp().toString();
                                rowData[i][3] = doc.getDescription();
                                rowData[i][4] = doc.getFilename();
                                i++;
                            }
                            output.writeUTF("displayedDoc");
                            output.flush();
                            output.writeInt(i);
                            output.flush();
                            for (int j = 0; j < i; j++) {
                                output.writeUTF(rowData[j][0]);
                                output.flush();
                                output.writeUTF(rowData[j][1]);
                                output.flush();
                                output.writeUTF(rowData[j][2]);
                                output.flush();
                                if (rowData[j][3] != null) {
                                    output.writeUTF(rowData[j][3]);
                                    output.flush();
                                }
                                output.writeUTF(rowData[j][4]);
                                output.flush();
                            }
                            break;
                        }
                        case "USER_DELETE" -> {
                            String name = input.readUTF();
                            if (DataProcessing.deleteUser(name)) {

                                output.writeUTF("DELETE_TRUE");
                                output.flush();
                                System.out.println("SERVER>>> " + name + " USER_DELETE");
                            } else {
                                output.writeUTF("DELETE_FALSE");
                                output.flush();
                            }

                            break;
                        }
                        case "USER_ADD" -> {
                            String name = input.readUTF();
                            String password = input.readUTF();
                            String role = input.readUTF();
                            if (DataProcessing.insertUser(name, password, role)) {
                                output.writeUTF("ADD_TRUE");
                                output.flush();
                                System.out.println("SERVER>>> " + name + " USER_ADD");
                            } else {
                                output.writeUTF("ADD_FALSE");
                                output.flush();
                            }

                            break;
                        }
                        case "USER_UPDATE" -> {
                            String name = input.readUTF();
                            String password = input.readUTF();
                            String role = input.readUTF();
                            if (DataProcessing.update(name, password, role)) {
                                output.writeUTF("UPDATE_TRUE");
                                output.flush();
                                System.out.println("SERVER>>> " + name + " USER_UPDATE");
                            } else {
                                output.writeUTF("UPDATE_FALSE");
                                output.flush();
                            }

                            break;
                        }
                        case "UPLOAD" -> {
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                            String ID = input.readUTF();
                            String Creator = input.readUTF();
                            String description = input.readUTF();
                            String filename = input.readUTF();
                            long fileLength = input.readLong();
                            FileOutputStream fos = new FileOutputStream(new File("D:\\DownLoad\\javatestdocument\\" + filename));
                            DataInputStream dis = new DataInputStream(connection.getInputStream());
                            byte[] sendBytes = new byte[1024];
                            int transLen = 0;
                            System.out.println("----开始接收文件<" + filename + ">,文件大小为<" + fileLength + ">----");
                            while (true) {
                                int read = 0;
                                read = input.read(sendBytes, 0, sendBytes.length);
                                if (read <= 0) break;
                                transLen += read;
                                System.out.println("接收文件进度" + 100 * transLen * 1.0 / fileLength + "%...");
                                fos.write(sendBytes, 0, read);
                                fos.flush();
                                if (transLen >= fileLength) break;
                            }
                            System.out.println("----接收文件<" + filename + ">成功----");
                            if (DataProcessing.insertDoc(ID, Creator, timestamp, description, filename)) {
                                output.writeUTF("UPLOAD_TRUE");
                                output.flush();
                                System.out.println("SERVER>>> CLIENT_FILE_UP");
                            } else {
                                output.writeUTF("UPLOAD_FALSE");
                                output.flush();
                            }

                            break;
                        }
                        case "DOWNLOAD" -> {
                            String ID = input.readUTF();
                            output.writeUTF("SERVER>>> CLIENT_FILE_DOWN");
                            output.flush();
                            System.out.println("SERVER>>> CLIENT_FILE_DOWN");
                            String filename = Objects.requireNonNull(DataProcessing.searchDoc(ID)).getFilename();
                            output.writeUTF(filename);
                            output.flush();
                            String filepath = "D:\\DownLoad\\javatestdocument\\";
                            File file = new File(filepath + filename);
                            long fileLength = file.length();
                            output.writeLong(fileLength);
                            output.flush();
                            FileInputStream fis = new FileInputStream(file);
                            byte[] sendBytes = new byte[1024];
                            int length = 0;
                            while ((length = fis.read(sendBytes, 0, sendBytes.length)) > 0) {
                                output.write(sendBytes, 0, length);
                                output.flush();
                            }
                            break;
                        }
                        default -> displayMessage(message);
                    }
                } while ( !message.equals( "CLIENT>>> TERMINATE" ) );

            }catch(IOException e) {
                System.out.println("Server.java//275line//报错了");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }



//   void waitForConnection() throws IOException
//   {
//      displayMessage( "Waiting for connection\n" );
//
//      displayMessage( "Connection " + counter + " received from: " +
//      connection.getInetAddress().getHostName() );
//   }


//   private void getStreams() throws IOException
//   {
//
//      output = new DataOutputStream( connection.getOutputStream() );
//      output.flush();
//
//
//      input = new DataInputStream( connection.getInputStream() );
//
//      displayMessage( "\nGot I/O streams\n" );
//   }





//   private void closeConnection()
//   {
//      displayMessage( "\nTerminating connection\n" );
//
//      try
//      {
//         output.close();
//         input.close();
//         connection.close();
//      }
//      catch ( IOException ioException )
//      {
//         ioException.printStackTrace();
//      }
//   }




    void displayMessage( String messageToDisplay )
    {
        SwingUtilities.invokeLater(
                () -> System.out.println( messageToDisplay )
        );
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
    }
}
