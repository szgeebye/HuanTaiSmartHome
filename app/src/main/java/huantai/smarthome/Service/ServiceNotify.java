package huantai.smarthome.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import java.util.concurrent.ConcurrentHashMap;

import huantai.smarthome.initial.R;

/**
 * Created by lj_xwl on 2017/11/8.
 */

public class ServiceNotify extends Service {
    private static final String KEY_Gate = "gate1";
    private static final String KEY_Smoke = "smoke1";
    private static final String KEY_Gas = "gas1";
    private static final String KEY_Body = "body1";
    private ConcurrentHashMap<String, Object> deviceMap;
    private GizWifiDevice device;
    /**
     * 各项数据点
     */
    protected boolean gasstua = false;//燃气
    protected boolean smokestua = false;//烟雾
    protected boolean gatestua = false;//门磁
    protected boolean bodystua = false;//人体
    protected boolean lastgasstua = false;
    protected boolean lastsmokestua = false;
    protected boolean lastgatestua = false;
    protected boolean lastbodystua = false;
    private Intent dataIntent;
    protected static final int RESP = 1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESP:
                    if (deviceMap != null) {
                        for (String datakey:deviceMap.keySet()){
                            switch (datakey) {
                                case KEY_Gate:
                                    gatestua = (boolean) deviceMap.get(KEY_Gate);
                                    break;
                                case KEY_Smoke:
                                    smokestua = (boolean) deviceMap.get(KEY_Smoke);
                                    break;
                                case KEY_Gas:
                                    gasstua = (boolean) deviceMap.get(KEY_Gas);
                                    break;
                                case KEY_Body:
                                    bodystua = (boolean) deviceMap.get(KEY_Body);
                                    break;
                            }
                        }
                    }
                    showNotify();
                    break;
            }
        }
    };

    private void showNotify() {
        if (lastgasstua != gasstua) {
            lastgasstua = gasstua;
            if (lastgasstua) {
                notifbulid("燃气报警器发出警报!!", R.drawable.ic_gas_a, 0x010,
                        "燃气");
            }
        }
        if (lastgatestua != gatestua) {
            lastgatestua = gatestua;
            if (lastgatestua) {
                notifbulid("门磁触发了!!", R.drawable.ic_gate_a, 0x011, "门磁");
            }
        }

        if (lastbodystua != bodystua) {
            lastbodystua = bodystua;
            if (lastbodystua) {
                notifbulid("人体红外移动监测到有人经过!!", R.drawable.ic_body_a,
                        0x012, "人体移动");
            }
        }


        if (lastsmokestua != smokestua) {
            lastsmokestua = smokestua;
            if (lastsmokestua) {
                notifbulid("烟雾报警器发出警报!!", R.drawable.ic_smoke_a, 0x013,
                        "烟雾");
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        initData();
        dataIntent = intent;
        ConnetGIZThread initThread = new ConnetGIZThread();
        initThread.start();
        return START_REDELIVER_INTENT;
    }



    // 设备监听者及相关回调
    protected GizWifiDeviceListener gizWifiDeviceListener = new GizWifiDeviceListener() {

        /** 用于设备状态 */
        public void didReceiveData(GizWifiErrorCode arg0, GizWifiDevice arg1,
                                   ConcurrentHashMap<String, Object> arg2, int arg3) {
            ServiceNotify.this.didReceiveData(arg0, arg1, arg2, arg3);
        }

    };

    private void didReceiveData(GizWifiErrorCode result, GizWifiDevice arg1,
                                ConcurrentHashMap<String, Object> dataMap, int arg3) {
        if (dataMap.get("data") != null) {
            deviceMap = (ConcurrentHashMap<String, Object>) dataMap.get("data");
            Message msg = new Message();
            msg.obj = deviceMap;
            msg.what = RESP;
            handler.sendMessage(msg);
        }
    }


    //连接机智云
    private class ConnetGIZThread extends Thread {
        @Override
        public void run() {
            initDevice(dataIntent);
        }
    }

    //初始化机智云设备
    private void initDevice(Intent dataIntent) {
        device = (GizWifiDevice) dataIntent.getParcelableExtra("GizWifiDevice");
        Log.i("==", "device:" + device.getMacAddress());
        deviceMap = new ConcurrentHashMap<String, Object>();
        device.setListener(gizWifiDeviceListener);
    }

//    private GizWifiDeviceListener gizWifideviceMapener = new GizWifiDeviceListener(){
//        @Override
//        public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
//            if (dataMap.get("data") != null) {
//                deviceMap = (ConcurrentHashMap<String, Object>) dataMap.get("data");
//                Message msg = new Message();
//                msg.obj = deviceMap;
//                msg.what = RESP;
//                handler.sendMessage(msg);
//            }
//        }
//    };
    private void initData() {

    }
    private void notifbulid(String mes, int icon, int id, String mestit) {
//        if (spf.getBoolean("issafe", true)) {
            NotificationCompat.Builder bulider = new NotificationCompat.Builder(
                    this);
            bulider.setSmallIcon(icon);
            bulider.setContentTitle("接收到" + mestit + "报警消息");
            bulider.setTicker("接到环泰智能家居提示消息");
            bulider.setDefaults(Notification.DEFAULT_ALL);
            bulider.setAutoCancel(true);
            bulider.setContentText(mes);
            Notification notification = bulider.build();
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(id, notification);
//        }
    }

}
