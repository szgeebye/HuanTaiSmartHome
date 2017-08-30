package huantai.smarthome.initial.UserModule;

import com.google.gson.Gson;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import huantai.smarthome.bean.UserBackInfo;
import huantai.smarthome.bean.UserData;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.Encryption;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.xutils.BuildConfig;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

@SuppressLint("HandlerLeak")
public class GosRegisterUserActivity extends GosUserModuleBaseActivity
		implements OnClickListener {


	private EditText et_name;
	private EditText t_psw;
	private EditText et_Code;
	private EditText et_psw;
	private Button btn_register;
	private Button btnGetCode;
	private String checkPsw;
	private String psw;
	private String userName;
	private Encryption encryption;
	private String Bmob_Application_ID = "0756eb2fc150aeed48cd454c6e74da3e";
	private String phoneNumber;
	int smsId = 123;
	private String smsCode;

	private enum handler_key{

		/**
		 * 注册成功
		 */
		REGISTER_SUCCESS,
		/**
		 * 注册失败.
		 */
		REGISTER_FAIL,
		/**
		 * 联网失败.
		 */
		CONNECT_FAIL,
		/**
		 * 验证验证码
		 */
		VERFYCOOD,
		/**
		 * 发送验证码
		 */
		SENDCOOD,




	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			handler_key key = handler_key.values()[msg.what];
			switch (key){
				case SENDCOOD:
					Toast.makeText(GosRegisterUserActivity.this, "验证码发送成功", Toast.LENGTH_SHORT).show();
					break;
				case VERFYCOOD:
					Toast.makeText(GosRegisterUserActivity.this, "验证码验证成功", Toast.LENGTH_SHORT).show();
					break;
				case REGISTER_SUCCESS:
					Toast.makeText(GosRegisterUserActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
					finish();
					break;
				case REGISTER_FAIL:
					Toast.makeText(GosRegisterUserActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
					break;
				case CONNECT_FAIL:
					Toast.makeText(GosRegisterUserActivity.this, "服务器异常，请稍后重试", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_register_user);
		x.Ext.init(getApplication());
		x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
		// 设置ActionBar
		setActionBar(true, true, R.string.register);

		//初始化短信验证SDK
		BmobSMS.initialize(getApplicationContext(),Bmob_Application_ID);

		initView();
		initEvent();
	}

	private void initEvent() {
		btn_register.setOnClickListener(this);
		btnGetCode.setOnClickListener(this);
	}

	private void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_Code = (EditText) findViewById(R.id.et_Code);
		et_psw = (EditText) findViewById(R.id.et_psw);
		btn_register = (Button) findViewById(R.id.btn_register);
		btnGetCode = (Button) findViewById(R.id.btnGetCode);
		//加密工具
		encryption = new Encryption();

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_register:
				phoneNumber = et_name.getText().toString();
				smsCode = et_Code.getText().toString();
				psw = et_psw.getText().toString();
			if (TextUtils.isEmpty(phoneNumber)) {
				Toast.makeText(GosRegisterUserActivity.this,
						R.string.toast_name_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
			if (smsCode.length() != 6) {
				Toast.makeText(GosRegisterUserActivity.this,
						R.string.no_getcode, Toast.LENGTH_SHORT).show();
				return;
			}
			if (TextUtils.isEmpty(psw)) {
				Toast.makeText(GosRegisterUserActivity.this,
						R.string.toast_psw_wrong, Toast.LENGTH_SHORT).show();
				return;
			}
				verifyCode();
//				register();
				break;
			case R.id.btnGetCode:
				phoneNumber = et_name.getText().toString();
				sendCode();
			break;
		}
	}

	//验证验证码
	private void verifyCode() {

		BmobSMS.verifySmsCode(getApplicationContext(),phoneNumber, smsCode, new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				// TODO Auto-generated method stub
				if(ex==null){//短信验证码已验证成功
					Message msg = new Message();
					msg.what = handler_key.VERFYCOOD.ordinal();
					handler.sendMessage(msg);
					Log.i("bmob", "验证通过");
					register();
				}else{
					Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
				}
			}
		});
	}

	//发送验证码
	private void sendCode() {
		BmobSMS.requestSMSCode(getApplicationContext(), phoneNumber,"默认模板",new RequestSMSCodeListener() {

			@Override
			public void done(Integer smsId,BmobException ex) {
				// TODO Auto-generated method stub
				if(ex==null){//验证码发送成功
					GosRegisterUserActivity.this.smsId = smsId;
					Message msg = new Message();
					msg.what = handler_key.SENDCOOD.ordinal();
					handler.sendMessage(msg);
					Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
				}else{
					Log.i("bmob", "发送失败："+ex.toString());
				}
			}
		});
	}

//	Message msg = handler.obtainMessage();
	private void register() {

		phoneNumber = et_name.getText().toString();
		Thread registerThread = new Thread(new registerThread());
		registerThread.start();

//		userName = et_name.getText().toString();
//		psw = et_Code.getText().toString();
//		checkPsw = et_psw.getText().toString();
//		if(psw.equals(checkPsw)){
//			Thread registerThread = new Thread(new registerThread());
//			registerThread.start();
//		}else{
//			msg.what = handler_key.PSW_FAIL.ordinal();
//		}

	}
	class registerThread implements Runnable{

		@Override
		public void run() {
			Gson gson = new Gson();
			//密码加密
			psw = encryption.encryption_data(psw);
			String jsonSend = gson.toJson(new UserData(phoneNumber, psw));
//			RequestParams params = new RequestParams(NetConstant.formatUrl("http://39.108.151.208:9000/user/register/"));
			RequestParams params = new RequestParams("http://39.108.151.208:9000/user/register/");
			params.addHeader("Content-type","application/x-www-form-urlencoded");
			params.setCharset("UTF-8");
			params.setAsJsonContent(true);
			params.setBodyContent(jsonSend);

			x.http().post(params, callback);
		}
	}

	private  Callback.CommonCallback<String> callback = new Callback.CommonCallback<String>() {
		@Override
		public void onSuccess(String result) {
			//获取到数据
			String jsonBack = result;
			UserBackInfo userBackInfo = new Gson().fromJson(jsonBack, UserBackInfo.class);
			//处理数据
			boolean info = userBackInfo.success;
			if (info) {
				Message msg = new Message();
				msg.what = handler_key.REGISTER_SUCCESS.ordinal();
				handler.sendMessage(msg);
			} else {
				Message msg = new Message();
				msg.what = handler_key.REGISTER_FAIL.ordinal();
				handler.sendMessage(msg);
			}
		}

		@Override
		public void onError(Throwable ex, boolean isOnCallback) {
			Message msg = new Message();
			msg.what = handler_key.CONNECT_FAIL.ordinal();
			handler.sendMessage(msg);
		}

		@Override
		public void onCancelled(CancelledException cex) {

		}

		@Override
		public void onFinished() {

		}
	};
	
	
	
	
	
	
	
	
	
	
//	/** The et Name */
//	private EditText etName;
//
//	/** The btn GetCode */
//	private Button btnGetCode;
//
//	/** The et Code */
//	private EditText etCode;
//
//	/** The et Psw */
//	private EditText etPsw;
//
//	/** The btn Rrgister */
//	private Button btnRrgister;
//
//	/** The cb Laws */
//	private CheckBox cbLaws;
//
//	/** The Button Drawable */
//	GradientDrawable drawable;
//
//	/**
//	 * 验证码重发倒计时
//	 */
//	int secondleft = 60;
//
//	/**
//	 * The timer.
//	 */
//	Timer timer;
//
//	/** 数据变量 */
//	String name, code, psw;
//
//	private enum handler_key {
//
//		/** 获取验证码. */
//		GETCODE,
//
//		/** 提示信息 */
//		TOAST,
//
//		/** 手机验证码发送成功. */
//		SENDSUCCESSFUL,
//
//		/**
//		 * 倒计时通知
//		 */
//		TICK_TIME,
//
//		/** 注册 */
//		REGISTER,
//	}
//
//	Handler handler = new Handler() {
//		public void handleMessage(android.os.Message msg) {
//			super.handleMessage(msg);
//			handler_key key = handler_key.values()[msg.what];
//			switch (key) {
//			case GETCODE:
//				progressDialog.show();
//				String AppSecret = GosDeploy.setAppSecret();
//				GizWifiSDK.sharedInstance().requestSendPhoneSMSCode(AppSecret,
//						msg.obj.toString());
//
//				break;
//			case TOAST:
//				Toast.makeText(GosRegisterUserActivity.this,
//						msg.obj.toString(), toastTime).show();
//				String successfulText = (String) getText(R.string.register_successful);
//
//				if (msg.obj.toString().equals(successfulText)) {
//					// spf.edit().putString("UserName", name).commit();
//					// spf.edit().putString("PassWord", psw).commit();
//					isclean = true;
//					finish();
//				}
//				break;
//			case SENDSUCCESSFUL:
//				etName.setEnabled(false);
//				etName.setTextColor(getResources().getColor(
//						R.color.text_gray_light));
//				isStartTimer();
//
//				break;
//
//			case TICK_TIME:
//				String getCodeAgain = getString(R.string.getcode_again);
//				String timerMessage = getString(R.string.timer_message);
//				secondleft--;
//				if (secondleft <= 0) {
//					timer.cancel();
//					btnGetCode.setBackgroundDrawable(drawable);
//					btnGetCode.setTextColor(GosDeploy.setButtonTextColor());
//					btnGetCode.setEnabled(true);
//					btnGetCode.setText(getCodeAgain);
//				} else {
//					btnGetCode.setText(secondleft + timerMessage);
//				}
//				break;
//			case REGISTER:
//				progressDialog.show();
//				GizWifiSDK.sharedInstance().registerUser(name, psw, code,
//						GizUserAccountType.GizUserPhone);
//				break;
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_gos_register_user);
//		// 设置ActionBar
//		setActionBar(true, true, R.string.register);
//		initView();
//		initEvent();
//	}
//
//	private void initView() {
//		etName = (EditText) findViewById(R.id.etName);
//		btnGetCode = (Button) findViewById(R.id.btnGetCode);
//		etCode = (EditText) findViewById(R.id.etCode);
//		etPsw = (EditText) findViewById(R.id.etPsw);
//		btnRrgister = (Button) findViewById(R.id.btnRrgister);
//		cbLaws = (CheckBox) findViewById(R.id.cbLaws);
//
//		// 配置文件部署
//		drawable = (GradientDrawable) GosDeploy.setButtonBackgroundColor();
//		drawable.setCornerRadius(30);
//		btnGetCode.setBackgroundDrawable(drawable);
//		btnRrgister.setBackgroundDrawable(GosDeploy.setButtonBackgroundColor());
//		btnRrgister.setTextColor(GosDeploy.setButtonTextColor());
//
//	}
//
//	private void initEvent() {
//		final Timer etTimer = new Timer();
//		etTimer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				etName.requestFocus();
//				InputMethodManager imm = (InputMethodManager) etName
//						.getContext().getSystemService(
//								Context.INPUT_METHOD_SERVICE);
//				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
//				etTimer.cancel();
//
//			}
//		}, 500);
//
//		btnGetCode.setOnClickListener(this);
//		btnRrgister.setOnClickListener(this);
//
//		cbLaws.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
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
//		case R.id.btnGetCode:
//			name = etName.getText().toString();
//			if (TextUtils.isEmpty(name)) {
//				Toast.makeText(GosRegisterUserActivity.this,
//						R.string.toast_name_wrong, toastTime).show();
//				return;
//			}
//			Message msg = new Message();
//			msg.obj = name;
//			msg.what = handler_key.GETCODE.ordinal();
//			handler.sendMessage(msg);
//			break;
//
//		case R.id.btnRrgister:
//			name = etName.getText().toString();
//			code = etCode.getText().toString();
//			psw = etPsw.getText().toString();
//			if (TextUtils.isEmpty(name)) {
//				Toast.makeText(GosRegisterUserActivity.this,
//						R.string.toast_name_wrong, toastTime).show();
//				return;
//			}
//			if (code.length() != 6) {
//				Toast.makeText(GosRegisterUserActivity.this,
//						R.string.no_getcode, toastTime).show();
//				return;
//			}
//			if (TextUtils.isEmpty(psw)) {
//				Toast.makeText(GosRegisterUserActivity.this,
//						R.string.toast_psw_wrong, toastTime).show();
//				return;
//			}
//			/*
//			 * if (psw.length() < 6) {
//			 * Toast.makeText(GosRegisterUserActivity.this,
//			 * R.string.toast_psw_short, toastTime).show(); return; }
//			 */
//			handler.sendEmptyMessage(handler_key.REGISTER.ordinal());
//			break;
//		}
//	}
//
//	/** 手机验证码回调 */
//	@Override
//	protected void didRequestSendPhoneSMSCode(GizWifiErrorCode result,
//			String token) {
//		progressDialog.cancel();
//		Message msg = new Message();
//		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
//			msg.what = handler_key.TOAST.ordinal();
//			msg.obj = toastError(result);
//			handler.sendMessage(msg);
//		} else {
//			handler.sendEmptyMessage(handler_key.SENDSUCCESSFUL.ordinal());
//			msg.what = handler_key.TOAST.ordinal();
//			String sendSuccessful = (String) getText(R.string.send_successful);
//			msg.obj = sendSuccessful;
//			handler.sendMessage(msg);
//		}
//	}
//
//	/** 用户注册回调 */
//	@Override
//	protected void didRegisterUser(GizWifiErrorCode result, String uid,
//			String token) {
//		progressDialog.cancel();
//		if (GizWifiErrorCode.GIZ_SDK_SUCCESS != result) {
//			Message msg = new Message();
//			msg.what = handler_key.TOAST.ordinal();
//			msg.obj = toastError(result);
//			handler.sendMessage(msg);
//		} else {
//			Message msg = new Message();
//			msg.what = handler_key.TOAST.ordinal();
//			String registerSuccessful = (String) getText(R.string.register_successful);
//			msg.obj = registerSuccessful;
//			handler.sendMessage(msg);
//		}
//	}
//
//	/**
//	 * 倒计时
//	 */
//	public void isStartTimer() {
//		btnGetCode.setEnabled(false);
//		btnGetCode.setBackgroundResource(R.drawable.btn_getcode_shape_gray);
//		secondleft = 60;
//		timer = new Timer();
//		timer.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				handler.sendEmptyMessage(handler_key.TICK_TIME.ordinal());
//			}
//		}, 1000, 1000);
//	}

}
