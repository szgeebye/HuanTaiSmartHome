package huantai.smarthome.control;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.gizwits.gizwifisdk.listener.GizWifiDeviceListener;

import org.json.JSONException;

import java.util.concurrent.ConcurrentHashMap;

import huantai.smarthome.adapter.MyFragmentPagerAdapter;
import huantai.smarthome.initial.CommonModule.GosBaseActivity;
import huantai.smarthome.initial.R;
import huantai.smarthome.utils.ConvertUtil;

public class GosDeviceControlActivity extends GosBaseActivity implements RadioGroup.OnCheckedChangeListener,
		ViewPager.OnPageChangeListener{

	private RadioGroup rg_tab_bar;
	private RadioButton rb_channel;
	private RadioButton rb_message;
	private RadioButton rb_better;
	private RadioButton rb_setting;
	private ViewPager vpager;

	private MyFragmentPagerAdapter mAdapter;

	//几个代表页面的常量
	public static final int PAGE_ONE = 0;
	public static final int PAGE_TWO = 1;
	public static final int PAGE_THREE = 2;
	public static final int PAGE_FOUR = 3;


	/** The tv MAC */
	private TextView tvMAC;

	/** The GizWifiDevice device */
	private GizWifiDevice device;

	/** The ActionBar actionBar */
	ActionBar actionBar;

	//发送状态更新广播
	private static final String KUOZHAN = "kuozhan";
	private byte[] SEND_BROAD = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x15};

	private Button btn_send;

//	@ViewInject(R.id.tv_msg)
	private TextView tv_msg;
	private final int RETURN_MSG=1;

	private String msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_control);
		initDevice();
		setActionBar(true, true, device.getProductName());
		initView();
		//开启广播
		btn_send=(Button)findViewById(R.id.btn_send);
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		btn_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				initBroadreceive();
			}
		});
		initStatusListener();

		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		bindViews();
		rb_channel.setChecked(true);
	}


	/**
	 * description:设置设备状态监听
	 * auther：joahluo
	 * time：2017/6/27 16:27
	 */
	private void initStatusListener() {
		//设置设备状态监听
		device.setListener(mListener);
		//通知设备上报数据
		initBroadreceive();
	}


	/**
	 * description：开启广播监听
	 * auther：joahluo
	 * time：2017/6/27 14:30
	 */
	private void initBroadreceive() {
		//TODO: 发送状态同步广播给服务器
		try {
			sendJson(KUOZHAN, SEND_BROAD);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void initView() {
		tvMAC = (TextView) findViewById(R.id.tvMAC);
		if (null != device) {

			tvMAC.setText(device.getMacAddress().toString());

		}
	}

	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		Log.i("Apptest", device.getDid());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * description:发送数据
	 * auther：joahluo
	 * time：2017/6/26 20:14
	 */
	private void sendJson(String key, Object value) throws JSONException {
		ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap<String, Object>();
		hashMap.put(key, value);
		device.write(hashMap, 0);
		Log.i("==", hashMap.toString());
	}


	/**
	 * description:获取设备状态
	 * auther：joahluo
	 * time：2017/6/27 15:53
	 */
	private GizWifiDeviceListener mListener = new GizWifiDeviceListener() {
		@Override
		public void didReceiveData(GizWifiErrorCode result, GizWifiDevice device, ConcurrentHashMap<String, Object> dataMap, int sn) {
			if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
				// 已定义的设备数据点，有布尔、数值和枚举型数据
				if (dataMap.get("data") != null) {
					ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) dataMap.get("data");
					// 获得kuozhan类型数据
					String msg = ConvertUtil.byteStringToHexString((byte[]) map.get("kuozhan"));
					Log.i("ListenerMsg",msg);
					tv_msg.setText(msg);
					System.out.println("接收到数据："+msg);
				}
			}

		}

	};


	private void bindViews() {
		rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
		rb_channel = (RadioButton) findViewById(R.id.rb_channel);
		rb_message = (RadioButton) findViewById(R.id.rb_message);
		rb_better = (RadioButton) findViewById(R.id.rb_better);
		rb_setting = (RadioButton) findViewById(R.id.rb_setting);
		rg_tab_bar.setOnCheckedChangeListener(this);

		vpager = (ViewPager) findViewById(R.id.vpager);
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
					break;
				case PAGE_FOUR:
					rb_setting.setChecked(true);
					break;
			}
		}
	}

}
