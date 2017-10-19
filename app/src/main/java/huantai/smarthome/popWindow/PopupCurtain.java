package huantai.smarthome.popWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;

import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ControlProtocol;
import razerdp.basepopup.BasePopupWindow;

/**
 * Auther：joahluo
 * E-mail：joahluo@163.com
 * Time：2017/7/22 10:03
 */
public class PopupCurtain extends BasePopupWindow implements View.OnClickListener {

    private Context context;
    private TextView ok;
    private TextView cancel;
    private Button btn_redic, btn_colse, btn_open, btn_stop;
    //UI更新广播
//    public static final String curtain_action = "com.device.control.curtain.action";

    private Intent intent;

    public PopupCurtain(Activity context) {
        super(context);
        this.context = context;

        intent = new Intent(ConstAction.curtaincontrolaction);
        ok = (TextView) findViewById(R.id.ok);
        cancel = (TextView) findViewById(R.id.cancel);
        btn_redic = (Button) findViewById(R.id.btn_show_redic);
        btn_colse = (Button) findViewById(R.id.btn_show_colse);
        btn_open = (Button) findViewById(R.id.btn_show_open);
        btn_stop = (Button) findViewById(R.id.btn_show_stop);
        setViewClickListener(this, ok, cancel, btn_redic, btn_colse, btn_open, btn_stop);
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set = new AnimationSet(false);
        Animation shakeAnima = new RotateAnimation(0, 15, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shakeAnima.setInterpolator(new CycleInterpolator(5));
        shakeAnima.setDuration(400);
        set.addAnimation(getDefaultAlphaAnimation());
        set.addAnimation(shakeAnima);
        return set;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return createPopupById(R.layout.popupwindow_device_control_curtain);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                break;
            case R.id.cancel:
                break;
            case R.id.btn_show_colse:
                intent.putExtra("control", ControlProtocol.DevCMD.CURTAIN_CLOSE);
                context.sendBroadcast(intent);
                break;
            case R.id.btn_show_open:
                intent.putExtra("control", ControlProtocol.DevCMD.CURTAIN_OPEN);
                context.sendBroadcast(intent);
                break;
            case R.id.btn_show_stop:
                intent.putExtra("control", ControlProtocol.DevCMD.CURTAIN_STOP);
                context.sendBroadcast(intent);
                break;
            case R.id.btn_show_redic:
                intent.putExtra("control", ControlProtocol.DevCMD.CURTAIN_REDIC);
                context.sendBroadcast(intent);
                break;
            default:
                break;
        }

    }
}
