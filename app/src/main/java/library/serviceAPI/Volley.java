package library.serviceAPI;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import library.serviceAPI.loader.GetHeader;

/**
 * Created by Tam Huynh on 1/19/2015.
 */
public class Volley<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<?> clazz;
    private final Response.Listener<Object> listener;

    public Volley(String url, Class<?> clazz,
                  Response.Listener<Object> listener, Response.ErrorListener errorListener,int method ) {
        super(method, url, errorListener);
        this.clazz = clazz;
        this.listener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String json = "";
        try {
            json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            if(json.equals("[]"))
                return Response.error(new ParseError());
            return (Response<T>) Response.success(gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T t) {
        listener.onResponse(t);
    }



    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        return super.setRetryPolicy(policy);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        GetHeader getHeader = new GetHeader();
        return getHeader.GetStringMap();
    }

}
