package huantai.smarthome.initial.UserModule;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import com.gizwits.gizwifisdk.api.GizWifiSDK;
import com.gizwits.gizwifisdk.enumration.GizEventType;
import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.google.gson.Gson;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import huantai.smarthome.bean.UserBackInfo;
import huantai.smarthome.bean.UserData;
import huantai.smarthome.initial.CommonModule.GosBaseActivity;
import huantai.smarthome.initial.CommonModule.GosDeploy;
import huantai.smarthome.initial.CommonModule.TipsDialog;
import huantai.smarthome.initial.DeviceModule.GosDeviceListActivity;
import huantai.smarthome.initial.DeviceModule.GosMainActivity;
import huantai.smarthome.initial.GosApplication;
import huantai.smarthome.initial.PushModule.GosPushManager;
import huantai.smarthome.initial.R;
import huantai.smarthome.initial.ThirdAccountModule.BaseUiListener;
import huantai.smarthome.initial.view.DotView;

@SuppressLint("HandlerLeak")
//@ContentView(R.layout.activity_gos_user_login)
public class GosUserLoginActivity extends GosUserModuleBaseActivity implements OnClickListener {

	GosPushManager gosPushManager;

	/** The et Name */
//	@ViewInject(R.id.etName)
	private static EditText etName;

	/** The et Psw */
//	@ViewInject(R.id.etPsw)
	private static EditText etPsw;

	/** The btn Login */
//	@ViewInject(R.id.btnLogin)
	private Button btnLogin;

	/** The tv Register */
//	@ViewInject(R.id.tvRegister)
	private TextView tvRegister;

	/** The tv Forget */
//	@ViewInject(R.id.tvForget)
	private TextView tvForget;

	/** The tv Pass */
//	@ViewInject(R.id.tvPass)
	private TextView tvPass;

	/** The cb Laws */
//	@ViewInject(R.id.cbLaws)
	private CheckBox cbLaws;

	/** The ll QQ */
//	@ViewInject(R.id.llQQ)
	private LinearLayout llQQ;

	/** The ll Wechat */
//	@ViewInject(R.id.llWechat)
	private LinearLayout llWechat;

	/** The Tencent */
	private Tencent mTencent;

	/** The Wechat */
	public static IWXAPI mIwxapi;

	/** The Scope */
	private String Scope = "get_user_info,add_t";

	/** The IUiListener */
	IUiListener listener;

	Intent intent;

	/** The GizThirdAccountType */
	public static GizThirdAccountType gizThirdAccountType;

	/** The THRED_LOGIN UID&TOKEN */
	public static String thirdUid, thirdToken;

	public static enum handler_key {

		/** 登录 */
		LOGIN,

		/** 自动登录 */
		AUTO_LOGIN,

		/** 第三方登录 */
		THRED_LOGIN,

	}

	/** 与WXEntryActivity共用Handler */
	private Handler baseHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key) {
				// 登录
				case LOGIN:
					progressDialog.show();
					GosDeviceListActivity.loginStatus = 0;
					GizWifiSDK.sharedInstance().userLogin(etName.getText().toString(), etPsw.getText().toString());
					//截取用户名密码子线程
					Thread loginThread = new Thread(new LoginThread());
					loginThread.start();
					break;
				// 自动登录
				case AUTO_LOGIN:
					progressDialog.show();
					GosDeviceListActivity.loginStatus = 0;
					GizWifiSDK.sharedInstance().userLogin(spf.getString("UserName", ""), spf.getString("PassWord", ""));
					break;
				// 第三方登录
				case THRED_LOGIN:
					progressDialog.show();
					GosDeviceListActivity.loginStatus = 0;
					GizWifiSDK.sharedInstance().loginWithThirdAccount(gizThirdAccountType, thirdUid, thirdToken);
					spf.edit().putString("thirdUid", thirdUid).commit();
					break;

			}
		};

	};

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		if (!this.isTaskRoot()) {// 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
			Intent mainIntent = getIntent();
			String action = mainIntent.getAction();
			if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		}
		if (GosApplication.flag != 0) {
			GosBaseActivity.noIDAlert(this, R.string.AppID_Toast);
		}

		// 在配置文件中选择推送类型（0：不开启推送，1：极光推送，2：百度推送。默认为0）
		gosPushManager = new GosPushManager(GosDeploy.setPushType(), this);

		setContentView(R.layout.activity_gos_user_login);

		// 设置actionBar
		setActionBar(false, false, R.string.app_company);
