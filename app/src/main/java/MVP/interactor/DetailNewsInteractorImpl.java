package MVP.interactor;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

import com.bongda365.API;

import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ServiceAction;
import model.DetailPage;
import model.ServiceResponse;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class DetailNewsInteractorImpl implements DetailInteractor, IServiceListener {
    private ModernFinishGetData modernFinishGetData;

    @Override
    public void getDataDetailNews(ModernFinishGetData listener, Context context, String detailNewsId) {
        new APIHandler(this, context).executeAPI(API.getDetailNews.replace(API.detailIdNews, detailNewsId), null, true, false, ServiceAction.ActionGetDetailNews);
        this.modernFinishGetData = listener;
    }

    @Override
    public void onCompleted(ServiceResponse result) {
        if (result != null) {
            DetailPage detailPage = (DetailPage) result.getData();
            this.modernFinishGetData.success(detailPage);
        }else
            this.modernFinishGetData.fail();
    }

    @Override
    public void onFail() {
        this.modernFinishGetData.fail();
    }
}
