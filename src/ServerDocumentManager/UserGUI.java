package ServerDocumentManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Enumeration;

public class UserGUI {
    //定义一堆玩意，具体功能自己看
    static JFrame userManageFrame = new JFrame("用户管理页面");
    static JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
//    static int screenWidth;
//    static int screenHeight;
//    static int frameWidth;
//    static int frameHeight;
    static JPanel modifyPanel = new JPanel();
    static JPanel deletePanel = new JPanel();
    static JPanel addPanel = new JPanel();
    static JLabel nameLabel = new JLabel("用户:   ");
    static JLabel roleLabel = new JLabel("属性:   ");
    static JLabel passwordLabel = new JLabel("密码:   ");
    static String []usersName = new String[100];
    static String []usersRole = new String[100];
    static JTextField nameField = new JTextField(13);
    static JPasswordField passwordField = new JPasswordField(13);
    static JComboBox<String> nameComboBox;
    static JComboBox<String> roleComboBox;
    static DefaultTableModel tableModel;
    static JTable table;

    public static void playUserManagerGUI(int index) throws SQLException {
        //设置大小和位置
        userManageFrame.setSize(300,400);
        userManageFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        userManageFrame.setLocationRelativeTo(null);

        //搞基础选择栏目
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        modifyPanel = new JPanel();
        deletePanel = new JPanel();
        addPanel = new JPanel();
        tabbedPane.addTab("修改用户",modifyPanel);
        tabbedPane.addTab("删除用户",deletePanel);
        tabbedPane.addTab("添加用户",addPanel);

        tabbedPane.setSelectedIndex(index);//设置选中框默认选项

        nameField = new JTextField(13);//输入框最多可以显示的行数
        passwordField = new JPasswordField(13);
        nameField.setText(null);//设置默认文字，没啥卵用，不写都行
        passwordField.setText(null);

        userManageFrame.add(tabbedPane, BorderLayout.CENTER);//边框布局，有东西南北中五个玩意，咱把他放在中间了
        userManageFrame.setVisible(true);
        userManageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //注意到窗口关闭方式变化，如果是EXIT...会让所有页面都关闭，这里只想让他关闭一个页面

        if (index == 0) {
            modifyPanel.removeAll();//清空组件，英文应该可以看懂，全部玩意！
            deletePanel.removeAll();
            addPanel.removeAll();
            getModifyUserGUI();
        }else if (index == 1) {
            modifyPanel.removeAll();
            deletePanel.removeAll();
            addPanel.removeAll();
            getDeleteUserGUI();
        }else if (index == 2) {
            modifyPanel.removeAll();
            deletePanel.removeAll();
            addPanel.removeAll();
            getAddUserGUI();
        }

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                //这里感觉没啥必要，可以使用无break结构switch使重合代码最后一起运行；
                switch (selectedIndex) {
                    case 0:
                        try {
                            modifyPanel.removeAll();
                            deletePanel.removeAll();
                            addPanel.removeAll();
                            getModifyUserGUI();
                        } catch (SQLException e1) {
                            System.out.println("UserGUI|getModifyUserGUI()出错了！！");
                        }
                        break;
                    case 1:
                        try {
                            modifyPanel.removeAll();
                            deletePanel.removeAll();
                            addPanel.removeAll();
                            getDeleteUserGUI();
                        } catch (SQLException e2) {
                            System.out.println("UserGUI|getDeleteUserGUI()出错了！！");
                        }
                        break;
                    case 2 :
                        try {
                            modifyPanel.removeAll();
                            deletePanel.removeAll();
                            addPanel.removeAll();
                            getAddUserGUI();
                        } catch (SQLException e3) {
                            System.out.println("UserGUI|getAddUserGUI()出错了！！");

                        }
                        break;
                    default : break;
                }
            }
        });
    }

    public static void getModifyUserGUI() throws SQLException {
        setUser_name_role();
        //下拉框，就是老师视频里的一大堆数据
        nameComboBox = new JComboBox<String>(usersName);
        roleComboBox = new JComboBox<String>(usersRole);
        nameComboBox.setPreferredSize(new Dimension(150,24));
        roleComboBox.setPreferredSize(new Dimension(150,24));

        //这里就是各个组件的位置关系了
        //这个是用户名下拉框
        JPanel jp_name = new JPanel();
        JLabel nameLabel = new JLabel("用户：   ");
        jp_name.add(nameLabel);
        jp_name.add(nameComboBox);
        modifyPanel.add(jp_name);

        //这个是密码下拉框
        JPanel jp_password = new JPanel();
        JLabel passwordLabel = new JLabel("密码：   ");
        jp_password.add(passwordLabel);
        jp_password.add(passwordField);
        modifyPanel.add(jp_password);

        //这个是属性下拉框
        JPanel jp_role = new JPanel();
        JLabel roleLabel = new JLabel("属性：   ");
        jp_role.add(roleLabel);
        jp_role.add(roleComboBox);
        modifyPanel.add(jp_role);

        //一个确定一个取消，无限JPanel套娃，注意一点他们几个之间的关系，还有包含即可
        JPanel jp_confirm_and_cancel = new JPanel();
        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        jp_confirm_and_cancel.add(confirmButton);
        jp_confirm_and_cancel.add(cancelButton);
        modifyPanel.add(jp_confirm_and_cancel);

        //设置一下这个页面的大小和布局方式
        // GridLayout,网格布局，就是说他在第几排，第几个，每一排长多少，宽多少
        modifyPanel.setLayout(new GridLayout(4,1,5,5));

        //确定按钮的按动监视，经典老演员了，不解释
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getConfirmDialog();
            }
        });
        cancelButton.addActionListener(new ActionListener(){        //点击“取消”按钮清除该面板上的组件回到上一个面板
            public void actionPerformed(ActionEvent e) {
                getCancel();
            }
        });
    }

    public static void getDeleteUserGUI() throws SQLException {
        //画大表，在表里呈现所有元素，后面只需要选中即可
        setUser_name_role();//获得信息
        Enumeration<User> e = DataProcessing.getAllUser();
        String []columnNames = {"用户名","密码","属性"};
        String [][]tableValues = new String[100][3];
        int i = Client.get_Rows();
        //循环得到表里的各个数据，然后给放到所建立的数组里
        while(e.hasMoreElements()){
            User user = e.nextElement();
            String userName = user.getName();
            tableValues[i][0]=userName;
            String userPassword = user.getPassword();
            tableValues[i][1] = userPassword;
            String userRole = user.getRole();
            tableValues[i][2] = userRole;
            i++;
        }

        //现在才开始写GUI
        tableModel = new DefaultTableModel(tableValues,columnNames);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(250, 200));
        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        deletePanel.setLayout(new BorderLayout());
        JPanel jp = new JPanel();
        jp.add(confirmButton);
        jp.add(cancelButton);
        deletePanel.add(BorderLayout.CENTER,scrollPane);
        deletePanel.add(BorderLayout.SOUTH,jp);


        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                getConfirmDialog();
            }
        });
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                getCancel();
            }
        });
    }

    public static void getAddUserGUI() throws SQLException {
        setUser_name_role();

        roleComboBox = new JComboBox<String>(usersRole);
        roleComboBox.setPreferredSize(new Dimension(150,24));

        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        addPanel.setLayout(new GridLayout(4,1,5,5));
        JPanel jp1 = new JPanel();
        jp1.add(nameLabel);
        jp1.add(nameField);
        JPanel jp2 = new JPanel();
        jp2.add(passwordLabel);
        jp2.add(passwordField);
        JPanel jp3 = new JPanel();
        jp3.add(roleLabel);
        jp3.add(roleComboBox);
        JPanel jp4 = new JPanel();
        jp4.add(confirmButton);
        jp4.add(cancelButton);
        addPanel.add(jp1);
        addPanel.add(jp2);
        addPanel.add(jp3);
        addPanel.add(jp4);

        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                getConfirmDialog();
            }
        });

        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                getCancel();
            }
        });
    }

    public static void setUser_name_role() throws SQLException {
        Enumeration<User> e = DataProcessing.getAllUser();
        usersRole = new String[]{"administrator","operator","browser"};
        usersName = new String[100];
        int i = 0;
        while(e.hasMoreElements()){
            User user = e.nextElement();
            String userName = user.getName();
            usersName[i] = userName;
            i++;
        }
    }

    public static void getConfirmDialog() {
        //这个是一个Dialog弹窗，里面会进行具体操作内容，具体自己看
        JPanel jp1 = new JPanel();
        JLabel j1 = new JLabel();
        jp1.add(j1);
        int index = tabbedPane.getSelectedIndex();
        if (index == 0 ){
            j1.setText("确认修改该用户信息吗？");
        }else if (index == 1) {
            j1.setText("确认删除该用户信息吗？");
        }else if (index == 2) {
            j1.setText("确认添加该用户信息吗？");
        }

        //弹窗里的确认取消
        JPanel jp2 = new JPanel();
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");
        jp2.add(cancelButton);
        jp2.add(confirmButton);

        //在这里才开始创建那个弹窗，设置弹窗的一系列玩意
        JDialog dialog = new JDialog();
        dialog.setTitle("消息");
        dialog.setSize(200, 230);
        dialog.setLocationRelativeTo(null);
        dialog.add(new JPanel());
        dialog.add(jp1);
        dialog.add(jp2);
        dialog.setLayout(new GridLayout(3,1,0,0));
        dialog.setVisible(true);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String password = null;
                String role = null;

                JDialog jdialog = new JDialog();
                jdialog.setTitle("提示");
                jdialog.setLayout(new GridLayout(2,1,0,0));
                JLabel label = new JLabel();
                label.setText("添加成功");
                jdialog.setLocationRelativeTo(null);

                int index = tabbedPane.getSelectedIndex();
                if(index == 0){
                    name = (String)nameComboBox.getSelectedItem();
                    password = String.valueOf(passwordField.getPassword());
                    role = (String) roleComboBox.getSelectedItem();
                    try {
                        //更新信息
                        Client.UpdateUser(name, password, role, userManageFrame);

                    } catch (IOException e1) {
                        e1.printStackTrace();
                        label.setText("添加失败");
                    }

                }else if(index == 1){
                    int row = table.getSelectedRow();
                    String getName = table.getValueAt(row,0).toString();
                    try {
                        Client.DelUser(getName, userManageFrame);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }else{
                    password = String.valueOf(passwordField.getPassword());
                    role = (String) roleComboBox.getSelectedItem();
                    try {
                        Client.AddUser(name, password, role, userManageFrame);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                jdialog.setSize(200, 130);
                JPanel jp1 = new JPanel();
                JPanel jp2 =new JPanel();
                jdialog.add(jp1);
                jdialog.add(jp2);
                jdialog.setVisible(true);
                jp1.add(label);
                JButton button = new JButton("确定");
                jp2.add(button);

                button.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                        jdialog.dispose();
                    }
                });
            }
        });

        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
    }
    public static void getCancel(){
        modifyPanel.removeAll();
        deletePanel.removeAll();
        addPanel.removeAll();
        nameField.setText(null);
        passwordField.setText(null);
        userManageFrame.dispose();
    }
}
