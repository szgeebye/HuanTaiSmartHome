package huantai.smarthome.control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.xmcamera.core.model.XmErrInfo;
import com.xmcamera.core.model.XmStreamMode;
import com.xmcamera.core.sys.XmSystem;
import com.xmcamera.core.sysInterface.IXmRealplayCameraCtrl;
import com.xmcamera.core.sysInterface.IXmSystem;
import com.xmcamera.core.sysInterface.IXmTalkManager;
import com.xmcamera.core.sysInterface.OnXmBeginTalkListener;
import com.xmcamera.core.sysInterface.OnXmEndTalkListener;
import com.xmcamera.core.sysInterface.OnXmListener;
import com.xmcamera.core.sysInterface.OnXmSimpleListener;
import com.xmcamera.core.sysInterface.OnXmStartResultListener;
import com.xmcamera.core.sysInterface.OnXmTalkVolumListener;
import com.xmcamera.core.view.decoderView.XmGlView;

import huantai.smarthome.initial.R;
import huantai.smarthome.utils.spUtil;

import static huantai.smarthome.initial.R.id.glview;

/**
 * Created by lj_xwl on 2017/11/2.
 */

public class VideoPlayActivity extends Activity implements View.OnClickListener{


    IXmTalkManager talkma;
    private ListView list_oc;
    private LinearLayout ll_oc_list;
    private LinearLayout ll_show;
    private boolean isshow = false;
    private ScrollView scrollView;
    private TextView tv_show;
    private TextView tx_oc;
    String logtext = "";
    IXmSystem xmSystem;
    IXmRealplayCameraCtrl realplayCameraCtrl;
    int cameraid;
    int playId;
    boolean isRecord = false;
    FrameLayout playContent;
    XmGlView glView;

