package library.facebookHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.tamhuynh.bongda365.R;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import util.ShareManagement;

public class DialogShareFacebook extends DialogFragment {

	// layout elements
	private Button btnCancel, btnSend;
	private EditText sharedString;
	private TextView usernameFacebook;
    private ProgressBar progressbar;
	
	private String mSharedTitle,mSharedImage,mShareCaption;
    public static String TAG_SHARED_TEXT = "TAG_SHARED_TEXT";
    public static String TAG_SHARED_IMAGE = "TAG_SHARED_IMAGE";
    public static String TAG_SHARED_CAPTION = "TAG_SHARED_CAPTION";
	public DialogShareFacebook() {
		// default constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub


		View vi = inflater.inflate(R.layout.facebook_view,
				container, false);
        initUI(vi);

        try{
            mSharedTitle = getArguments().getString(TAG_SHARED_TEXT);
            mSharedImage = getArguments().getString(TAG_SHARED_IMAGE);
            mShareCaption = getArguments().getString(TAG_SHARED_CAPTION);
        }catch (Exception e){
            e.printStackTrace();
        }
		sharedString.setText(Html.fromHtml(mSharedTitle));
		usernameFacebook.setText(new ShareManagement(getActivity()).loadFacebook().getUserName());

		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogShareFacebook.this.dismiss();
			}
		});

		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                progressbar.setVisibility(View.VISIBLE);
				postToWall();
			}
		});

		return vi;
	}

    private void initUI(View vi) {
        progressbar = (ProgressBar) vi.findViewById(R.id.progressbar);
        btnCancel = (Button) vi.findViewById(R.id.btnCancel);
        btnSend = (Button) vi.findViewById(R.id.btnSend);
        usernameFacebook = (TextView) vi.findViewById(R.id.usernameFacebook);
        sharedString = (EditText) vi.findViewById(R.id.sharedString);
    }

    private void postToWall() {
		// TODO Auto-generated method stub
		if (mSharedTitle.matches("")) {
			Toast.makeText(getActivity(), "Nothing to share", Toast.LENGTH_SHORT).show();
		} else {
			if (!mSharedTitle.matches("null")) {
                publishStory();
			}
		}
	}

    public void postStatusMessage() {
        if (checkPermissions()) {
            Session s = Session.getActiveSession();
            Request request = Request.newStatusUpdateRequest(s, "Testing App For genral purpose", new Request.Callback()
            {
                @Override
                public void onCompleted(Response response)
                {
                    if (response.getError() == null)
                        Toast.makeText(getActivity(), "Đã chia sẻ bài viết", Toast.LENGTH_LONG)
                                .show();
                    dismiss();

                }
            });
            request.executeAsync();
        } else {
            requestPermissions();
        }
    }
    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

    private static String message = "Sample status posted from android app";
    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
            postStatusMessage();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void publishStory() {
        Session session = Session.getActiveSession();

        if (session != null) {

            Bundle params = new Bundle();
            params.putString("caption", mShareCaption);
            params.putString("message", mSharedTitle.replace("<p>","!").replace("</p>",""));
            params.putString("picture", mSharedImage);
            params.putString("link","http://bongdaplus.vn/");

            Request request = new Request(Session.getActiveSession(), "me/feed", params, HttpMethod.POST);
            request.setCallback(new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    progressbar.setVisibility(View.GONE);
                    if (response.getError() == null) {
                        // Tell the user success!
                        if (response.getError() == null)
                            Toast.makeText(getActivity(), "Đã chia sẻ bài viết", Toast.LENGTH_LONG)
                                    .show();
                        dismiss();
                    }else
                        Toast.makeText(getActivity(), "Đã xảy ra lỗi", Toast.LENGTH_LONG)
                                .show();
                }
            });
            request.executeAsync();
        }
    }


    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
}
