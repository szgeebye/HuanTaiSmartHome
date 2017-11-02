package huantai.smarthome.control;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.xmcamera.core.model.XmAccount;
import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;

import org.json.JSONException;

import java.util.concurrent.ConcurrentHashMap;

import huantai.smarthome.adapter.MyFragmentPagerAdapter;
import huantai.smarthome.bean.ConstAction;
import huantai.smarthome.initial.CommonModule.GosBaseActivity;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.spUtil;
import huantai.smarthome.view.MainViewPager;


/**
 * Created by Coder-pig on 2015/8/28 0028.
 */
public class MainActivity extends GosBaseActivity implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {

    private GizWifiDevice device;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_better;
    private RadioButton rb_setting;
    private MainViewPager vpager;

    private MyFragmentPagerAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static GizWifiDevice commandevice;//供其他界面调用的device
    private IXmSystem xmSystem;
    spUtil sp;
    public static  XmAccount account = null;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "监控视频加载成功！", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "监控视频加载失败！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
    private Intent sendDataBroadcastIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        bindViews();
        rb_channel.setChecked(true);
        initDevice();
        initBroadreceive();

//        initVideo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        loadVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        xmSystem.xmLogout();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        xmSystem.xmLogout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        xmSystem.xmLogout();
    }

    /**
     * description:注册震动广播
     * auther：xuewenliao
     * time：2017/9/10 17:02
     */
    private void initBroadreceive() {

        IntentFilter intentFilter = new IntentFilter(ConstAction.senddeviceaction);
        MainActivity.this.registerReceiver(sendDataBroadcast, intentFilter);

        //发送video登陆信息广播
        sendDataBroadcastIntent = new Intent(ConstAction.sendvideoaction);

        //接收震动广播
        IntentFilter filter = new IntentFilter(ConstAction.vibratoraction);
        registerReceiver(vibratorBroadcast, filter);

    }

    private BroadcastReceiver sendDataBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("kuozhan");
            byte[] value = intent.getByteArrayExtra("value");
            try {
                sendJson(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void initDevice() {
        Intent intent = getIntent();
        device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
        commandevice = device;
    }

    private void sendJson(String key, Object value) throws JSONException {
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
        hashMap.put(key, value);
        device.write(hashMap, 0);
        Log.i("==", hashMap.toString());
        // Log.i("Apptest", hashMap.toString());
    }

    //实现广播内容
    private BroadcastReceiver vibratorBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Vibrator vibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
            long[] pattern = {800, 30, 10, 30};
            vibrator.vibrate(pattern, -1);
        }
    };

    private void bindViews() {
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_better = (RadioButton) findViewById(R.id.rb_better);
        rb_setting = (RadioButton) findViewById(R.id.rb_setting);
        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (MainViewPager) findViewById(R.id.vpager);
        //设置不能滑动
        vpager.setScanScroll(false);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);
        vpager.addOnPageChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_better:
                vpager.setCurrentItem(PAGE_THREE);
//                initVideo();
                break;
            case R.id.rb_setting:
                vpager.setCurrentItem(PAGE_FOUR);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_channel.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_message.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_better.setChecked(true);
//                    initVideo();
                    break;
                case PAGE_FOUR:
                    rb_setting.setChecked(true);
                    break;
            }
        }
    }




    private void loadVideo() {
        initVideo();
        showLoadingDialog();
        try {
            xmSystem.xmLogin("13135367953",
                    "chen162858", new OnXmListener<XmAccount>() {
                        @Override
                        public void onSuc(XmAccount outinfo) {
                            closeLoadingDialog();
                            handler.sendEmptyMessage(1);
//                            MainActivity.this.sp.setUsername("13135367953");
//                            sendDataBroadcastIntent.putExtra("user", outinfo);
//                            sendBroadcast(sendDataBroadcastIntent);
                            account = outinfo;
                            System.out.print(1);
//                            loginSuc(outinfo);
                        }

                        @Override
                        public void onErr(XmErrInfo info) {
                            closeLoadingDialog();
                            handler.sendEmptyMessage(2);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            closeLoadingDialog();
            handler.sendEmptyMessage(2);
        } finally {

        }
    }

    //初始化视频
    private void initVideo() {
        //初始化爱小屏sdk
        xmSystem = XmSystem.getInstance();
        xmSystem.xmInit(MainActivity.this, "CN", new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                Log.v("AAAAA", "init Fail");
            }

            @Override
            public void onSuc() {
                Log.v("AAAAA", "init Suc");
            }
        });
    }

    //登陆成功后跳转添加摄像头设备界面
    public void loginSuc(XmAccount info) {
        //登录成功的操作MainContentActivity.class
//        Intent intent = new Intent(MainActivity.this, VideoFragment.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("user", info);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        System.out.print(1);
//        sendDataBroadcastIntent.putExtra("user", info);
//        sendBroadcast(sendDataBroadcastIntent);
        //finish();
    }

    ProgressDialog dialog;

    //加载视频提示
    public void showLoadingDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("请稍后...");
        dialog.show();
    }

    public void closeLoadingDialog() {
        dialog.dismiss();
    }
}
