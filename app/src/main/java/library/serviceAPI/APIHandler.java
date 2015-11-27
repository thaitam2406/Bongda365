package library.serviceAPI;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

import model.CatVideoFootball;
import model.ContentModernPage;
import model.DetailPage;
import model.ModernNew;
import model.RankingItem;
import model.ResultMatch;
import model.ScheduleMatch;
import model.ServiceResponse;
import model.VideoFootBall;
import model.VideoGoalItem;

/**
 * Created by Tam Huynh on 1/10/2015.
 */
public class APIHandler implements Response.Listener<Object>, Response.ErrorListener {
    private IServiceListener serviceListener;
    private ServiceAction _action;
    private boolean isSuccess = false;
    private Class<?> clazz;
    private int method;
    private Context context;

    public APIHandler(IServiceListener serviceListener,Context context) {
        this.serviceListener = serviceListener;
        this.context = context;
    }

    public void executeAPI(String url,Map<String,String> param,boolean isGet,boolean isPut,ServiceAction action){
        this._action = action;
        this.clazz = getMyObjectResponse(action);
        this.method = getMethodRequest(isGet,isPut);

        VolleySingleton.getInstance(this.context).addToRequestQueue(new Volley(url,clazz,this,this, method));
    }

    private int  getMethodRequest(boolean isGet, boolean isPut) {
        if(isGet){
            return Request.Method.GET;
        }else if(!isPut){
            return Request.Method.POST;
        }else
            return Request.Method.PUT;

    }

    private Class<?> getMyObjectResponse(ServiceAction action) {
        switch (action){
            case ActionGetListVideoGoal:
                return VideoGoalItem[].class;
            case ActionGetRankingFootball:
                return RankingItem[].class;
            case ActionGetVideoFootball:
                return VideoFootBall[].class;
            case ActionGetCatVideoFootball:
                return CatVideoFootball[].class;
            case ActionGetCatNews:
                return ModernNew[].class;
            case ActionGetContentNews:
                return ContentModernPage[].class;
            case ActionGetDetailNews:
                return DetailPage.class;
            case ActionGetScheduleMatch:
                return ScheduleMatch[].class;
            case ActionGetResultMatch:
                return ResultMatch[].class;
        }
        return null;
    }
    @Override
    public void onResponse(Object o) {
        ServiceResponse response = null;
        if (o == null )
            response = new ServiceResponse(this._action, o, ResultCode.Failed);
        else {
            response = new ServiceResponse(this._action, o, ResultCode.Success);
        }
        serviceListener.onCompleted(response);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        serviceListener.onFail();
    }
}
