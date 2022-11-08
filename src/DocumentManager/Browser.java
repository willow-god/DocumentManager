package DocumentManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Browser extends User{
    public Browser(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu(){
        String tip_browser =
                "******欢迎来到档案浏览菜单******\n\t" +
                        "1.下载文件\n\t" +
                        "2.文件列表\n\t" +
                        "3.修改密码\n\t" +
                        "4.退出\n" +
                        "****************************";
        String tip_menu = "请选择菜单：";
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println(tip_browser);
            System.out.print(tip_menu);

            int option;
            option = in.nextInt();

            switch(option) {
                case 1:
                    System.out.println("下载文件");
                    System.out.print("请输入档案号:");
                    String filename;
                    filename = in.next();
                    try {
                        downloadFile(filename);
                    } catch (IOException e) {
                        System.out.println("文件错误" + e.getMessage());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    System.out.println("文件列表如下：\n嗷嗷嗷");
                    try {
                        showFileList();
                    } catch (SQLException e) {
                        System.out.println("数据库错误" + e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("修改密码");
                    System.out.print("请输入新密码:");
                    String password;
                    password = in.next();
                    try {
                        changeSelfInfo(password);
                    } catch (SQLException e) {
                        System.out.println("数据库错误" + e.getMessage()
                        );
                    }
                    break;
                case 4:
                    exitSystem();
                    break;
            }
        }
    }
}
