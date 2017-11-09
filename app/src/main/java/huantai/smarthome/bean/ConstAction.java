package huantai.smarthome.bean;

/**
 * Created by lj_xwl on 2017/9/10.
 */

public class ConstAction {
    //震动广播
    public static final String vibratoraction = "com.huantai.smarthome.mainactivity.vibrator.acion";
    //完成(删除)广播
    public static final String deletefinishaction = "com.huantai.smarthome.homefragment.deletefinish.action";
    //完成(更新)广播
    public static final String notifyfinishaction = "com.huantai.smarthome.addremoveadpter.notifyfinish.action";
    //inputPopup向DeviceShowAdapter发送界面更新广播
    public static final String devicenotifyfinishaction = "com.huantai.smarthome.deviceshowadapter.devicenotifyfinish.action";
    //inputPopup接收DeviceAddActivity传递的数据
    public static final String deviceaddaction = "com.huantai.smarthome.deviceaddactivity.deviceaddaction.action";
    //开关控制广播
    public static final String switchcontrolaction = "com.huantai.smarthome.popupswitch.switchcontrolaction.action";
    //窗帘控制广播
    public static final String curtaincontrolaction = "com.huantai.smarthome.popupcurtain.curtaincontrolaction.action";
    //发送device广播
    public static final String senddeviceaction = "com.huantai.smarthome.MainActivity.senddeviceaction.action";
    //发送video广播
    public static final String sendvideoaction = "com.huantai.smarthome.MainActivity.sendvideoaction.action";
    //弹吐司广播
    public static final String showtoastaction = "com.huantai.smarthome.MainActivity.showtoastaction.action";
    //报警界面更新广播
    public static final String alertnotifyaction = "com.huantai.smarthome.alertmesadaper.alertnotifyaction.action";
    //服务发数据广播
    public static final String servicedatanotifyaction = "huantai.smarthome.Service.ServiceNotify.alertnotifyaction.action";
    //服务发数据广播
    public static final String exitappnotifyaction = "huantai.smarthome.control.MineFragment.exitappnotifyaction.action";

}
