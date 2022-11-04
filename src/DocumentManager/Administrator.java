package DocumentManager;

import java.util.Scanner;

public class Administrator extends User{
    public Administrator(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu(){
        String tip_administrator =
                "******欢迎来到系统管理菜单******\n\t" +
                        "1.修改用户\n\t" +
                        "2.删除用户\n\t" +
                        "3.新增用户\n\t" +
                        "4.列出用户\n\t" +
                        "5.下载文件\n\t" +
                        "6.文件列表\n\t" +
                        "7.修改（本人）密码\n\t" +
                        "8.退出\n" +
                        "***************************";
        String tip_menu = "请选择菜单：";
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.println(tip_administrator);
            System.out.print(tip_menu);
            int option;
            option = in.nextInt();
            switch (option) {
                case 1:
                    System.out.println("修改用户");
                    System.out.println("请输入用户名：");
                    String name1,password,role;
                    name1 = in.next();
                    System.out.println("请输入密码:");
                    password = in.next();
                    System.out.println("请输入角色:");
                    role = in.next();
                    break;
                case 2:
                    System.out.println("删除用户");
                    System.out.println("请输入用户名:");
                    String name2;
                    name2 = in.next();
                    break;
                case 3:
                    System.out.println("新增用户");
                    String name3,password1,role1;
                    System.out.println("请输入用户名:");
                    name3 = in.next();
                    System.out.println("请输入密码:");
                    password1 = in.next();
                    System.out.println("请输入角色:");
                    role1 = in.next();
                    break;
                case 4:
                    System.out.println("列出用户:");
                    break;
                case 5:
                    System.out.println("下载文件");
                    System.out.println("请输入档案号:");
                    String filename;
                    filename = in.next();
                    break;
                case 6:
                    System.out.println("文件列表如下：");
                    break;
                case 7:
                    System.out.println("修改(本人)密码");
                    System.out.println("请输入新口令:");
                    String password2;
                    password2 = in.next();
                    System.out.println("修改成功！");
                case 8:
                    exitSystem();
                    break;
            }
        }
    }
}
