package huantai.smarthome.popWindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.initial.R;
import razerdp.basepopup.BasePopupWindow;

/**
 * Created by 大灯泡 on 2016/1/23.
 * dialogpopup :)
 * 客串一下dialog
 */
public class ExitPopup extends BasePopupWindow implements View.OnClickListener{

    private TextView ok;
    private TextView cancel;
    private Context context;

    public ExitPopup(Activity context) {
        super(context);
        this.context = context;
        ok= (TextView) findViewById(R.id.ok);
        cancel= (TextView) findViewById(R.id.cancel);

        setViewClickListener(this,ok,cancel);
    }

    @Override
    protected Animation initShowAnimation() {
        AnimationSet set=new AnimationSet(false);
        Animation shakeAnima=new RotateAnimation(0,15, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
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
        return createPopupById(R.layout.popup_exit);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.popup_anima);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok:
                Intent eintent = new Intent();
                eintent.setAction(ConstAction.exitappnotifyaction);
                context.sendBroadcast(eintent);
                break;
            case R.id.cancel:
                getPopupWindow().dismiss();
                break;
            default:
                break;
        }

    }
}
