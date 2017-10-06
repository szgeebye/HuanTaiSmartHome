package huantai.smarthome.popWindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import com.orm.SugarRecord;

import java.util.List;

import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.bean.ConstantData;
import huantai.smarthome.bean.SwitchInfo;
import huantai.smarthome.control.DeviceFragment;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ToastUtil;
import razerdp.basepopup.BasePopupWindow;

/**
 * description:添加设备的popupWindow
 * auther：xuewenliao
 * time：2017/10/2 19:10
 */
public class InputPopup extends BasePopupWindow implements View.OnClickListener {
    private Button btn_cancel;
    private Button btn_Compelete;
    private EditText et_deviceName;
    private EditText et_deviceMac;
    private String deviceSort;//设备类型

    private static List<SwitchInfo> switchInfoList;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtil.ToastShow(getContext(), "添加成功");
                    break;
                case 2:
                    ToastUtil.ToastShow(getContext(), "设备名不能为空");
                    break;
                case 3:
                    ToastUtil.ToastShow(getContext(), "请输入正确的设备地址");
                    break;
            }
        }
    };
    private String regex;
    private String macAddress;
    private String deviceName;

    public InputPopup(Activity context) {
        super(context);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_Compelete = (Button) findViewById(R.id.btn_Compelete);
        et_deviceName = (EditText) findViewById(R.id.et_deviceName);
        et_deviceMac = (EditText) findViewById(R.id.et_deviceMac);

        //接收DeviceAddActivity发来的数据广播
        IntentFilter filter = new IntentFilter(ConstAction.deviceaddaction);
        context.registerReceiver(deviceaddbroadcast, filter);

        setAutoShowInputMethod(true);
        bindEvent();
    }

    private BroadcastReceiver deviceaddbroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            deviceSort = intent.getStringExtra("devicesort");
        }
    };

//    private void getDeviceData() {
//        DeviceAddActivity deviceAddActivity = new DeviceAddActivity();
//        deviceAddActivity.setHandler(handler);
//        Log.i("devicesort",deviceSort);
//    }

    @Override
    protected Animation initShowAnimation() {
        return null;
    }

    private void bindEvent() {
        btn_cancel.setOnClickListener(this);
        btn_Compelete.setOnClickListener(this);
    }

    //=============================================================super methods


    @Override
    public Animator initShowAnimator() {
        return getDefaultSlideFromBottomAnimationSet();
    }


    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.device_add_popup, null);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public Animator initExitAnimator() {
        AnimatorSet set = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            set = new AnimatorSet();
            if (initAnimaView() != null) {
                set.playTogether(
                        ObjectAnimator.ofFloat(initAnimaView(), "translationY", 0, 250).setDuration(400),
                        ObjectAnimator.ofFloat(initAnimaView(), "alpha", 1, 0.4f).setDuration(250 * 3 / 2));
            }
        }
        return set;
    }

    //=============================================================click event
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_Compelete:
                savaData();
                ToastUtil.ToastShow(getContext(), deviceSort + "添加成功");
                //符合要求才消失弹窗
                if (macAddress.matches(regex) && !deviceName.isEmpty()) {
                    dismiss();
                }
                break;
            default:
                break;
        }

    }

    //保存数据到数据库
    private void savaData() {

//        if (switchInfoList.isEmpty()) {

        SwitchInfo switchInfo = new SwitchInfo();
        regex = "^[A-Fa-f0-9]{8}$";
        macAddress = String.valueOf(et_deviceMac.getText());
        deviceName = String.valueOf(et_deviceName.getText());
        //判断Mac是否为8位16进制数
        if (macAddress.matches(regex) && !deviceName.isEmpty()) {
            switchInfo.setName(deviceName);
            switchInfo.setAddress(macAddress);


            if (deviceSort.equals(ConstantData.devicename[0])) {//一位开关
                switchInfo.setType(1);
                switchInfo.setPicture(0);

            } else if (deviceSort.equals(ConstantData.devicename[1])) {//二位开关
                switchInfo.setType(2);
                switchInfo.setPicture(1);

            } else if (deviceSort.equals(ConstantData.devicename[2])) {//三位开关
                switchInfo.setType(3);
                switchInfo.setPicture(2);

            } else if (deviceSort.equals(ConstantData.devicename[3])) {//插座
                switchInfo.setType(4);
                switchInfo.setPicture(3);

            }
            SugarRecord.save(switchInfo);
            switchInfoList = SugarRecord.listAll(SwitchInfo.class);
            Log.i("addall", switchInfoList.toString());

//        }
//        else {//更新数据
////            SwitchInfo switchInfo = new SwitchInfo();
//            for (SwitchInfo switchInfo : switchInfoList) {
//                switchInfo.setName(String.valueOf(et_deviceName.getText()));
//                switchInfo.setAddress(String.valueOf(et_deviceMac.getText()));
//                SugarRecord.save(switchInfo);
//                Log.i("addalll",switchInfoList.toString());
//            }
//
//        }

            //发送switchInfoList到DeviceFragment
            DeviceFragment deviceFragment = new DeviceFragment();
            deviceFragment.setSwitchInfoList(switchInfoList);
//        DeviceFragment.setSwitchInfoList(switchInfoList);

            //添加成功友好提示
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);

            //通知DeviceShowAdapter更新界面
            //发送DeviceShowAdapter界面更新广播
            Intent intent = new Intent(ConstAction.devicenotifyfinishaction);
            getContext().sendBroadcast(intent);
        } else if (deviceName.isEmpty()) {
            Message msg = new Message();
            msg.what = 2;
            handler.sendMessage(msg);
        } else {
            Message msg = new Message();
            msg.what = 3;
            handler.sendMessage(msg);
        }

    }
}
