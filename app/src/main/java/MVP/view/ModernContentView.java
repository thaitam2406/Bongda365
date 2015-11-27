package MVP.view;

import java.util.ArrayList;

import model.ContentModernPage;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public interface ModernContentView {
    public void setAdapter(ArrayList<ContentModernPage> contentModernPages);
    public void showProgress();
    public void hideProgress();
}
