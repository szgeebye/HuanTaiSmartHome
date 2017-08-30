package huantai.smarthome.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huantai.smarthome.initial.R;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class VideoFragment extends Fragment {

    public VideoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_video);
        txt_content.setText("videoFragment");
        Log.e("HEHE", "3日狗");
        return view;
    }
}