    Button play, stop, HD, SD, AT, capture, Record, StopRecord, btn_rebinder,
            SLrecord, SLrecordclose;
    spUtil sp;

    Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
//            ArrayList<Gizinfo> gizinfos;
            switch (msg.what) {
                case 0x123:
                    Toast.makeText(VideoPlayActivity.this, "删除成功！", Toast.LENGTH_LONG)
                            .show();
                    setResult(101);
                    finish();
                    break;
                case 0x124:
                    Toast.makeText(VideoPlayActivity.this, "删除失败！", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 0x125:
                    Toast.makeText(VideoPlayActivity.this, (String) msg.obj,
                            Toast.LENGTH_LONG).show();
                    break;
                case 0x126:
                    Toast.makeText(VideoPlayActivity.this, logtext, Toast.LENGTH_LONG)
                            .show();
                    //                    show.setText(logtext);
                    break;
                case 0x127:
                    Toast.makeText(VideoPlayActivity.this, "你没有权限，请先注册登录~",
                            Toast.LENGTH_LONG).show();
                    break;
                case 0x128:
                    Toast.makeText(VideoPlayActivity.this, "视频已经在播放中！",
                            Toast.LENGTH_LONG).show();
                    break;
                case 0x129:
                    Toast.makeText(VideoPlayActivity.this, "开启错误", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 0x130:
                    Toast.makeText(VideoPlayActivity.this, "关闭错误", Toast.LENGTH_LONG)
                            .show();
                    break;
                case 0x131:
                    Toast.makeText(VideoPlayActivity.this, "已开启对讲", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case 0x132:
                    Toast.makeText(VideoPlayActivity.this, "已关闭对讲", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
        }

    };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        init();
    }

    public void init() {
        cameraid = getIntent().getExtras().getInt("cameraId");
        xmSystem = XmSystem.getInstance();
        realplayCameraCtrl = xmSystem.xmGetRealplayController();

        talkma = xmSystem.xmGetTalkManager(cameraid);

        glView = new XmGlView(this, null);
        playContent = (FrameLayout) findViewById(glview);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels - 10;
        params.height = dm.widthPixels - 10;
        playContent.setLayoutParams(params);
        playContent.addView((View) glView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);

        scrollView = (ScrollView) findViewById(R.id.scrollview);

        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);
        HD = (Button) findViewById(R.id.HD);
        SD = (Button) findViewById(R.id.SD);
        AT = (Button) findViewById(R.id.AT);
        capture = (Button) findViewById(R.id.capture);
        Record = (Button) findViewById(R.id.Record);
        StopRecord = (Button) findViewById(R.id.StopRecord);
        btn_rebinder = (Button) findViewById(R.id.btn_rebinder);
        SLrecord = (Button) findViewById(R.id.SLrecord);
        SLrecordclose = (Button) findViewById(R.id.SLrecordclose);
        ll_show = (LinearLayout) findViewById(R.id.ll_show);
        tv_show = (TextView) findViewById(R.id.tv_show);

        tx_oc = (TextView) findViewById(R.id.tx_oc);
        tx_oc.setVisibility(View.GONE);

        ll_oc_list = (LinearLayout) findViewById(R.id.ll_oc_list);
        ll_oc_list.setVisibility(View.GONE);

        list_oc = (ListView) findViewById(R.id.list_oc);
//        list_oc.setAdapter(adapter);

        ll_show.setVisibility(View.GONE);
        ll_show.setOnClickListener(this);
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        HD.setOnClickListener(this);
        SD.setOnClickListener(this);
        AT.setOnClickListener(this);
        capture.setOnClickListener(this);
        Record.setOnClickListener(this);
        StopRecord.setOnClickListener(this);
        btn_rebinder.setOnClickListener(this);
        SLrecord.setOnClickListener(this);
        SLrecordclose.setOnClickListener(this);
        //show = (TextView) findViewById(R.id.show);

        //        mHander.sendEmptyMessage(0x126);
        sp = new spUtil(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play:
                Play();
                break;
            case R.id.stop:
                Stop();
                break;
            case R.id.HD:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                SwitchStream(XmStreamMode.ModeHd);//高清模式
                break;
            case R.id.SD:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                SwitchStream(XmStreamMode.ModeFluency);//标清
                break;
            case R.id.AT:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                SwitchStream(XmStreamMode.ModeAdapter);//自动
                break;
            case R.id.capture:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                Capture();
                break;
            case R.id.Record:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                Record();
                break;
            case R.id.StopRecord:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                if (!isPlay()) {
                    break;
                }
                StopRecord();
                break;
            case R.id.btn_rebinder:
                if (sp.getisDemo()) {
                    mHander.sendEmptyMessage(0x127);
                    break;
                }
                btn_rebinder();
                break;
            case R.id.SLrecord:
                if (talkma.getCurState() == IXmTalkManager.TalkState.NoOpen) {
                    slrecord();
                } else {
                    mHander.sendEmptyMessage(0x129);
                }

                break;
            case R.id.SLrecordclose:
                if (talkma.getCurState() == IXmTalkManager.TalkState.Opened) {
                    slrecordclose();
                } else {
                    mHander.sendEmptyMessage(0x130);
                }

                break;
        }
    }

    //打开监控
    private void Play() {
        if (realplayCameraCtrl.isPlaying()) {
            mHander.sendEmptyMessage(0x128);
            return;
        }
        realplayCameraCtrl.xmStart(glView, cameraid,
                new OnXmStartResultListener() {
                    @Override
                    public void onStartSuc(boolean isLocalNet, int cameraId,
                                           int var3) {
                        playId = var3;
                        showTV("播放成功！");
                    }

                    @Override
                    public void onStartErr(XmErrInfo errcode) {

                        Log.i("playerr", String.valueOf(errcode.errCode));
                        showTV("播放失败");
                    }
                });
    }

    //关闭监控
    private void Stop() {
        if (realplayCameraCtrl.isPlaying()) {
            realplayCameraCtrl.xmStop(playId);
            showTV("停止播放！");
        }
    }

    //切换模式
    private void SwitchStream(final XmStreamMode mode) {
        realplayCameraCtrl.xmSwitchStream(mode, new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                showTV("切换失败！" + info.discribe);
            }

            @Override
            public void onSuc() {
                if (mode == XmStreamMode.ModeHd) {
                    showTV("切换到HD");
                } else if (mode == XmStreamMode.ModeFluency) {
                    showTV("切换到SD");
                } else if (mode == XmStreamMode.ModeAdapter) {
                    showTV("切换到AT");
                }
            }
        });
    }

    //截图
    private void Capture() {
        final long time = System.currentTimeMillis();
        realplayCameraCtrl.xmCapture("/sdcard/zzj/", "p" + time + ".jpg",
                new OnXmListener<String>() {
                    @Override
                    public void onErr(XmErrInfo info) {
                        showTV("截图失败");
                    }

                    @Override
                    public void onSuc(String info) {
                        showTV("截图成功1:" + "/sdcard/zzj/" + "p" + time + ".jpg");
                        realplayCameraCtrl.xmThumbnail("/sdcard/zzj", "thumb"
                                        + time + ".jpg", "p" + time + ".jpg",
                                new OnXmListener<String>() {
                                    @Override
                                    public void onErr(XmErrInfo info) {
                                        showTV("截图失败2");
                                    }

                                    @Override
                                    public void onSuc(String info) {
                                        showTV("截图成功2:" + "/sdcard/zzj/"
                                                + "thumb" + time + ".jpg");
                                    }
                                });
                    }
                });
    }

    private void Record() {
        isRecord = true;
        long time = System.currentTimeMillis();
        boolean suc = realplayCameraCtrl.xmRecord("/sdcard/zzj", "v" + time
                + ".mp4");
        Toast.makeText(this, suc ? "开始录像" : "录像失败", Toast.LENGTH_LONG).show();
        showTV(suc ? "开始录像" : "录像失败");
    }

    private void StopRecord() {
        if (!isRecord) {
            return;
        }
        isRecord = false;
        String ss = realplayCameraCtrl.xmStopRecord();
        Toast.makeText(this, ss == null ? "录像失败" : "录像成功：" + ss,
                Toast.LENGTH_LONG).show();
        showTV(ss == null ? "录像失败" : "录像成功：" + ss);
    }

    //解绑设备
    private void btn_rebinder() {
        new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage("您确定要删除此摄像机吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteDevice();
                    }
                }).setNegativeButton("取消", null).show();
    }
    //删除设备
    private void DeleteDevice() {
        xmSystem.xmDeleteDevice(cameraid, xmSystem.xmFindDevice(cameraid)
                .getmUuid(), new OnXmSimpleListener() {
            @Override
            public void onErr(XmErrInfo info) {
                mHander.sendEmptyMessage(0x124);
                showTV("删除失败！" + info.discribe);
            }

            @Override
            public void onSuc() {
                mHander.sendEmptyMessage(0x123);
            }
        });
    }

    //打开可视对讲
    private void slrecord() {
        talkma.xmBeginTalk(new OnXmBeginTalkListener() {

            @Override
            public void onSuc() {
                mHander.sendEmptyMessage(0x131);
            }

            @Override
            public void onOpenTalkErr(XmErrInfo arg0) {
                mHander.sendEmptyMessage(0x129);
            }

            @Override
            public void onNoRecordPermission() {

            }

            @Override
            public void onIPCIsTalking() {

            }

            @Override
            public void onAlreadyTalking() {
            }
        }, new OnXmTalkVolumListener() {

            @Override
            public void onVolumeChange(int arg0) {
            }
        });

    }

    //关闭可视对讲
    private void slrecordclose() {
        talkma.xmEndTalk(new OnXmEndTalkListener() {

            @Override
            public void onTalkClosing() {

            }

            @Override
            public void onSuc() {
                mHander.sendEmptyMessage(0x132);
                // Toast.makeText(getApplicationContext(), "关闭对讲成功",
                // Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCloseTalkErr(XmErrInfo arg0) {
                mHander.sendEmptyMessage(0x130);
            }

            @Override
            public void onAlreadyClosed() {
                // TODO Auto-generated method stub

            }
        });
    }


    //判断视频是否开启
    private boolean isPlay() {
        if (!realplayCameraCtrl.isPlaying()) {
            showTV("视频未开启！");
        }
        return realplayCameraCtrl.isPlaying();
    }


    private void showTV(String ss) {
        logtext = ss;
        mHander.sendEmptyMessage(0x126);
    }
}
