package huantai.smarthome.bean;

import com.google.gson.annotations.Expose;
import com.orm.dsl.Table;

/**
     * description:开关表
     * auther：xuewenliao
     * time：2017/10/1 10:11
     */

@Table
public class SwitchInfo {

//    @SerializedName("id")
//    @Expose
//    public Long id;
    @Expose
    public String name;//开关名称
    @Expose
    public String address;//设备Mac地址
    @Expose
    public String bindgiz;//绑定到此Mac地址的板子
    @Expose
    public String userid;//用户ID，备用
    @Expose
    public int flag;//留用
    @Expose
    public int type;//开关类型
    @Expose
    public int status;//状态
    @Expose
    public int picture;//对应图片
    @Expose
    public boolean isdelete;//是否在界面删除
//    public int status1;
//    public int status2;
//    public int status3;

    public SwitchInfo() {

    }

    public SwitchInfo(String name, String address, String bindgiz, String userid, int flag, int type, int status, int picture, boolean isdelete) {
        this.name = name;
        this.address = address;
        this.bindgiz = bindgiz;
        this.userid = userid;
        this.flag = flag;
        this.type = type;
        this.status = status;
        this.picture = picture;
        this.isdelete = isdelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBindgiz() {
        return bindgiz;
    }

    public void setBindgiz(String bindgiz) {
        this.bindgiz = bindgiz;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public boolean isdelete() {
        return isdelete;
    }

    public void setIsdelete(boolean isdelete) {
        this.isdelete = isdelete;
    }

    @Override
    public String toString() {
        return "SwitchInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", bindgiz='" + bindgiz + '\'' +
                ", userid='" + userid + '\'' +
                ", flag=" + flag +
                ", type=" + type +
                ", status=" + status +
                ", picture=" + picture +
                ", isdelete=" + isdelete +
                '}';
    }
}
