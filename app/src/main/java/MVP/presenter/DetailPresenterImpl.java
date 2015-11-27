package MVP.presenter;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

import MVP.interactor.DetailInteractor;
import MVP.interactor.DetailNewsInteractorImpl;
import MVP.view.ModernDetailView;
import model.DetailPage;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class DetailPresenterImpl implements DetailNewsPresenter,ModernFinishGetData {
    private ModernDetailView modernDetailView;
    private DetailInteractor detailInteractor;

    public DetailPresenterImpl(ModernDetailView modernDetailView) {
        this.modernDetailView = modernDetailView;
        this.detailInteractor = new DetailNewsInteractorImpl();
    }
    @Override
    public void success(Object object) {
        DetailPage detailPage = (DetailPage) object;
        modernDetailView.setDataUI(detailPage);
        modernDetailView.hideProgress();
        modernDetailView.showToolbar();
    }

    @Override
    public void fail() {

    }

    @Override
    public void getDataDetailNews(Context context,String detailNewsId) {
        this.modernDetailView.hideToolbar();
        this.detailInteractor.getDataDetailNews(this,context,detailNewsId);
    }
}
