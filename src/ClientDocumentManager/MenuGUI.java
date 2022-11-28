package ClientDocumentManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MenuGUI {
    public static void playMenu() {
        JFrame menuframe = new JFrame("用户管理菜单");
        JMenuBar menuBar = new JMenuBar();
        menuframe.setJMenuBar(menuBar);

        JMenu userMenu = new JMenu("用户管理");
        JMenu fileMenu = new JMenu("档案管理");
        JMenu personMessageMenu = new JMenu("个人信息管理");
        menuBar.add(userMenu);
        menuBar.add(fileMenu);
        menuBar.add(personMessageMenu);

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();      //获得屏幕大小
        menuframe.setSize(dimension.width/3,dimension.height/3);
        menuframe.setLocationRelativeTo(null);

        //管理菜单
        userMenu.add(new JMenuItem("修改用户"));
        userMenu.add(new JMenuItem("删除用户"));
        userMenu.add(new JMenuItem("添加用户"));

        fileMenu.add(new JMenuItem("档案上传"));
        fileMenu.add(new JMenuItem("档案下载"));

        personMessageMenu.add(new JMenuItem("信息修改"));

        menuframe.setVisible(true);
        menuframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        String role = Client.get_Role();
        //String name = Client.get_Name();
        switch (role) {
            case "administrator" -> {
                menuframe.setTitle("系统管理员界面");
                fileMenu.getItem(0).setEnabled(false);
            }
            case "browser" -> {
                menuframe.setTitle("档案浏览员界面");
                userMenu.getItem(0).setEnabled(false);
                userMenu.getItem(1).setEnabled(false);
                userMenu.getItem(2).setEnabled(false);
            }
            case "operator" -> {
                menuframe.setTitle("档案录入员界面");
                userMenu.getItem(0).setEnabled(false);
                userMenu.getItem(1).setEnabled(false);
                userMenu.getItem(2).setEnabled(false);
            }
            default -> {
            }
        }

        userMenu.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 0;
                try {
                    UserGUI.playUserManagerGUI(index);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        userMenu.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 1;
                try {
                    UserGUI.playUserManagerGUI(index);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        userMenu.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 2;
                try {
                    UserGUI.playUserManagerGUI(index);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        fileMenu.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 0;
                //文件
                FileManageGUI.playArchivesManageGUI(index);
            }
        });

        fileMenu.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = 1;
                //文件
                FileManageGUI.playArchivesManageGUI(index);
            }
        });

        personMessageMenu.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PersonMessageGUI.playPersonMessageManageGUI();
            }
        });
    }
}
