package MVP.presenter;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

import java.util.ArrayList;

import MVP.interactor.ModernContentInteractor;
import MVP.interactor.ModernContentInteractorImpl;
import MVP.view.ModernContentView;
import model.ContentModernPage;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class ModernContentPresenterImpl implements ModernContentPresenter,ModernFinishGetData {
    private ModernContentView modernContentView;
    private ModernContentInteractor modernContentInteractor;

    public ModernContentPresenterImpl(ModernContentView modernContentView) {
        this.modernContentView = modernContentView;
        this.modernContentInteractor = new ModernContentInteractorImpl();
    }


    @Override
    public void getDataContentModern(Context context, String contentID) {
        modernContentView.showProgress();
        modernContentInteractor.getDataContentModern(this, context, contentID);

    }

    @Override
    public void success(Object object) {
        ArrayList<ContentModernPage> contentModernPages = (ArrayList<ContentModernPage>) object;
        modernContentView.setAdapter(contentModernPages);
        modernContentView.hideProgress();
    }

    @Override
    public void fail() {

    }
}