//		x.view().inject(this);
		initView();
		initEvent();
	}

	@Override
	protected void onResume() {
		super.onResume();

		JPushInterface.onResume(this);
		autoLogin();

		cleanuserthing();
	}

	private void cleanuserthing() {

		if (isclean) {
			etName.setText("");
			;
			etPsw.setText("");
			;
		}
	}

	private void autoLogin() {

		if (TextUtils.isEmpty(spf.getString("UserName", "")) || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
			return;
		}

		baseHandler.sendEmptyMessageDelayed(handler_key.AUTO_LOGIN.ordinal(), 1000);
	}

	private void initView() {
		etName = (EditText) findViewById(R.id.etName);
		etPsw = (EditText) findViewById(R.id.etPsw);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvRegister = (TextView) findViewById(R.id.tvRegister);
		tvForget = (TextView) findViewById(R.id.tvForget);
		tvPass = (TextView) findViewById(R.id.tvPass);
		cbLaws = (CheckBox) findViewById(R.id.cbLaws);

		llQQ = (LinearLayout) findViewById(R.id.llQQ);
		llWechat = (LinearLayout) findViewById(R.id.llWechat);
		DotView DotView = (DotView) findViewById(R.id.dotView1);
		String setTencentAppID = GosDeploy.setTencentAppID();
		String setWechatAppID = GosDeploy.setWechatAppID();
		// 判断腾讯和微信是否需要隐藏和显示
		setWechatOrTencentIsVisable(DotView);
		// 配置文件部署
		btnLogin.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
		btnLogin.setTextColor(GosDeploy.setButtonTextColor());

	}

	protected void setWechatOrTencentIsVisable(DotView DotView) {
		if (!GosDeploy.setWechat()) {

			llWechat.setVisibility(View.GONE);
		}
		if (!GosDeploy.setQQ()) {

			llQQ.setVisibility(View.GONE);
		}

		if (!GosDeploy.setWechat() && !GosDeploy.setQQ()) {
			DotView.setVisibility(View.GONE);
		}
	}

	private void initEvent() {
		btnLogin.setOnClickListener(this);
		tvRegister.setOnClickListener(this);
		tvForget.setOnClickListener(this);
		tvPass.setOnClickListener(this);

		llQQ.setOnClickListener(this);
		llWechat.setOnClickListener(this);

		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String psw = etPsw.getText().toString();

				if (isChecked) {
					etPsw.setInputType(0x90);
				} else {
					etPsw.setInputType(0x81);
				}
				etPsw.setSelection(psw.length());
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btnLogin:

				if (TextUtils.isEmpty(etName.getText().toString())) {
					Toast.makeText(GosUserLoginActivity.this, R.string.toast_name_wrong, toastTime).show();
					return;
				}
				if (TextUtils.isEmpty(etPsw.getText().toString())) {
					Toast.makeText(GosUserLoginActivity.this, R.string.toast_psw_wrong, toastTime).show();
					return;
				}
				baseHandler.sendEmptyMessage(handler_key.LOGIN.ordinal());
				break;

			case R.id.tvRegister:
				intent = new Intent(GosUserLoginActivity.this, GosRegisterUserActivity.class);
				startActivity(intent);

				break;
			case R.id.tvForget:
				intent = new Intent(GosUserLoginActivity.this, GosForgetPasswordActivity.class);
				startActivity(intent);
				break;
			case R.id.tvPass:

				intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
				startActivity(intent);

				logoutToClean();
				break;

			case R.id.llQQ:
				String tencentAPPID = GosDeploy.setTencentAppID();
				if (TextUtils.isEmpty(tencentAPPID) || tencentAPPID.contains("your_tencent_app_id")) {
					noIDAlert(this, R.string.TencentAPPID_Toast);
					return;
				} else {
					// 启动QQ登录SDK
					mTencent = Tencent.createInstance(GosDeploy.setTencentAppID(), this.getApplicationContext());
				}

				listener = new BaseUiListener() {
					protected void doComplete(JSONObject values) {
						Message msg = new Message();
						try {
							if (values.getInt("ret") == 0) {
								gizThirdAccountType = GizThirdAccountType.GizThirdQQ;
								thirdUid = values.getString("openid").toString();
								thirdToken = values.getString("access_token").toString();
								msg.what = handler_key.THRED_LOGIN.ordinal();
								baseHandler.sendMessage(msg);
							} else {

								Toast.makeText(GosUserLoginActivity.this, msg.obj.toString(), toastTime).show();

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				};
				mTencent.login(this, Scope, listener);
				break;
			case R.id.llWechat:
				String wechatAppID = GosDeploy.setWechatAppID();
				String wechatAppSecret = GosDeploy.setWechatAppSecret();
				if (TextUtils.isEmpty(wechatAppID) || TextUtils.isEmpty(wechatAppSecret)
						|| wechatAppID.contains("your_wechat_app_id")
						|| wechatAppSecret.contains("your_wechat_app_secret")) {
					noIDAlert(this, R.string.WechatAppID_Toast);
					return;
				} else {

					// 设置与WXEntryActivity共用Handler
					setBaseHandler(baseHandler);

					// 启动微信登录SDK
					mIwxapi = WXAPIFactory.createWXAPI(this, wechatAppID, true);

					// 将应用的AppID注册到微信
					mIwxapi.registerApp(wechatAppID);
				}

				if (!(mIwxapi.isWXAppInstalled() && mIwxapi.isWXAppSupportAPI())) {
					noIDAlert(this, R.string.No_WXApp);
					return;
				}
				SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "wechat_sdk_demo_test";
				mIwxapi.sendReq(req);
				break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Tencent.onActivityResultData(requestCode, resultCode, data, listener);
	}

	/**
	 * 设置云端服务回调
	 */
	protected void didGetCurrentCloudService(GizWifiErrorCode result,
											 java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, toastError(result), toastTime).show();
		}
	}

	/**
	 * 用户登录回调
	 */
	@Override
	protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {

		progressDialog.cancel();
		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "User");
		if (GosDeviceListActivity.loginStatus == 4 || GosDeviceListActivity.loginStatus == 3) {
			return;
		}
		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "UserLogin");

		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {// 登录失败
			Toast.makeText(GosUserLoginActivity.this, toastError(result), toastTime).show();

		} else {// 登录成功

			GosDeviceListActivity.loginStatus = 1;
			Toast.makeText(GosUserLoginActivity.this, R.string.toast_login_successful, toastTime).show();

			// TODO 绑定推送
			GosPushManager.pushBindService(token);

			if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())
					&& TextUtils.isEmpty(spf.getString("thirdUid", ""))) {
				spf.edit().putString("UserName", etName.getText().toString()).commit();
				spf.edit().putString("PassWord", etPsw.getText().toString()).commit();
			}
			spf.edit().putString("Uid", uid).commit();
			spf.edit().putString("Token", token).commit();

			intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
			intent.putExtra("ThredLogin", true);
			startActivity(intent);

		}
	}

	/**
	 * 解绑推送回调
	 *immple
	 * @param result
	 */
	protected void didChannelIDUnBind(GizWifiErrorCode result) {
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
			Toast.makeText(this, toastError(result), toastTime).show();
		}

		Log.i("Apptest", "UnBind:" + result.toString());
	};

	/**
	 * 菜单、返回键响应
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exitBy2Click(); // 调用双击退出函数
		}
		return false;
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			String doubleClick = (String) getText(R.string.double_click);
			Toast.makeText(this, doubleClick, toastTime).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

		} else {
			this.finish();
			System.exit(0);
		}
	}

	@Override
	protected void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID,
								  String eventMessage) {
		super.didNotifyEvent(eventType, eventSource, eventID, eventMessage);

		if (eventID != null && eventID.getResult() != 8316) {
			String toastError = toastError(eventID);

			TipsDialog dia = new TipsDialog(this, toastError);
			dia.show();
		}

	}

	/** 注销函数 */
	void logoutToClean() {
		spf.edit().putString("UserName", "").commit();
		spf.edit().putString("PassWord", "").commit();
		spf.edit().putString("Uid", "").commit();
		spf.edit().putString("thirdUid", "").commit();

		spf.edit().putString("Token", "").commit();

		if (GosDeviceListActivity.loginStatus == 1) {
			GosDeviceListActivity.loginStatus = 0;
		} else {
			GosDeviceListActivity.loginStatus = 4;
		}

	}
	/**
	 * use:获取登录用户数据子线程
	 * auther:XuewenLiao
	 * date:2017/9/2
	 * time:1
	 */
	class LoginThread implements Runnable{

		@Override
		public void run() {
			Gson gson = new Gson();
//			String jsonSend = gson.toJson(new UserData(etName.getText().toString(), etPsw.getText().toString()));
			RequestParams params = new RequestParams("http://39.108.151.208:9000/user/login/"+etName.getText().toString()+"/");
			params.addHeader("Content-type","application/x-www-form-urlencoded");
			params.setCharset("UTF-8");
//			params.setAsJsonContent(true);
//			params.setBodyContent(jsonSend);

			x.http().get(params, callback);
		}
	}

	private  Callback.CommonCallback<String> callback = new Callback.CommonCallback<String>() {
		@Override
		public void onSuccess(String result) {

			Log.i("server","REGISTER_SUCCESS");

			//获取到数据
			String jsonBack = result;
			UserBackInfo userBackInfo = new Gson().fromJson(jsonBack, UserBackInfo.class);
			//处理数据
			Integer id = userBackInfo.id;
			String uname = userBackInfo.uname;
			String uphone = userBackInfo.uphone;
			String upwd = userBackInfo.upwd;

			spf.edit().putInt("id",id).commit();
			spf.edit().putString("uname",uname).commit();
			spf.edit().putString("uphone",uphone).commit();
			spf.edit().putString("upwd",upwd).commit();

			Log.i("server","id:"+id);
			Log.i("server","uname:"+uname);
			Log.i("server","uphone:"+uphone);
			Log.i("server","upwd:"+upwd);




		}

		@Override
		public void onError(Throwable ex, boolean isOnCallback) {
//			Message msg = new Message();
//			msg.what = handler_key.CONNECT_FAIL.ordinal();
//			handler.sendMessage(msg);
			Log.i("server","CONNECT_FAIL");
		}

		@Override
		public void onCancelled(CancelledException cex) {

			Log.i("server","onCancelled");
		}

		@Override
		public void onFinished() {

			Log.i("server","onFinished");
		}
	};
}




