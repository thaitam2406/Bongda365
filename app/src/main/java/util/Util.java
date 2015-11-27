package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TypedValue;
import android.widget.Toast;

import com.bongda365.BongdaApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tam Huynh on 1/7/2015.
 */
public class Util {
    public static boolean checkInternetConnection() {
        final ConnectivityManager conMgr = (ConnectivityManager) BongdaApplication
                .Instance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // notify user you are online
            return true;
        } else {
            // notify user you are not online
            return false;
        }
    }

    public static String getHourAgo(String time) {
        String hour = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date past = null;
        try {
            past = format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date now = new Date();

        // System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() -
        // past.getTime()) + " milliseconds ago");
        // System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() -
        // past.getTime()) + " minutes ago");
        System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime()
                - past.getTime())
                + " hours ago");
        // System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() -
        // past.getTime()) + " days ago");
        hour = String.valueOf(TimeUnit.MILLISECONDS.toHours(now.getTime()
                - past.getTime()));
        return hour;
    }

    public static String getDayAgo(String time) {
        String day = "";

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date past = null;
        try {
            past = format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date now = new Date();

        // System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() -
        // past.getTime()) + " milliseconds ago");
        // System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime() -
        // past.getTime()) + " minutes ago");
        // System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() -
        // past.getTime()) + " hours ago");
        System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime()
                - past.getTime())
                + " days ago");
        day = String.valueOf(TimeUnit.MILLISECONDS.toDays(now.getTime()
                - past.getTime()));
        return day;
    }

    public static String getMinuteAgo(String time) {
        String minute = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date past = null;
        try {
            past = format.parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Date now = new Date();

        // System.out.println(TimeUnit.MILLISECONDS.toMillis(now.getTime() -
        // past.getTime()) + " milliseconds ago");
        System.out.println(TimeUnit.MILLISECONDS.toMinutes(now.getTime()
                - past.getTime())
                + " minutes ago");
        // System.out.println(TimeUnit.MILLISECONDS.toHours(now.getTime() -
        // past.getTime()) + " hours ago");
        // System.out.println(TimeUnit.MILLISECONDS.toDays(now.getTime() -
        // past.getTime()) + " days ago");
        minute = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(now.getTime()
                - past.getTime()));
        return minute;
    }
    public static String parseTime(String dateTime){
        int day = Integer.parseInt(getDayAgo(dateTime));
        if (getDayAgo(dateTime).equals("0")) {
            if (getHourAgo(dateTime).equals(
                    "0"))
                return getMinuteAgo(dateTime)
                        + " phút trước";
            else
                return getHourAgo(dateTime)
                        + " giờ trước";
        } else if (day < 31)
            return getDayAgo(dateTime)
                    + " ngày trước";
        else
            return  dateTime;
    }
    public static int getResByTheme(Context context, int res) {
        TypedValue a = new TypedValue();
        // bottom bar background
        context.getTheme().resolveAttribute(res,a,true);
        if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT
                && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
            // windowBackground is a color
            return a.data;
        } else {
            // drawable maybe :v
            return a.resourceId;
        }
    }

    public static void showToast(Context context, String sms){
        Toast.makeText(context, sms, Toast.LENGTH_LONG).show();
    }
}
