package ServerDocumentManager;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.*;

public class PersonMessageGUI {

    public static void playPersonMessageManageGUI(){
        JFrame personMessageManageFrame = new JFrame();
        personMessageManageFrame.setTitle("个人信息管理");

        personMessageManageFrame.setSize(300, 400);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        int screenWidth= dimension.width;
        int screenHeight = dimension.height;
        int frameWidth = personMessageManageFrame.getWidth();
        int frameHeight = personMessageManageFrame.getHeight();
        personMessageManageFrame.setLocation((screenWidth-frameWidth)/2, (screenHeight-frameHeight)/2);

        JPanel changePanel = new JPanel();
        JLabel nameLabel = new JLabel("用户名:      ");
        JLabel roleLabel = new JLabel("属    性:      ");
        JLabel oldPasswordLabel = new JLabel("原密码:      ");
        JLabel newPasswordLabel = new JLabel("新密码:      ");
        JLabel newPasswordLabel2 = new JLabel("确认新密码");
        JTextField nameField = new JTextField(13);
        nameField.setText(Client.get_Name());
        nameField.setEditable(false);
        final JPasswordField oldPasswordField = new JPasswordField(13);
        final JPasswordField newPasswordField = new JPasswordField(13);
        final JPasswordField newPasswordField2 = new JPasswordField(13);
        final JTextField roleField = new JTextField(13);
        roleField.setText(Client.get_Role());
        roleField.setEditable(false);

        JButton confirmButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");

        changePanel.setLayout(new GridLayout(6,1,5,5));
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        panel1.add(nameLabel);
        panel1.add(nameField);
        panel2.add(oldPasswordLabel);
        panel2.add(oldPasswordField);
        panel3.add(newPasswordLabel);
        panel3.add(newPasswordField);
        panel4.add(newPasswordLabel2);
        panel4.add(newPasswordField2);
        panel5.add(roleLabel);
        panel5.add(roleField);
        panel6.add(confirmButton);
        panel6.add(cancelButton);
        changePanel.add(panel1);
        changePanel.add(panel2);
        changePanel.add(panel3);
        changePanel.add(panel4);
        changePanel.add(panel5);
        changePanel.add(panel6);
        personMessageManageFrame.add(changePanel);

        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {

                String name = nameField.getText();
                String oldPassword = String.valueOf(oldPasswordField.getPassword());
                String newPassword = String.valueOf(newPasswordField.getPassword());
                String newPassword2 = String.valueOf(newPasswordField2.getPassword());
                String role = roleField.getText();

                try {
                    Client.ChangeSelfInfo(oldPassword, newPassword, newPassword2);
                } catch (IOException | SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                personMessageManageFrame.dispose();
            }
        });

        personMessageManageFrame.setVisible(true);
        personMessageManageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}