//import org.json.JSONException;
//import org.json.JSONObject;
//import org.xutils.BuildConfig;
//import org.xutils.common.Callback;
//import org.xutils.http.RequestParams;
//import org.xutils.x;
//
//import cn.jpush.android.api.JPushInterface;
//import huantai.smarthome.bean.UserBackInfo;
//import huantai.smarthome.bean.UserData;
//
//import huantai.smarthome.initial.CommonModule.GosBaseActivity;
//import huantai.smarthome.initial.CommonModule.GosDeploy;
//import huantai.smarthome.initial.CommonModule.TipsDialog;
//import huantai.smarthome.initial.DeviceModule.GosDeviceListActivity;
//import huantai.smarthome.initial.DeviceModule.GosMainActivity;
//import huantai.smarthome.initial.GosApplication;
//import huantai.smarthome.initial.PushModule.GosPushManager;
//import huantai.smarthome.initial.R;
//import huantai.smarthome.initial.ThirdAccountModule.BaseUiListener;
//import huantai.smarthome.initial.view.DotView;
//import huantai.smarthome.utils.Encryption;
//
//import com.gizwits.gizwifisdk.api.GizWifiSDK;
//import com.gizwits.gizwifisdk.enumration.GizEventType;
//import com.gizwits.gizwifisdk.enumration.GizThirdAccountType;
//import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
//import com.google.gson.Gson;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tencent.tauth.IUiListener;
//import com.tencent.tauth.Tencent;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.Timer;
//import java.util.TimerTask;


