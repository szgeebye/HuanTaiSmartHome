package huantai.smarthome.initial;

import android.app.Application;

import com.orm.SugarContext;

public class GosApplication extends Application {

	public static int flag = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		SugarContext.init(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		SugarContext.terminate();

	}

}
