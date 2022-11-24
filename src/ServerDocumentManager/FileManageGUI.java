package ServerDocumentManager;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;
import java.util.Enumeration;

public class FileManageGUI {
    static JFrame archivesManageFrame = new JFrame("文件管理页面");
    static JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
    static int frameWidth;
    static int frameHeight;
    static JPanel downloadPanel = new JPanel();
    static JPanel uploadPanel = new JPanel();

    public static void playArchivesManageGUI(int index) {
        //点开档案选中框之后，上方的便捷切换按钮
        archivesManageFrame.setSize(450,250);
        archivesManageFrame.setLocationRelativeTo(null);
        frameWidth = archivesManageFrame.getWidth();
        frameHeight = archivesManageFrame.getHeight();


        //添加两个选择，和之前那个一样
        tabbedPane.add("档案上传",uploadPanel);
        tabbedPane.add("档案下载",downloadPanel);

        //获取这个人的身份，后面要用，比如上传的时候需要本人身份姓名等信息
        String userRole = Client.get_Role();
        if (userRole.equalsIgnoreCase("administrator")
        ||userRole.equalsIgnoreCase("operator")
        ||userRole.equalsIgnoreCase("browser")){
            //代码应该是文字不能够被更改的意思~没有查到该函数的作用~
            tabbedPane.setEnabledAt(0,false);
        }
        //默认选项，点开哪个，默认哪个
        tabbedPane.setSelectedIndex(index);
        if (index == 0) {
            downloadPanel.removeAll();
            uploadPanel.removeAll();
            getUploadFileGUI();
        }else if (index == 1) {
            downloadPanel.removeAll();
            uploadPanel.removeAll();
            try {
                getDownloadFileGUI();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //上面的那一条条状态改变，底下的UI一起改变
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                switch (selectedIndex) {
                    case 0 :
                        uploadPanel.removeAll();
                        downloadPanel.removeAll();
                        getUploadFileGUI();
                        break;
                    case 1 :
                        uploadPanel.removeAll();
                        downloadPanel.removeAll();
                        try {
                            getDownloadFileGUI();
                        } catch (SQLException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    default :
                        break;
                }
            }
        });
       // 把上面创建的这个导航栏添加到页面里
        //前面讲过，方位布局，这个玩意在正中间
        archivesManageFrame.add(tabbedPane, BorderLayout.CENTER);
        archivesManageFrame.setVisible(true);
        archivesManageFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static void getUploadFileGUI(){
        //又开始GUI了……这个是上传的GUI
        JLabel archivesNumberLabel = new JLabel("档案号");
        JLabel archivesDescriptionLabel = new JLabel("档案描述:");
        JLabel archivesNameLabel = new JLabel("档案文件名:");
        //文本输入框，文献档案号
        final JTextField archivesNumberField = new JTextField(12);
        //显示纯文本的多行区域~
        final JTextArea archivesDescriptionArea = new JTextArea(4,12);
        archivesDescriptionArea.setLineWrap(true);//自动换行功能
        final JTextField archivesNameField = new JTextField(12);//上传者姓名
        JButton openButton = new JButton("打开");
        JButton uploadButton = new JButton("上传");
        JButton cancelButton = new JButton("取消");

        //这里要素比较多，如果记不住，可以搞一个本子画一下
        uploadPanel.setLayout(new GridLayout(4,3,5,5));
        JPanel panel1 = new JPanel();
        panel1.add(archivesNumberLabel);
        uploadPanel.add(panel1);
        JPanel panel2 = new JPanel();
        panel2.add(archivesNumberField);
        uploadPanel.add(panel2);
        uploadPanel.add(new JPanel());
        JPanel panel3 = new JPanel();
        panel3.add(archivesDescriptionLabel);
        uploadPanel.add(panel3);
        JScrollPane panel4 = new JScrollPane(archivesDescriptionArea);
        //滚动条，显示所有文档嗷嗷嗷
        uploadPanel.add(panel4);
        uploadPanel.add(new JPanel());
        JPanel panel5 = new JPanel();
        panel5.add(archivesNameLabel);
        uploadPanel.add(panel5);
        JPanel panel6 = new JPanel();
        panel6.add(archivesNameField);
        uploadPanel.add(panel6);
        JPanel panel7 = new JPanel();
        panel7.add(openButton);
        uploadPanel.add(panel7);
        JPanel panel8 = new JPanel();
        panel8.add(uploadButton);
        uploadPanel.add(panel8);
        uploadPanel.add(new JPanel());
        JPanel panel9 = new JPanel();
        panel9.add(cancelButton);
        uploadPanel.add(panel9);

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int flag = fileChooser.showOpenDialog(uploadPanel);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    archivesNameField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int flag = fileChooser.showOpenDialog(uploadPanel);
                if (flag == JFileChooser.APPROVE_OPTION) {
                    //文件选择的类，人家已经写了就不用管了，然后后面显示文件的绝对路径
                    //绝对路径指的是以D://或者其他盘符为基本位置的路径
                    //相对路径指的是以本文件夹为基本位置的路径
                    File selectedFile = fileChooser.getSelectedFile();
                    archivesNameField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //写了一个弹窗，显示的事是否确认上传~
                JDialog dialog = new JDialog();
                dialog.setTitle("消息");
                dialog.setLayout(new GridLayout(3,1,0,0));
                JLabel jl = new JLabel();
                jl.setText("确定上传该文件吗?");
                dialog.setSize(200, 230);
                dialog.setLocationRelativeTo(null);
                JButton confirmBotton = new JButton("确定");
                JButton cancelBotton = new JButton("取消");
                JPanel jp1 = new JPanel();
                jp1.add(jl);
                JPanel jp2 =new JPanel();
                jp2.add(confirmBotton);
                jp2.add(cancelBotton);

                dialog.add(new JPanel());
                dialog.add(jp1);
                dialog.add(jp2);
                dialog.setVisible(true);

                //这里开始编辑弹窗里，两个按钮的作用
                confirmBotton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //这里获取的是刚才主页面得到的那玩意（档案号，位置）
                        String archivesNumber = archivesNumberField.getText();
                        String archivesDescription = archivesDescriptionArea.getText();
                        String archivesName = archivesNameField.getText();
                        String uploaderName = Client.get_Name();

                        JDialog jdialog = new JDialog();
                        jdialog.setTitle("消息");
                        jdialog.setLayout(new GridLayout(2,1,0,0));

                        //这里的label后面会给里面添加内容的，就是上传成功与否的内容
                        JLabel label = new JLabel();

                        jdialog.setSize(200, 130);
                        JButton button = new JButton("确定");
                        JPanel jp1 = new JPanel();
                        jp1.add(label);
                        JPanel jp2 =new JPanel();
                        jp2.add(button);
                        jdialog.add(jp1);
                        jdialog.add(jp2);

                        //开始试图保存文件~
                        try {
                            Client.Upload(archivesNumber,uploaderName,archivesDescription,archivesName,archivesManageFrame);
                            //这里的原网站胡写了，所以写不了了~我把它一起集成在了上面的upload函数里面~
                            label.setText("文件上传成功！！！");
                        } catch (IOException ex) {
                            label.setText("文件上传失败!");
                            ex.printStackTrace();
                        } catch (SQLException ex) {
                            label.setText("文件上传失败!");
                            ex.printStackTrace();
                        }

                        //弹窗里的确认按钮
                        jdialog.setVisible(true);
                        button.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                dialog.dispose();
                                jdialog.dispose();
                            }
                        });
                    }
                });//确认按钮到这里就写完了！！！！嗷嗷嗷嗷

                //这里开始写弹窗里的取消按钮
                cancelBotton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose();
                    }
                });
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadPanel.removeAll();
                uploadPanel.removeAll();
                archivesManageFrame.dispose();
            }
        });
    }

    public static void getDownloadFileGUI() throws SQLException{
        Enumeration <Doc> e = DataProcessing.getAllDocs();
        String []columnNames = {"档案号","档案号","时间","描述","文件名"};
        String [][]tableValues = new String[100][5];
        int i = 0;
        //循环获取Docs里面的所有玩意放到string类型数组里
        while (e.hasMoreElements()) {
            Doc doc = e.nextElement();
            String number =
            tableValues[i][0] = doc.getID();
            tableValues[i][1] = doc.getCreator();
            tableValues[i][2] = doc.getTimestamp().toString();
            tableValues[i][3] = doc.getDescription();
            tableValues[i][4] = doc.getFilename();
            i++;
        }

        //添加表格，添加按钮等
        DefaultTableModel tableModel= new DefaultTableModel(tableValues,columnNames);
        final JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        JButton downloadButton = new JButton("下载");
        JButton cancelButton = new JButton("取消");
        downloadPanel.setLayout(new BorderLayout());
        JPanel jp = new JPanel();
        jp.add(downloadButton);
        jp.add(cancelButton);
        downloadPanel.add(BorderLayout.CENTER,scrollPane);
        downloadPanel.add(BorderLayout.SOUTH,jp);

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(null,"请选择文件","提示",JOptionPane.ERROR_MESSAGE);
                } else {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int flag = fileChooser.showSaveDialog(downloadPanel);
                    String ID = table.getValueAt(row,0).toString();
                    final JDialog jDialog = new JDialog();
                    jDialog.setTitle("提示");
                    jDialog.setLayout(new GridLayout(2,1,0,0));

                    final JLabel label = new JLabel();

                    jDialog.setSize(200,130);
                    jDialog.setLocationRelativeTo(null);
                    JButton button = new JButton("确定");
                    JPanel jp1 = new JPanel();
                    jp1.add(label);
                    JPanel jp2 = new JPanel();
                    jp2.add(button);
                    jDialog.add(jp1);
                    jDialog.add(jp2);

                    try {
                        Client.Download(ID,archivesManageFrame);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    Doc doc = null;
                    doc = DataProcessing.searchDoc(ID);
                    if (doc == null) {
                        System.out.println("下载文件失败");
                    }
                    try {
                        String uploadpath = "D:\\DownLoad\\javatestdocument\\uploadfile\\";
                        File srcFile = new File(doc.getFilename());
                        String filename = srcFile.getName();
                        File destFile = new File(fileChooser.getSelectedFile().getAbsolutePath() +"\\"+ filename);
                        if (!(destFile.exists())) {
                            destFile.createNewFile();
                        }
                        //FileInputStream fis = new FileInputStream(srcFile);
                        //FileOutputStream fos = new FileOutputStream(destFile);

                        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(srcFile));
                        BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(destFile));

                        byte[] buf = new byte[1024];
                        int len = 0;
                        while((len = fis.read(buf)) != -1) {
                            fos.write(buf,0,len);
                        }
                        fos.flush();
                        fis.close();
                        fos.close();
                        System.out.println("下载成功！");
                    } catch (IOException ex) {
                        label.setText("文件下载失败");
                        ex.printStackTrace();
                    }
                    jDialog.setVisible(true);

                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            jDialog.dispose();
                        }
                    });
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadPanel.removeAll();
                uploadPanel.removeAll();
                archivesManageFrame.dispose();
            }
        });
    }
}