//@SuppressLint("HandlerLeak")
//public class GosUserLoginActivity extends GosUserModuleBaseActivity implements OnClickListener {

//	private Button btnLogin;
//	private TextView tvRegister;
//	private TextView tvForget;
//	private EditText etPsw;
//	private EditText etName;
//	private CheckBox cbLaws;
//	private Intent intent;
//	private Encryption encryption;
//	private SharedPreferences userData;
//	private String result = "";
//	private TextView tvPass;
//	private SharedPreferences sp;

//	private enum handler_key {
//		/**
//		 * 登录
//		 */
//		LOGIN,
//
//		/**
//		 * 自动登录
//		 */
//		AUTO_LOGIN,
//
//		/**
//		 * 第三方登录
//		 */
//		THRED_LOGIN,
//
//		/**
//		 * 登陆成功.
//		 */
//		LOGIN_SUCCESS,
//
//		/**
//		 * 登陆失败.
//		 */
//		LOGIN_FAIL,
//		/**
//		 * 联网失败.
//		 */
//		CONNECT_FAIL,
//	}
//
//	Handler handler = new Handler() {
//		@Override
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			handler_key key = handler_key.values()[msg.what];
//			switch (key) {
//				//登录成功
//				case LOGIN_SUCCESS:
//					Toast.makeText(GosUserLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//					intent = new Intent(GosUserLoginActivity.this, GosDeviceListActivity.class);
//					startActivity(intent);
//					finish();
//					break;
//				//登录失败
//				case LOGIN_FAIL:
//					Toast.makeText(GosUserLoginActivity.this, "登录失敗", Toast.LENGTH_SHORT).show();
//					break;
//				//联网失败
//				case CONNECT_FAIL:
//					Toast.makeText(GosUserLoginActivity.this, "服务器异常，请稍后重试", Toast.LENGTH_SHORT).show();
//					break;
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setTheme(R.style.AppTheme);
//		setContentView(R.layout.activity_gos_user_login);
//		x.Ext.init(getApplication());
//		x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
//		// 设置 Theme actionBar
//
//		setActionBar(false, false, R.string.app_company);
//		initView();
//		initEvent();
//
//	}
//
//	private void initEvent() {
//		btnLogin.setOnClickListener(this);
//		tvRegister.setOnClickListener(this);
//		tvForget.setOnClickListener(this);
//		cbLaws.setOnClickListener(this);
//		tvPass.setOnClickListener(this);
//	}
//
//	private void initView() {
//		sp = getSharedPreferences("userdata", 0);
//
//
//		etName = (EditText) findViewById(R.id.etName);
//		etPsw = (EditText) findViewById(R.id.etPsw);
//		btnLogin = (Button) findViewById(R.id.btnLogin);
//		tvRegister = (TextView) findViewById(R.id.tvRegister);
//		tvForget = (TextView) findViewById(R.id.tvForget);
//		tvPass = (TextView)findViewById(R.id.tvPass);
//		//记住密码
//		cbLaws = (CheckBox) findViewById(R.id.cbLaws);
//		//加密工具
//		encryption = new Encryption();
//		//保存用户名和密码
//		userData = getSharedPreferences("userData", 0);
//	}
//
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.btnLogin:
//				if (TextUtils.isEmpty(etName.getText().toString())) {// TODO: 06/08/2017 判断是否为手机号
//					Toast.makeText(GosUserLoginActivity.this, R.string.toast_name_wrong, Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if (TextUtils.isEmpty(etPsw.getText().toString())) {
//					Toast.makeText(GosUserLoginActivity.this, R.string.toast_psw_wrong, Toast.LENGTH_SHORT).show();
//					return;
//				}
//				login();
////                handler.sendEmptyMessage(handler_key.LOGIN.ordinal());
//				break;
//
//			case R.id.tvRegister:
//				//TODO:注册
//				intent = new Intent(GosUserLoginActivity.this, GosRegisterUserActivity.class);
//				startActivity(intent);
//				break;
//			case R.id.tvForget:
//				intent = new Intent(GosUserLoginActivity.this, GosForgetPasswordActivity.class);
//				startActivity(intent);
//				break;
//			//匿名登录响应
//			case R.id.tvPass:
//				sp.edit().putString("UserName", "").commit();
//				sp.edit().putString("PassWord", "").commit();
//				intent = new Intent(GosUserLoginActivity.this, GosDeviceListActivity.class);
//				startActivity(intent);
//				finish();
//				break;
//		}
//	}
//
//	private void login() {
//		Thread loginThread = new Thread(new LoginThread());
//		loginThread.start();
//
//	}
//
//	class LoginThread implements Runnable {
//
//		@Override
//		public void run() {
//			Gson gson = new Gson();
//			String userName = etName.getText().toString();
//			String psw = etPsw.getText().toString();
//			//密码加密
//			psw = encryption.encryption_data(psw);
//			//保存密码
//			SharedPreferences.Editor editor = sp.edit();
//			editor.putString("userName", userName);
//			editor.putString("psw", psw);
//			editor.commit();
//			//要发送的json数据
////            UserData userData = new UserData(userName,psw);
////            Gson gson = new Gson();
//			//封装数据为json并发送
//			String jsonSend = gson.toJson(new UserData(userName, psw));
////            RequestParams params = new RequestParams(NetConstant.formatUrl("http://39.108.151.208:9000/user/register/"));
//			RequestParams params = new RequestParams("http://39.108.151.208:9000/user/login/");
//			params.addHeader("Content-type","application/x-www-form-urlencoded");
//			params.setCharset("UTF-8");
//			params.setAsJsonContent(true);
//			params.setBodyContent(jsonSend);
//			x.http().post(params, callback);
//
//
////            boolean loginValidate = HttpSever.isCorrect(userName, psw);
////            Message msg = handler.obtainMessage();
////            if (loginValidate) {
////                msg.what = handler_key.LOGIN_SUCCESS.ordinal();
////                handler.sendMessage(msg);
////            } else {
////                msg.what = handler_key.LOGIN_FAIL.ordinal();
////                handler.sendMessage(msg);
////            }
//
//		}
//	}
//
//	Message msg = handler.obtainMessage();
//	//处理返回数据
//	private Callback.CommonCallback<String> callback = new Callback.CommonCallback<String>() {
//
//		@Override
//		public void onSuccess(String result) {
//
//			//获取到数据
//			String jsonBack = result;
//			UserBackInfo userBackInfo = new Gson().fromJson(jsonBack, UserBackInfo.class);
//			//处理数据
//			boolean info = userBackInfo.success;
//			if (info) {
//				msg.what = handler_key.LOGIN_SUCCESS.ordinal();
//				handler.sendMessage(msg);
//			} else {
//				msg.what = handler_key.LOGIN_FAIL.ordinal();
//				handler.sendMessage(msg);
//			}
//		}
//
//		@Override
//		public void onError(Throwable ex, boolean isOnCallback) {
//			msg.what = handler_key.CONNECT_FAIL.ordinal();
//			handler.sendMessage(msg);
//		}
//
//		@Override
//		public void onCancelled(CancelledException cex) {
//
//		}
//
//		@Override
//		public void onFinished() {
//
//		}
//	};













