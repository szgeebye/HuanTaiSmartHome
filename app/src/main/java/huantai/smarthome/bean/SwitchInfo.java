package huantai.smarthome.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.orm.dsl.Table;

/**
     * description:开关表
     * auther：xuewenliao
     * time：2017/10/1 10:11
     */

@Table
public class SwitchInfo {

    @SerializedName("id")
    @Expose
    private Long id;
    @Expose
    private String name;//开关名称
    @Expose
    private String address;//设备Mac地址
    @Expose
    private String bindgiz;//绑定到此Mac地址的板子
    @Expose
    private String userid;//用户ID，备用
    @Expose
    private int flag;//留用
    @Expose
    private int type;//开关类型
    @Expose
    private int status;//状态
    @Expose
    private int picture;//对应图片
    @Expose
    private boolean isdelete;//是否在界面删除
//    private int status1;
//    private int status2;
//    private int status3;

    public SwitchInfo() {

    }

    public SwitchInfo(Long id, String name, String address, String bindgiz, String userid, int flag, int type, int status, int picture, boolean isdelete) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
                "id=" + id +
                ", name='" + name + '\'' +
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

    //    public SwitchInfo(int id, String name, String address, String bindgiz, String userid, int flag, int type) {
//        super();
//        this._id = id;
//        this.name = name;
//        this.address = address;
//        this.bindgiz = bindgiz;
//        this.userid = userid;
//        this.flag = flag;
//        this.type = type;
//        this.status1 = 0;
//        this.status2 = 0;
//        this.status3 = 0;
//    }
//
//    public SwitchInfo(String name, String address, String bindgiz, String userid, int flag, int type) {
//        super();
//        this.name = name;
//        this.address = address;
//        this.bindgiz = bindgiz;
//        this.flag = flag;
//        this.userid = userid;
//        this.type = type;
//    }
}
