package DocumentManager;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        String name;
        String password;

        String tip_system = "档案系统";
        String tip_menu = "请选择菜单：";
        String tip_exit = "系统退出，谢谢使用！";
        String infos = "******欢迎进入" +
                tip_system +
                "******\n\t" +
                "1.登录\n\t" +
                "2.退出\n" +
                "*************************";

        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println(infos);
            System.out.print(tip_menu);

            int option = in.nextInt();
            switch(option){
                case 1:
                    System.out.print("请输入用户名:");
                    name = in.next();

                    System.out.print("请输入密码:");
                    password = in.next();

                    User user  = DataProcessing.search(name, password);
                    if(user == null){
                        System.out.println("请重新输入");
                    }
                    else{
                        user.showMenu();
                    }
                    break;
                case 2:
                    System.out.println(tip_exit);
                    System.exit(0);
                    break;
            }
        }
    }
}
