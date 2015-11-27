package view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tamhuynh.bongda365.R;

/**
 * Created by Tam Huynh on 12/22/2014.
 */
public class SlidingMenuFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sliding_menu,null);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
