package MVP.interactor;

import android.content.Context;
import android.interfaces.ModernFinishGetData;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public interface DetailInteractor {
    public void getDataDetailNews(ModernFinishGetData listener, Context context,String detailNewsId);
}
