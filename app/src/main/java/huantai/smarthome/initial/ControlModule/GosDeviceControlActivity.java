package huantai.smarthome.initial.ControlModule;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.gizwits.gizwifisdk.api.GizWifiDevice;
import huantai.smarthome.initial.CommonModule.GosBaseActivity;
import huantai.smarthome.initial.R;


import org.json.JSONException;

import java.util.concurrent.ConcurrentHashMap;

public class GosDeviceControlActivity extends GosBaseActivity {

	/** The tv MAC */
	private TextView tvMAC;

	/** The GizWifiDevice device */
	private GizWifiDevice device;

	/** The ActionBar actionBar */
	ActionBar actionBar;

	//发送状态更新广播
	private static final String KUOZHAN = "kuozhan";
	private byte[] SEND_BROAD = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0x15};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gos_device_control);
		initDevice();
		setActionBar(true, true, device.getProductName());
		initView();
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

}
