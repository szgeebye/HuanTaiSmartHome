package huantai.smarthome.bean;


/**
     * description:添加开关（只在添加设备界面做显示并不存入数据库）
     * auther：xuewenliao
     * time：2017/10/2 16:32
     */

public class AddSwitchDevice {
    private String devicesort;//设备类别
    private int picture;//对应图片

    public String getDevicesort() {
        return devicesort;
    }

    public void setDevicesort(String devicesort) {
        this.devicesort = devicesort;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "AddSwitchDevice{" +
                "devicesort='" + devicesort + '\'' +
                ", picture=" + picture +
                '}';
    }
}