package util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bongda365.BongdaApplication;

public class ListenerAccessNetworkState extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (BongdaApplication.Instance().is_networkState() == Util.checkInternetConnection()) {
            if(!BongdaApplication.Instance().is_networkState())
                BongdaApplication.Instance().set_networkState(true);
        } else
            BongdaApplication.Instance().set_networkState(false);
    }


}
