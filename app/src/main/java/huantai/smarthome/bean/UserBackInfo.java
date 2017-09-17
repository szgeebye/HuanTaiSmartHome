package huantai.smarthome.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

/**
 * use：接收后台用户数据
 * Created by xuewenliao on 2017/8/9.
 */
@Table
public class UserBackInfo{


    //    @Unique
    @Expose
    @SerializedName("id")
    Long id; //用户id
    @Expose
    String uname;//用户姓名
    @Expose
    String upwd;//密码
    @Expose
    String uphone;//手机（用户名）

    public UserBackInfo(){

    }
    public UserBackInfo(Long id, String uname, String upwd, String uphone) {
        this.id = id;
        this.uname = uname;
        this.upwd = upwd;
        this.uphone = uphone;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
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

    @Override
    public String toString() {
        return "UserBackInfo{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", upwd='" + upwd + '\'' +
                ", uphone='" + uphone + '\'' +
                '}';
    }


}