//	GosPushManager gosPushManager;
//
//	/** The et Name */
//	private static EditText etName;
//
//	/** The et Psw */
//	private static EditText etPsw;
//
//	/** The btn Login */
//	private Button btnLogin;
//
//	/** The tv Register */
//	private TextView tvRegister;
//
//	/** The tv Forget */
//	private TextView tvForget;
//
//	/** The tv Pass */
//	private TextView tvPass;
//
//	/** The cb Laws */
//	private CheckBox cbLaws;
//
//	/** The ll QQ */
//	private LinearLayout llQQ;
//
//	/** The ll Wechat */
//	private LinearLayout llWechat;
//
//	/** The Tencent */
//	private Tencent mTencent;
//
//	/** The Wechat */
//	public static IWXAPI mIwxapi;
//
//	/** The Scope */
//	private String Scope = "get_user_info,add_t";
//
//	/** The IUiListener */
//	IUiListener listener;
//
//	Intent intent;
//
//	/** The GizThirdAccountType */
//	public static GizThirdAccountType gizThirdAccountType;
//
//	/** The THRED_LOGIN UID&TOKEN */
//	public static String thirdUid, thirdToken;
//
//	public static enum handler_key {
//
//		/** 登录 */
//		LOGIN,
//
//		/** 自动登录 */
//		AUTO_LOGIN,
//
//		/** 第三方登录 */
//		THRED_LOGIN,
//
//	}
//
//	/** 与WXEntryActivity共用Handler */
//	private Handler baseHandler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			super.handleMessage(msg);
//			handler_key key = handler_key.values()[msg.what];
//			switch (key) {
//			// 登录
//			case LOGIN:
//				progressDialog.show();
//				GosDeviceListActivity.loginStatus = 0;
//				GizWifiSDK.sharedInstance().userLogin(etName.getText().toString(), etPsw.getText().toString());
//				break;
//			// 自动登录
//			case AUTO_LOGIN:
//				progressDialog.show();
//				GosDeviceListActivity.loginStatus = 0;
//				GizWifiSDK.sharedInstance().userLogin(spf.getString("UserName", ""), spf.getString("PassWord", ""));
//				break;
//			// 第三方登录
//			case THRED_LOGIN:
//				progressDialog.show();
//				GosDeviceListActivity.loginStatus = 0;
//				GizWifiSDK.sharedInstance().loginWithThirdAccount(gizThirdAccountType, thirdUid, thirdToken);
//				spf.edit().putString("thirdUid", thirdUid).commit();
//				break;
//
//			}
//		};
//
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		x.Ext.init(getApplication());
//		x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
//		setTheme(R.style.AppTheme);
//		if (!this.isTaskRoot()) {// 判断此activity是不是任务控件的源Activity，“非”也就是说是被系统重新实例化出来的
//			Intent mainIntent = getIntent();
//			String action = mainIntent.getAction();
//			if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
//				finish();
//				return;
//			}
//		}
//		if (GosApplication.flag != 0) {
//			GosBaseActivity.noIDAlert(this, R.string.AppID_Toast);
//		}
//
//		// 在配置文件中选择推送类型（0：不开启推送，1：极光推送，2：百度推送。默认为0）
//		gosPushManager = new GosPushManager(GosDeploy.setPushType(), this);
//
//		setContentView(R.layout.activity_gos_user_login);
//
//
//		// 设置actionBar
//		setActionBar(false, false, R.string.app_company);
//		initView();
//		initEvent();
//	}
//
//
//
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		JPushInterface.onResume(this);
//		autoLogin();
//
//		cleanuserthing();
//	}
//
//	private void cleanuserthing() {
//
//		if (isclean) {
//			etName.setText("");
//			;
//			etPsw.setText("");
//			;
//		}
//	}
//
//	private void autoLogin() {
//
//		if (TextUtils.isEmpty(spf.getString("UserName", "")) || TextUtils.isEmpty(spf.getString("PassWord", ""))) {
//			return;
//		}
//
//		baseHandler.sendEmptyMessageDelayed(handler_key.AUTO_LOGIN.ordinal(), 1000);
//	}
//
//	private void initView() {
//		etName = (EditText) findViewById(R.id.etName);
//		etPsw = (EditText) findViewById(R.id.etPsw);
//		btnLogin = (Button) findViewById(R.id.btnLogin);
//		tvRegister = (TextView) findViewById(R.id.tvRegister);
//		tvForget = (TextView) findViewById(R.id.tvForget);
//		tvPass = (TextView) findViewById(R.id.tvPass);
//		cbLaws = (CheckBox) findViewById(R.id.cbLaws);
//
//		DotView DotView = (DotView) findViewById(R.id.dotView1);
//		llQQ = (LinearLayout) findViewById(R.id.llQQ);
//		llWechat = (LinearLayout) findViewById(R.id.llWechat);
//		String setTencentAppID = GosDeploy.setTencentAppID();
//		String setWechatAppID = GosDeploy.setWechatAppID();
//		// 判断腾讯和微信是否需要隐藏和显示
//		setWechatOrTencentIsVisable(DotView);
//		// 配置文件部署
//		btnLogin.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
//		btnLogin.setTextColor(GosDeploy.setButtonTextColor());
//
//	}
//
//	protected void setWechatOrTencentIsVisable(DotView DotView) {
//		if (!GosDeploy.setWechat()) {
//
//			llWechat.setVisibility(View.GONE);
//		}
//		if (!GosDeploy.setQQ()) {
//
//			llQQ.setVisibility(View.GONE);
//		}
//
//		if (!GosDeploy.setWechat() && !GosDeploy.setQQ()) {
//			DotView.setVisibility(View.GONE);
//		}
//	}
//
//	private void initEvent() {
//		btnLogin.setOnClickListener(this);
//		tvRegister.setOnClickListener(this);
//		tvForget.setOnClickListener(this);
//		tvPass.setOnClickListener(this);
//
//		llQQ.setOnClickListener(this);
//		llWechat.setOnClickListener(this);
//
//		cbLaws.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				String psw = etPsw.getText().toString();
//
//				if (isChecked) {
//					etPsw.setInputType(0x90);
//				} else {
//					etPsw.setInputType(0x81);
//				}
//				etPsw.setSelection(psw.length());
//			}
//		});
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btnLogin:
//
//			if (TextUtils.isEmpty(etName.getText().toString())) {
//				Toast.makeText(GosUserLoginActivity.this, R.string.toast_name_wrong, toastTime).show();
//				return;
//			}
//			if (TextUtils.isEmpty(etPsw.getText().toString())) {
//				Toast.makeText(GosUserLoginActivity.this, R.string.toast_psw_wrong, toastTime).show();
//				return;
//			}
//			baseHandler.sendEmptyMessage(handler_key.LOGIN.ordinal());
//			break;
//
//		case R.id.tvRegister:
//			intent = new Intent(GosUserLoginActivity.this, GosRegisterUserActivity.class);
//			startActivity(intent);
//
//			break;
//		case R.id.tvForget:
//			intent = new Intent(GosUserLoginActivity.this, GosForgetPasswordActivity.class);
//			startActivity(intent);
//			break;
//		case R.id.tvPass:
//
//			intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
//			startActivity(intent);
//
//			logoutToClean();
//			break;
//
//		case R.id.llQQ:
//			String tencentAPPID = GosDeploy.setTencentAppID();
//			if (TextUtils.isEmpty(tencentAPPID) || tencentAPPID.contains("your_tencent_app_id")) {
//				noIDAlert(this, R.string.TencentAPPID_Toast);
//				return;
//			} else {
//				// 启动QQ登录SDK
//				mTencent = Tencent.createInstance(GosDeploy.setTencentAppID(), this.getApplicationContext());
//			}
//
//			listener = new BaseUiListener() {
//				protected void doComplete(JSONObject values) {
//					Message msg = new Message();
//					try {
//						if (values.getInt("ret") == 0) {
//							gizThirdAccountType = GizThirdAccountType.GizThirdQQ;
//							thirdUid = values.getString("openid").toString();
//							thirdToken = values.getString("access_token").toString();
//							msg.what = handler_key.THRED_LOGIN.ordinal();
//							baseHandler.sendMessage(msg);
//						} else {
//
//							Toast.makeText(GosUserLoginActivity.this, msg.obj.toString(), toastTime).show();
//
//						}
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			};
//			mTencent.login(this, Scope, listener);
//			break;
//		case R.id.llWechat:
//			String wechatAppID = GosDeploy.setWechatAppID();
//			String wechatAppSecret = GosDeploy.setWechatAppSecret();
//			if (TextUtils.isEmpty(wechatAppID) || TextUtils.isEmpty(wechatAppSecret)
//					|| wechatAppID.contains("your_wechat_app_id")
//					|| wechatAppSecret.contains("your_wechat_app_、secret")) {
//				noIDAlert(this, R.string.WechatAppID_Toast);
//				return;
//			} else {
//
//				// 设置与WXEntryActivity共用Handler
//				setBaseHandler(baseHandler);
//
//				// 启动微信登录SDK
//				mIwxapi = WXAPIFactory.createWXAPI(this, wechatAppID, true);
//
//				// 将应用的AppID注册到微信
//				mIwxapi.registerApp(wechatAppID);
//			}
//
//			if (!(mIwxapi.isWXAppInstalled() && mIwxapi.isWXAppSupportAPI())) {
//				noIDAlert(this, R.string.No_WXApp);
//				return;
//			}
//			SendAuth.Req req = new SendAuth.Req();
//			req.scope = "snsapi_userinfo";
//			req.state = "wechat_sdk_demo_test";
//			mIwxapi.sendReq(req);
//			break;
//
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Tencent.onActivityResultData(requestCode, resultCode, data, listener);
//	}
//
//	/**
//	 * 设置云端服务回调
//	 */
//	protected void didGetCurrentCloudService(GizWifiErrorCode result,
//			java.util.concurrent.ConcurrentHashMap<String, String> cloudServiceInfo) {
//		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
//			Toast.makeText(this, toastError(result), toastTime).show();
//		}
//	}
//
//	/**
//	 * 用户登录回调
//	 */
//	@Override
//	protected void didUserLogin(GizWifiErrorCode result, String uid, String token) {
//
//		progressDialog.cancel();
//		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "User");
//		if (GosDeviceListActivity.loginStatus == 4 || GosDeviceListActivity.loginStatus == 3) {
//			return;
//		}
//		Log.i("Apptest", GosDeviceListActivity.loginStatus + "\t" + "UserLogin");
//
//		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {// 登录失败
//			Toast.makeText(GosUserLoginActivity.this, toastError(result), toastTime).show();
//
//		} else {// 登录成功
//
//			GosDeviceListActivity.loginStatus = 1;
//			Toast.makeText(GosUserLoginActivity.this, R.string.toast_login_successful, toastTime).show();
//
//			// TODO 绑定推送
//			GosPushManager.pushBindService(token);
//
//			if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())
//					&& TextUtils.isEmpty(spf.getString("thirdUid", ""))) {
//				spf.edit().putString("UserName", etName.getText().toString()).commit();
//				spf.edit().putString("PassWord", etPsw.getText().toString()).commit();
//			}
//			spf.edit().putString("Uid", uid).commit();
//			spf.edit().putString("Token", token).commit();
//
//			intent = new Intent(GosUserLoginActivity.this, GosMainActivity.class);
//			intent.putExtra("ThredLogin", true);
//			startActivity(intent);
//
//		}
//	}
//
//	/**
//	 * 解绑推送回调
//	 *
//	 * @param result
//	 */
//	protected void didChannelIDUnBind(GizWifiErrorCode result) {
//		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
//			Toast.makeText(this, toastError(result), toastTime).show();
//		}
//
//		Log.i("Apptest", "UnBind:" + result.toString());
//	};
//
//	/**
//	 * 菜单、返回键响应
//	 */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			exitBy2Click(); // 调用双击退出函数
//		}
//		return false;
//	}
//
//	/**
//	 * 双击退出函数
//	 */
//	private static Boolean isExit = false;
//
//	private void exitBy2Click() {
//		Timer tExit = null;
//		if (isExit == false) {
//			isExit = true; // 准备退出
//			String doubleClick = (String) getText(R.string.double_click);
//			Toast.makeText(this, doubleClick, toastTime).show();
//			tExit = new Timer();
//			tExit.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					isExit = false; // 取消退出
//				}
//			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
//
//		} else {
//			this.finish();
//			System.exit(0);
//		}
//	}
//
//	@Override
//	protected void didNotifyEvent(GizEventType eventType, Object eventSource, GizWifiErrorCode eventID,
//								  String eventMessage) {
//		super.didNotifyEvent(eventType, eventSource, eventID, eventMessage);
//
//		if (eventID != null && eventID.getResult() != 8316) {
//			String toastError = toastError(eventID);
//
//			TipsDialog dia = new TipsDialog(this, toastError);
//			dia.show();
//		}
//
//	}
//
//	/** 注销函数 */
//	void logoutToClean() {
//		spf.edit().putString("UserName", "").commit();
//		spf.edit().putString("PassWord", "").commit();
//		spf.edit().putString("Uid", "").commit();
//		spf.edit().putString("thirdUid", "").commit();
//
//		spf.edit().putString("Token", "").commit();
//
//		if (GosDeviceListActivity.loginStatus == 1) {
//			GosDeviceListActivity.loginStatus = 0;
//		} else {
//			GosDeviceListActivity.loginStatus = 4;
//		}
//
//	}


//}







