package com.bongda365;

/**
 * Created by Tam Huynh on 12/19/2014.
 */
public class API {
    public static String numberItemOnPage = "20";
    public static String detailIdNews = "{detailIdNews}";
    public static String pageNumber = "{pageNumber}";
    public static String catVideo = "{catVideo}";
    public static String leagueStr = "[leagueID]";
    public static String categoryId = "[categoryId]";
    public static String lastID = "{lastID}";
    public static String lastTime = "{lastTime}";
    public static String headerThumbnail = "http://103.9.77.133/bongda365/bongda365/";
    public static String headerAPI = "http://103.9.77.133/bongda365/service.php";
    public static String getListVideoGoal = headerAPI +"?action=getArticles&categoryID=10"
            + "&lastTime="+ lastTime + "&lastID="+ lastID + "&numPerPage=" + numberItemOnPage;
    public static String getRakingFootball = headerAPI + "?action=getBXH&leagueID=" + leagueStr + "&season=1415";
    public static String getVideoFootBall = headerAPI + "?action=getVideo&category=" +
            catVideo + "&pageNumber=" + pageNumber + "&numPerPage=" + numberItemOnPage;
    public static String getCatVideoFootBall = headerAPI + "?action=getVideoCategory";

    public static String getCatNews = headerAPI + "?action=getMainCategory";

    public static String getListNews = headerAPI + "?action=getArticles&categoryID=" +
            categoryId + "&numPerPage=" + numberItemOnPage+"&lastTime="+ lastTime + "&lastID="+ lastID;

    public static String getDetailNews = headerAPI + "?action=getArticleDetail&articleID="
            + detailIdNews;
    public static String getScheduleMatch = headerAPI + "?action=getMatch&leagueID=" + leagueStr + "&season=1415&type=nextMatch";
    public static String getResultMatch = headerAPI + "?action=getMatch&leagueID=" + leagueStr + "&season=1415&type=playedMatch";
}
