package kr.ac.hansung.thetherfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.icu.util.Calendar.*;

/**
 * Created by Jur on 2018-12-05.
 */

public class CardItem {

    private String title;
    private String contents;
    private String ranks;
    private String audiA;
    private Bitmap bitmap;
    private String overview;



    private static String image;
    private static JSONObject entity, entity2;
    private static JSONArray array, array2;
    private static ArrayList<CardItem> contacts;
    private static ArrayList<String> moviePost;


    public CardItem(String title, String contents, String rank, String audi, Bitmap bitmap, String overview) {
        this.title = title;
        this.contents = contents;
        this.ranks = rank;
        this.audiA = audi;
        this.bitmap = bitmap;
        this.overview = overview;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }
    public String getOverview() { return overview; }

    public void setOverview(String overview) { this.overview = overview; }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getRanks() { return ranks; }

    public void setRanks(String ranks) { this.ranks = ranks; }

    public void setAudiA(String audiA) { this.audiA = audiA; }

    public String getAudiA() { return audiA; }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<CardItem> createContactsList() throws IOException, JSONException, InterruptedException {

        contacts = new ArrayList<CardItem>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Calendar calendar = getInstance();
        calendar.add(Calendar.DATE, -1);  // ?ㅻ뒛 ?좎쭨?먯꽌 ?섎（瑜?類?
        String date = sdf.format(calendar.getTime());
        String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=3fd751d446aad318a5ad723cfdbb9104&targetDt="+ date;

        ConnectThread thread = new ConnectThread(url);
        thread.start();
        thread.join();

        return contacts;
    }

    public static class ConnectThread extends Thread {
        String urlStr;
        String urlStr2;
        String url3;
        Bitmap bitmap;
        OkHttpClient client = new OkHttpClient();
        public ConnectThread(String url) {
            urlStr = url;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public void run() {
            moviePost = new ArrayList<>();
            try{
                Request request = new Request.Builder()
                        .url(urlStr)
                        .get()
                        .build();
                Response response = client.newCall(request).execute();
                JSONObject jsonobject = new JSONObject(response.body().string());

                JSONObject json =  (JSONObject) jsonobject.get("boxOfficeResult");
                array = (JSONArray)json.get("dailyBoxOfficeList");

                for(int i=0; i < array.length(); i++){
                    int j = 0;
                    entity = (JSONObject)array.get(i);
                    String rank = (String) entity.get("rank");
                    String movieNm = (String) entity.get("movieNm");
                    String openDt = (String) entity.get("openDt");
                    String audiAcc = (String) entity.get("audiAcc");

                    urlStr2 = "https://api.themoviedb.org/3/search/movie?api_key=73c895f19f63e146daf91c90d90f9360&language=ko-KR&query="
                            + (String) entity.get("movieNm");
                    Request request2 = new Request.Builder()
                            .url(urlStr2)
                            .get()
                            .build();
                    Response response2 = client.newCall(request2).execute();
                    JSONObject jsonobject2 = new JSONObject(response2.body().string());

                    JSONObject json2 =  (JSONObject) jsonobject2;
                    array2 = (JSONArray)json2.get("results");
                    // Log.d("?ㅻ쭏3", urlStr2);

                    entity2 = (JSONObject)array2.get(j);
                    String image = (String) entity2.get("poster_path");
                    String overview = (String)entity2.get("overview");
                    //moviePost.add("https://image.tmdb.org/t/p/w500"+image);
                    // Log.d("?ㅻ쭏5", moviePost.get(i).toString());
                    url3 = "https://image.tmdb.org/t/p/w500" + image;
                    try {
                        URL url = new URL(url3);
                        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    contacts.add(new CardItem(rank + "위", movieNm, "개봉날짜: " + openDt, "누적 관객: " + audiAcc, bitmap, overview));
                    ++j;
                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
