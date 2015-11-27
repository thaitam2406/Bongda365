package MVP.presenter;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

import java.util.ArrayList;

import MVP.interactor.ModernCatInteractor;
import MVP.interactor.ModernCatInteractorImpl;
import MVP.view.ModernStyleCategoryView;
import model.ModernNew;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class ModernCatPresenterImpl implements ModernCatPresenter,ModernFinishGetData {
    private ModernStyleCategoryView modernStyleCategoryView;
    private ModernCatInteractor modernInteractor;

    public ModernCatPresenterImpl(ModernStyleCategoryView modernStyleCategoryView) {
        this.modernStyleCategoryView = modernStyleCategoryView;
        this.modernInteractor = new ModernCatInteractorImpl();
    }


    @Override
    public void getDataCatModern(Context context) {
        modernStyleCategoryView.showProgress();
        modernInteractor.getDataCatModern(this,context);

    }

    @Override
    public void success(Object object) {
        ArrayList<ModernNew> modernNews = (ArrayList<ModernNew>) object;
        int i = (modernNews.size() - 5);
        if (i % 6 == 0)
            i =  i / 6 + 1;
        else
            i = i / 6 + 2;
        modernStyleCategoryView.setTotalPage(String.valueOf(i));
        modernStyleCategoryView.setPageNumber("1");
        modernStyleCategoryView.setAdapter(modernNews);
        modernStyleCategoryView.hideProgress();
    }

    @Override
    public void fail() {

    }
}
