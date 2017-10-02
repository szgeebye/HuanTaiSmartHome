package huantai.smarthome.popWindow;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;

import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ToastUtil;
import razerdp.basepopup.BasePopupWindow;

/**
     * description:添加设备的popupWindow
     * auther：xuewenliao
     * time：2017/10/2 19:10
     */
public class InputPopup extends BasePopupWindow implements View.OnClickListener{
    private Button btn_cancel;
    private Button btn_Compelete;
    private EditText et_deviceName;
    private EditText et_deviceMac;

    public InputPopup(Activity context) {
        super(context);
        btn_cancel= (Button) findViewById(R.id.btn_cancel);
        btn_Compelete= (Button) findViewById(R.id.btn_Compelete);
        et_deviceName= (EditText) findViewById(R.id.et_deviceName);
        et_deviceMac= (EditText) findViewById(R.id.et_deviceMac);

        setAutoShowInputMethod(true);
        bindEvent();
    }

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

//    @Override
//    public EditText getInputView() {
//        return mInputEdittext;
//    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        return LayoutInflater.from(getContext()).inflate(R.layout.device_add_popup,null);
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
        switch (v.getId()){
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_Compelete:
//                ToastUtils.ToastMessage(getContext(),mInputEdittext.getText().toString());
                ToastUtil.ToastShow(getContext(),et_deviceName.getText().toString()+"/n"+et_deviceMac.getText().toString());
                dismiss();
                break;
            default:
                break;
        }

    }
}
