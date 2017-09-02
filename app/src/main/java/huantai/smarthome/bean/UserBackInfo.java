package huantai.smarthome.bean;

/**
 * use：接收后台用户数据
 * Created by xuewenliao on 2017/8/9.
 */

public class UserBackInfo {
    public int id; //用户id
    public String uname;//用户姓名
    public String upwd;//密码
    public String uphone;//手机（用户名）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }


}
