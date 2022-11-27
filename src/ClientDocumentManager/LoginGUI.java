package ClientDocumentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginGUI {
    public static void systemLogin() {
        JFrame loginframe = new JFrame("WUT·档案管理系统");
        loginframe.setSize(300,220);
        loginframe.setLayout(new GridLayout(5,1,5,5));

        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();

        //用户填写用户名的组件
        JLabel nameLabel = new JLabel("用户:  ");
        JTextField nameFiled = new JTextField(13);
        jp1.add(nameLabel);
        jp1.add(nameFiled);

        //用户填写密码的组件
        JLabel passwordLabel = new JLabel("密码:  ");
        JPasswordField passwordField = new JPasswordField(13);
        jp2.add(passwordLabel);
        jp2.add(passwordField);

        //登录，取消按钮
        JButton loginButton = new JButton("登录");
        JButton cancelButton = new JButton("取消");
        jp3.add(loginButton);
        jp3.add(cancelButton);

        //网上说设置五个，防止顶格，但是感觉这玩意就是多此一举，老子setBound照样不顶
        loginframe.add(new JPanel());
        loginframe.add(jp1);
        loginframe.add(jp2);
        loginframe.add(jp3);
        loginframe.add(new JPanel());

        //设置可见性，位置，是否支持关闭
        loginframe.setVisible(true);
        loginframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loginframe.setLocationRelativeTo(null);

        //登录按钮事件监视器
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginName = nameFiled.getText();
                String loginPassword = String.valueOf(passwordField.getPassword());
                try {
                    Client.Login(loginName,loginPassword,loginframe);
                } catch (IOException e1) {
                    loginframe.dispose();
                    systemLogin();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginframe.dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) throws Exception{
        Client application;
        systemLogin();
        if (args.length == 0)
            application = new Client("127.0.0.1");
        else
            application = new Client(args[ 0 ]);
        application.runClient();
    }
}
