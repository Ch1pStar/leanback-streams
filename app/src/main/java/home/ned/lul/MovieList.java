package home.ned.lul;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MovieList {
    private static final String TAG = "MovieList";
    public static final String MOVIE_CATEGORY[] = {
            "TV",
            "Network Stream"
    };

    public static List<Movie> list;

    public static Map<String,  ArrayList<Movie>> categories;

    public static Map<String,  ArrayList<Movie>> setupMovies(Context context) {
        int color[] = {
                ContextCompat.getColor(context, R.color.gold),
                ContextCompat.getColor(context, R.color.red),
                ContextCompat.getColor(context, R.color.blue),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black)
        };

        String cardImageUrl[] = {
                "http://www.bdfbg.com/wp-content/uploads/2015/07/nova-sport-hd_logo.jpg",
                "http://gledai-online.tv/images/diemasport.jpg",
                "http://static.nova.bg/public/pics/novatv/accents/img16x9_1437645182.jpg",
                "http://www.brandsoftheworld.com/sites/default/files/styles/logo-thumbnail/public/052012/hbo.png",
                "http://www.blizoo.bg/images/Root/content/services/packs/addons/hbo/images/hbo_plus_oneff.jpg",
                "http://1.bp.blogspot.com/-ty9NJBAITnY/TjjMmtkUDHI/AAAAAAAAB28/k6QxiNVeOVs/s500/Cinemax+logo+2011b.png",
                "http://www.tv-logo.com/pt-data/uploads/images/logo/cinemax2_ce.jpg"
        };
        categories = new HashMap<String, ArrayList<Movie>>();

        JSONArray epg = EPGService.getEPGData();
        try {
            for (int  i=0; i<epg.length();i++) {
                JSONObject channel = epg.getJSONObject(i);
                String cat = channel.getString("genre");
                ArrayList<Movie> mList = null;

                if (!categories.containsKey(cat)) {
                    mList = new ArrayList<Movie>();
                } else {
                    mList = categories.get(cat);
                }
                String pgRating = channel.getString("pg");
                String title = channel.getString("title");
                String videoUrl = channel.getString("sources");

                mList.add(buildMovieInfo(cat, title,
                        "lul", "", videoUrl, cardImageUrl[1], color[i % (color.length - 1)], pgRating));
                categories.put(cat, mList);
            }
        } catch (JSONException e) {
            Log.e(TAG, "setupMovies: ", e);
        }

        Log.d(TAG, Integer.toString(categories.size()));

        return categories;
    }

    private static Movie buildMovieInfo(String category, String title,
            String description, String studio, String videoUrl, String cardImageUrl, int color, String pgRating) {
        Movie movie = new Movie();
        movie.setId(Movie.getCount());
        Movie.incCount();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCategory(category);
        movie.setCardImageUrl(cardImageUrl);
        movie.setVideoUrl(videoUrl);
        movie.setColor(color);
        movie.setPgRating(pgRating);
        return movie;
    }
}
