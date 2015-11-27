package MVP.interactor;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

import com.bongda365.API;

import java.util.ArrayList;
import java.util.Arrays;

import library.serviceAPI.APIHandler;
import library.serviceAPI.IServiceListener;
import library.serviceAPI.ServiceAction;
import model.ContentModernPage;
import model.ServiceResponse;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public class ModernContentInteractorImpl implements ModernContentInteractor, IServiceListener{
    private ModernFinishGetData modernFinishGetData;
    private String lastID = "";
    private String lastTime = "";
    @Override
    public void getDataContentModern(ModernFinishGetData listener,Context context, String contentID) {
       /* ArrayList<ModernNew> arrayModernNew = new ArrayList<ModernNew>();
        ModernNew cat = new ModernNew();
        cat.setId(1);
        cat.setName("test");
        cat.setUrl("http://www.slate.com/content/dam/slate/blogs/the_spot/2014/07/13/argentina_germany_2014_world_cup_lionel_messi_is_sad/452110974-argentinas-forward-and-captain-lionel-messi-reacts.jpg.CROP.promo-mediumlarge.jpg");
        for(int i = 0; i < 7; i++)
            arrayModernNew.add(cat);*/
        new APIHandler(this,context).executeAPI(API.getListNews.replace(API.categoryId,contentID)
                .replace(API.lastID, this.lastID).replace(API.lastTime, this.lastTime)
                ,null,true,false, ServiceAction.ActionGetContentNews);
        this.modernFinishGetData = listener;

    }

    @Override
    public void onCompleted(ServiceResponse result) {
        ContentModernPage[] contentModernPages = (ContentModernPage[]) result.getData();
        initParam(contentModernPages);
        ArrayList<ContentModernPage> arrayContentModernPage = new ArrayList<ContentModernPage>();
        arrayContentModernPage.addAll(Arrays.asList(contentModernPages));
        this.modernFinishGetData.success(arrayContentModernPage);
    }

    private void initParam(ContentModernPage[] contentModernPages) {
        this.lastID = contentModernPages[contentModernPages.length - 1].getId();
        this.lastTime = contentModernPages[contentModernPages.length - 1].getDateTime(true).replace(" ","_");
    }

    @Override
    public void onFail() {

    }
}
