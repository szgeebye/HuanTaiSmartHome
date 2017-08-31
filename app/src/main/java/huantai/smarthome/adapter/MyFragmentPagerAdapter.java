package huantai.smarthome.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import huantai.smarthome.control.DeviceFragment;
import huantai.smarthome.control.GosDeviceControlActivity;
import huantai.smarthome.control.HomeFragment;
import huantai.smarthome.control.MainActivity;
import huantai.smarthome.control.MineFragment;
import huantai.smarthome.control.VideoFragment;

/**
 * Created by Jay on 2015/8/31 0031.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGER_COUNT = 4;
    private HomeFragment homeFragment = null;
    private DeviceFragment DeviceFragment = null;
    private VideoFragment VideoFragment = null;
    private MineFragment MineFragment = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        homeFragment = new HomeFragment();
        DeviceFragment = new DeviceFragment();
        VideoFragment = new VideoFragment();
        MineFragment = new MineFragment();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = homeFragment;
                break;
            case MainActivity.PAGE_TWO:
                fragment = DeviceFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = VideoFragment;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = MineFragment;
                break;
        }
        return fragment;
    }


}

