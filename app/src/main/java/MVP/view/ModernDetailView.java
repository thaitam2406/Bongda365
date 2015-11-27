package MVP.view;

import model.DetailPage;

/**
 * Created by Tam Huynh on 1/6/2015.
 */
public interface ModernDetailView {
    public void setDataUI(DetailPage detailPage);
    public void showProgress();
    public void hideProgress();
    public void hideToolbar();
    public void showToolbar();
}
