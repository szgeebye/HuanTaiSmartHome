package huantai.smarthome.control;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huantai.smarthome.initial.R;

/**
 * Created by Jay on 2015/8/28 0028.
 */
public class MineFragment extends Fragment {

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_mine);
        txt_content.setText("MineFragment");
        return view;
    }
}
