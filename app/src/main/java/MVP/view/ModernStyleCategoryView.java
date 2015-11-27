package MVP.view;

import java.util.ArrayList;

import model.ModernNew;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public interface ModernStyleCategoryView {
    public void setAdapter(ArrayList<ModernNew> modernNews);
    public void showProgress();
    public void hideProgress();
    public void setPageNumber(String number);
    public void setTotalPage(String total);
    public void hideFooterLine();
    public void showFooterLine();
}
