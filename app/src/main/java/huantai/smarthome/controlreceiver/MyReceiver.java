package huantai.smarthome.controlreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lj_xwl on 2017/11/2.
 */

//自定义广播接收者
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (action.equals(ConstAction.switchcontrolaction)) {
//            byte type = intent.getByteExtra("type", (byte) 0xff);
//            byte status = intent.getByteExtra("status", (byte) 0xff);
//            Log.i("addresss", String.valueOf(status));
//            try {
//                Log.i("address", address);
//                byte[] b = ControlUtils.getSwitchInstruction(type, status, address.toUpperCase());
//                sendJson("kuozhan", ControlUtils.getSwitchInstruction(type, status, address.toUpperCase()));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            Message msg = new Message();
//            msg.what = SENDSUCCESS;
//            handler.sendMessage(msg);
//
//        } else if (action.equals(ConstAction.curtaincontrolaction)) {
//            byte CMD = intent.getByteExtra("control", (byte) 0xff);
//            try {
//                sendJson("kuozhan", ControlUtils.getCurtainInstruction(address,CMD));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            //状态显示
//            switch (CMD) {
//                case ControlProtocol.DevCMD.CURTAIN_CLOSE:
//                    ToastUtil.ToastShow(getActivity(),"关闭");
//                    break;
//                case ControlProtocol.DevCMD.CURTAIN_OPEN:
//                    ToastUtil.ToastShow(getActivity(),"打开");
//                    break;
//                case ControlProtocol.DevCMD.CURTAIN_STOP:
//                    ToastUtil.ToastShow(getActivity(),"停止");
//                    break;
//                case ControlProtocol.DevCMD.CURTAIN_REDIC:
//                    ToastUtil.ToastShow(getActivity(),"换向");
//                    break;
//
//            }
//
//        }

    }
}