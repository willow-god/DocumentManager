package DocumentManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Scanner;

public class Operator extends User{
    Operator(String name,String password,String role){
        super(name,password,role);
    }

    public void uploadFile(String ID,String name,String filepath,String description) throws IOException, SQLException {

//        Scanner in = new Scanner(System.in);
        File srcFile = new File(filepath);
        String filename = srcFile.getName();
        File destFile = new File(uploadpath + filename);
        destFile.createNewFile();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        DataProcessing.insertDoc(ID,name,timestamp,description,filename);

        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);

        byte[] buf = new byte[1024];
        int len = 0;
        while((len = fis.read(buf))!=-1) {
            fos.write(buf,0,len);
        }
        fis.close();
        fos.close();
        System.out.println("上传成功！");
    }

    @Override
    public void showMenu(){
        String tip_operator =
                "******欢迎来到档案录入菜单******\n\t" +
                "1.上传文件\n\t" +
                "2.下载文件\n\t" +
                "3.文件列表\n\t" +
                "4.修改密码\n\t" +
                "5.退出\n" +
                "****************************";
        String tip_menu = "请选择菜单：";
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println(tip_operator);
            System.out.print(tip_menu);
            int option;
            option = in.nextInt();
            switch (option) {
                case 1:
                    System.out.println("上传文件：");
                    System.out.print("用户名："+this.getName());
                    String name;
                    name = this.getName();
                    System.out.print("\n请输入档案号：");
                    String ID;
                    ID = in.next();
                    System.out.print("请输入文件路径：");
                    String filepath;
                    filepath = in.next();
                    System.out.print("请输入文件描述：");
                    String description;
                    description = in.next();
                    try {
                        uploadFile(ID,name,filepath,description);
                    } catch (SQLException e) {
                        System.out.println("数据库错误"+e.getMessage());
                    } catch (IOException e) {
                        System.out.println("文件错误");
                    }
                    break;
                case 2:
                    System.out.println("下载文件：");
                    System.out.print("请输入档案号：");
                    String filename;
                    filename = in.next();
                    try {
                        downloadFile(filename);
                    } catch (IOException e) {
                        System.out.println("文件错误");
                    } catch (SQLException e) {
                        System.out.println("数据库错误" + e.getMessage());
                    }
                    break;
                case 3:
                    try {
                        showFileList();
                    } catch (SQLException e) {
                        System.out.println("数据库错误" + e.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("修改密码");
                    System.out.println("请输入新密码:");
                    String password;
                    password = in.next();
                    try {
                        changeSelfInfo(password);
                    } catch (SQLException e) {
                        System.out.println("数据库错误" + e.getMessage());
                    }
                    break;
                case 5:
                    exitSystem();
                    break;
            }
        }
    }
}