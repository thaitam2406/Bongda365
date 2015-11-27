package library.hearderMenu;

import android.content.Context;

import com.bongda365.API;

import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ResultCode;
import library.serviceAPI.ServiceAction;
import model.ServiceResponse;

/**
 * Created by Tam Huynh on 1/22/2015.
 */
public class HeaderMenuInteractorImpl implements HeaderMenuInteractor,IServiceListener {
    private HeaderMenuOnFinish headerMenuOnFinish;
    @Override
    public void onCompleted(ServiceResponse result) {
        if(result.getAction() == ServiceAction.ActionGetCatVideoFootball && result.getCode() == ResultCode.Success){
            headerMenuOnFinish.onSuccess(result.getData());
        }
    }

    @Override
    public void onFail() {

    }

    @Override
    public void exeGetCatVideo(Context context, HeaderMenuOnFinish headerMenuOnFinish) {
        // get cat from server
        this.headerMenuOnFinish = headerMenuOnFinish;
        new APIHandler(this,context).executeAPI(API.getCatVideoFootBall,null,true,false, ServiceAction.ActionGetCatVideoFootball);
    }
}
