package DocumentManager;

public abstract class User {
    private String name;
    private String password;
    private String role;

    String uploadpath = "/Users/air/Documents/java/uploadfile/";
    String downloadpath = "/Users/air/Documents/java/downloadfile/";

    User(String name,String password,String role){
        this.name=name;
        this.password=password;
        this.role=role;
    }

    public boolean changeSelfInfo(String password){
        //改变信息
        if (DataProcessing.update(name, password, role)){
            this.password=password;
            System.out.println("修改成功！");
            return true;
        }else
            return false;
    }

    public abstract void showMenu();

    public boolean downloadFile(String filename){
        System.out.println("展示如下");
        return true;
    }

    public void showFileList(){
        System.out.println("文件列表如下");
    }

    public void exitSystem(){
        System.out.println("系统退出，谢谢使用！");
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
