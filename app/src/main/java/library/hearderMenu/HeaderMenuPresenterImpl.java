package library.hearderMenu;

import android.content.Context;

/**
 * Created by Tam Huynh on 1/22/2015.
 */
public class HeaderMenuPresenterImpl implements HeaderMenuPresenter,HeaderMenuOnFinish {
    private HeaderMenuView headerMenuView;
    private HeaderMenuInteractor headerMenuInteractor;
    private Context mContext;

    public HeaderMenuPresenterImpl(Context context,HeaderMenuView headerMenuView) {
        this.mContext = context;
        this.headerMenuView = headerMenuView;
        headerMenuInteractor = new HeaderMenuInteractorImpl();
    }

    @Override
    public void handleGetCatVideo() {
        headerMenuInteractor.exeGetCatVideo(mContext, this);
    }

    @Override
    public void onSuccess(Object object) {
        this.headerMenuView.setDataSelectorVideoCat(object);
    }
}
