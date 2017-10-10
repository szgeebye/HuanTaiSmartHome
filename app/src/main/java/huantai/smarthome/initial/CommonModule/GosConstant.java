package huantai.smarthome.initial.CommonModule;

import android.net.wifi.ScanResult;

import com.gizwits.gizwifisdk.api.GizDeviceSharingInfo;
import com.gizwits.gizwifisdk.api.GizUserInfo;

import java.util.ArrayList;
import java.util.List;

public class GosConstant {

	public static List<ScanResult> ssidList = new ArrayList<ScanResult>();

	public static int nowPager = -1;

	public static List<GizUserInfo> mybindUsers = new ArrayList<GizUserInfo>();

	public static List<GizDeviceSharingInfo> mydeviceSharingInfos = new ArrayList<GizDeviceSharingInfo>();

	public static List<GizDeviceSharingInfo> newmydeviceSharingInfos = new ArrayList<GizDeviceSharingInfo>();

	public static boolean isEdit = false;

	public static String mozu = "https://item.taobao.com/item.htm?spm=686.1000925.0.0.nPcYfD&id=542479181481";

	// 设备热点默认密码
	public static final String SoftAP_PSW = "123456789";
	
	
	// 设备热点默认前缀
	public static final String SoftAP_Start = "XPG-GAgent";

	public static String Product_Key = "a5c5ddfdf47c4542ac23cd135cf55310";
	public static String Product_Secret = "031e8d83352a4fa0ba7df040e86dca23";

}
