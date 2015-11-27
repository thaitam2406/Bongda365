package MVP.interactor;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

import com.bongda365.API;

import java.util.ArrayList;
import java.util.Arrays;

import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ServiceAction;
import model.ModernNew;
import model.ServiceResponse;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class ModernCatInteractorImpl implements ModernCatInteractor, IServiceListener{
    private ModernFinishGetData modernFinishGetData;
    @Override
    public void getDataCatModern(ModernFinishGetData listener,Context context) {
        new APIHandler(this,context).executeAPI(API.getCatNews,null,true,false, ServiceAction.ActionGetCatNews);
        this.modernFinishGetData = listener;

    }

    @Override
    public void onCompleted(ServiceResponse result) {
        ModernNew[] modernNews = (ModernNew[]) result.getData();
        ArrayList<ModernNew> arrayModernNew = new ArrayList<ModernNew>();
        arrayModernNew.addAll(Arrays.asList(modernNews));
        this.modernFinishGetData.success(arrayModernNew);
    }

    @Override
    public void onFail() {

    }
}
