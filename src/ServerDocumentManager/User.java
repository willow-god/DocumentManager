package ServerDocumentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.io.IOException;
import java.util.Enumeration;


public abstract class User {
    private String name;
    private String password;
    private String role;

    String uploadpath = "D:\\DownLoad\\javatestdocument\\uploadfile\\";
    String downloadpath = "D:\\DownLoad\\javatestdocument\\downloadfile\\";

    User(String name,String password,String role){
        this.name=name;
        this.password=password;
        this.role=role;
    }

    public boolean changeSelfInfo(String password) throws SQLException{
        //修改个人信息
        if (DataProcessing.update(name, password, role)){
            this.password=password;
            System.out.println("修改成功！");
            return true;
        }else
            return false;
    }

    public boolean downloadFile(String ID) throws IOException,SQLException{
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new IOException( "Error in accessing file" );
        Doc doc = DataProcessing.searchDoc(ID);
        if (doc == null) {
            return false;
        }
        File srcFile = new File(uploadpath + doc.getFilename());
        String filename = srcFile.getName();
        File destFile = new File(downloadpath + filename);
        if (!(destFile.exists())) {
            destFile.createNewFile();
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);

        byte[] buf = new byte[1024];
        int len = 0;
        while((len = fis.read(buf)) != -1) {
            fos.write(buf,0,len);
        }
        fis.close();
        fos.close();
        System.out.println("下载成功！");
        return true;
    }

    public void showFileList() throws SQLException{
//        double ranValue=Math.random();
//        if (ranValue>0.5)
//            throw new SQLException( "Error in accessing file DB" );
        Enumeration<Doc> e = DataProcessing.getAllDocs();
        Doc doc;
        System.out.println("展示如下：");
        while(e.hasMoreElements()) {
            doc = e.nextElement();
            System.out.println("ID:" + doc.getID()+
                    "\t Creator:"+doc.getCreator()+
                    "\t time:"+doc.getTimestamp()+
                    "\t Description:" + doc.getDescription()+
                    "\t Filename:"+doc.getFilename());
        }
    }

    public abstract void showMenu();

    public void exitSystem(){
        System.out.println("程序退出，谢谢使用! ");
        System.exit(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
