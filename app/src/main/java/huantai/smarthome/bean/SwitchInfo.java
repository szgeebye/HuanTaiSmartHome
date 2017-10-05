package huantai.smarthome.bean;

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
//    @Expose
    public String name;//开关名称
//    @Expose
    public String address;//设备Mac地址
//    @Expose
    public String bindgiz;//绑定到此Mac地址的板子
//    @Expose
    public String userid;//用户ID，备用
//    @Expose
    public int flag;//留用
//    @Expose
    public int type;//开关类型
//    @Expose
    public int picture;//对应图片
//    @Expose
    public boolean isdelete;//是否在界面删除
//    @Expose
    public int status1;//状态
//    @Expose
    public int status2;//状态
//    @Expose
    public int status3;//状态

    public SwitchInfo() {

    }

    public SwitchInfo(String name, String address, String bindgiz, String userid, int flag, int type, int picture, boolean isdelete, int status1, int status2, int status3) {
        this.name = name;
        this.address = address;
        this.bindgiz = bindgiz;
        this.userid = userid;
        this.flag = flag;
        this.type = type;
        this.picture = picture;
        this.isdelete = isdelete;
        this.status1 = status1;
        this.status2 = status2;
        this.status3 = status3;
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

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public int getStatus2() {
        return status2;
    }

    public void setStatus2(int status2) {
        this.status2 = status2;
    }

    public int getStatus3() {
        return status3;
    }

    public void setStatus3(int status3) {
        this.status3 = status3;
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
                ", picture=" + picture +
                ", isdelete=" + isdelete +
                ", status1=" + status1 +
                ", status2=" + status2 +
                ", status3=" + status3 +
                '}';
    }
}
