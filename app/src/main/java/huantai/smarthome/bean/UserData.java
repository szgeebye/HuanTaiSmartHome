package huantai.smarthome.bean;

/**
 * Created by xuewenliao on 2017/8/7.
 */

public class UserData {
    public String uphone;
    public String upwd;


    public UserData(String userName, String upwd) {
        this.uphone = userName;
        this.upwd = upwd;
    }

    public String getUserName() {
        return uphone;
    }

    public void setUserName(String userName) {
        this.uphone = userName;
    }

    public String getPassWord() {
        return upwd;
    }

    public void setPassWord(String upwd) {
        this.upwd = upwd;
    }
}
