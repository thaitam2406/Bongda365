package library.serviceAPI;


import model.ServiceResponse;

public interface IServiceListener {
    /*
     * Called when a request has been fisnished without canceled.
     */
    public void onCompleted(ServiceResponse result);
    public void onFail();
}
