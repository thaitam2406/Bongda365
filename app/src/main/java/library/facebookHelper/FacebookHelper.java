package library.facebookHelper;

import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

import java.util.List;

/**
 * Created by Tam Huynh on 1/26/2015.
 */
public class FacebookHelper {
    private String TAG = getClass().getSimpleName();
    public FacebookHelper() {
    }
    public void facebookLogin(){
        Session session = Session.getActiveSession();
        if(session!=null && session.isOpened()) {
            Request.executeMyFriendsRequestAsync(session, new Request.GraphUserListCallback() {

                @Override
                public void onCompleted(List<GraphUser> users, Response response) {
                    for (GraphUser u : users) {
                        Log.d(TAG, "" + u.getId() + ": " + u.getName());
                    }
                }

            });
        }
    }
}
