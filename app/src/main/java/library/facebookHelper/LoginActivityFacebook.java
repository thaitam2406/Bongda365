package library.facebookHelper;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import model.FacebookUserInfo;
import util.ShareManagement;

public class LoginActivityFacebook extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

        Session s = new Session(getApplicationContext());
        Session.OpenRequest request = new Session.OpenRequest(this);
        String[] facebookPermission = new String[] {"publish_actions"};
        request.setPermissions(facebookPermission);
//        request.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO); // <-- this is the important line
        s.openForPublish(request);
        Session.setActiveSession(s);
        request.setCallback(new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
            }
        });
        Session.openActiveSession(this, true, new Session.StatusCallback() {

            // callback when session changes state
            @Override
            public void call(Session session, SessionState state,
                             Exception exception) {
                if (session.isOpened()) {

                    // make request to the /me API
                    Request.executeMeRequestAsync(session,
                            new Request.GraphUserCallback() {

                                // callback after Graph API response with user
                                // object
                                @Override
                                public void onCompleted(GraphUser user,
                                                        Response response) {
                                    if (user != null) {
                                        ShareManagement shareManagement = new ShareManagement(LoginActivityFacebook.this);
                                        FacebookUserInfo facebookUserInfo = new FacebookUserInfo();
                                        facebookUserInfo.setUserName(user.getFirstName() + " " +user.getLastName());
                                        facebookUserInfo.setThumbnail("http://graph.facebook.com/"+user.getId()+"/picture?type=large");
                                        shareManagement.saveFacebook(facebookUserInfo);
                                        finish();
                                    }
                                }
                            });
                }else{
                    switch (state){
                        case CLOSED_LOGIN_FAILED:
                            finish();
                            break;
                    }
                }
            }
        });

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

}
