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
    public String bindgiz;//绑定的网关
    //    @Expose
    public String userid;//用户ID，备用
    //    @Expose
    public int flag;//留用
    //    @Expose
    public int type;//开关类型（1：一位开关；2：二位开关；3：三位开关；4：插座；5：窗帘；6：空调）
    //    @Expose
    public int picture;//对应图片
    //    @Expose
    public boolean isdelete;//是否在界面删除
    //    @Expose
    public int status1;//共用字段1（对应开关：状态1--对应空调：brand品牌）
    //    @Expose
    public int status2;//共用字段2（对应开关：状态2--对应空调：temperature温度）
    //    @Expose
    public int status3;//共用字段3（对应开关：状态3--对应空调：mode模式）
    //    @Expose
    public int status4;//共用字段4（对应开关：无--对应空调：speed速度）
    //    @Expose
    public int status5;//共用字段5（对应开关：无--对应空调：direction风向）


    public SwitchInfo() {

    }

    public SwitchInfo(String name, String address, String bindgiz, String userid, int flag, int type, int picture, boolean isdelete, int status1, int status2, int status3, int status4, int status5) {
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
        this.status4 = status4;
        this.status5 = status5;
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

    public int getStatus4() {
        return status4;
    }

    public void setStatus4(int status4) {
        this.status4 = status4;
    }

    public int getStatus5() {
        return status5;
    }

    public void setStatus5(int status5) {
        this.status5 = status5;
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
                ", status4=" + status4 +
                ", status5=" + status5 +
                '}';
    }
}
