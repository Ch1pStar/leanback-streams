package home.ned.lul;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieList {
    private static final String TAG = "MovieList";
    public static final String MOVIE_CATEGORY[] = {
            "TV",
            "Network Stream"
    };

    public static List<Movie> list;

    public static List<Movie> setupMovies(Context context) {
        list = new ArrayList<Movie>();
        String title[] = {
                "Nova Sport",
                "Diema Sport",
                "Diema Sport 2 HD",
                "HBO",
                "HBO +1",
                "Cinemax",
                "Cinemax 2"
        };

        int color[] = {
                ContextCompat.getColor(context, R.color.gold),
                ContextCompat.getColor(context, R.color.red),
                ContextCompat.getColor(context, R.color.blue),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black)
        };
        String description = "Fusce id nisi turpis. Praesent viverra bibendum semper. "
                + "Donec tristique, orci sed semper lacinia, quam erat rhoncus massa, non congue tellus est "
                + "quis tellus. Sed mollis orci venenatis quam scelerisque accumsan. Curabitur a massa sit "
                + "amet mi accumsan mollis sed et magna. Vivamus sed aliquam risus. Nulla eget dolor in elit "
                + "facilisis mattis. Ut aliquet luctus lacus. Phasellus nec commodo erat. Praesent tempus id "
                + "lectus ac scelerisque. Maecenas pretium cursus lectus id volutpat.";

        String videoUrl[] = {
                "http://87.246.0.136:59349/tv_nova_Sport",
                "http://87.246.0.136:59349/tv_Diema_Sport",
                "http://87.246.0.136:59349/tv_Diema_Sport_2_HD",
                "http://87.246.0.136:59349/tv_HBO",
                "http://87.246.0.136:59349/tv_HBO_+1",
                "http://87.246.0.136:59349/tv_Cinemax",
                "http://87.246.0.136:59349/tv_Cinemax_2"
        };
        String bgImageUrl[] = {
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg",
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


        JSONArray epg = EPGService.getEPGData();

        try {
            for (int  i=0; i<epg.length();i++){
                JSONObject channel = epg.getJSONObject(i);
                list.add(buildMovieInfo(channel.getString("genre"), channel.getString("title"),
                    "lul", "", channel.getString("sources"), cardImageUrl[1], bgImageUrl[0],color[0]));
            }
        } catch (JSONException e) {
            Log.e(TAG, "setupMovies: ", e);
//            e.printStackTrace();
        }

//        list.add(buildMovieInfo("category", title[0],
//                description, "", videoUrl[0], cardImageUrl[0], bgImageUrl[0],color[0]));
//        list.add(buildMovieInfo("category", title[1],
//                description, "", videoUrl[1], cardImageUrl[1], bgImageUrl[1],color[1]));
//        list.add(buildMovieInfo("category", title[2],
//                description, "", videoUrl[2], cardImageUrl[2], bgImageUrl[2],color[2]));
//        list.add(buildMovieInfo("category", title[3],
//                description, "", videoUrl[3], cardImageUrl[3], bgImageUrl[3],color[3]));
//        list.add(buildMovieInfo("category", title[4],
//                description, "", videoUrl[4], cardImageUrl[4], bgImageUrl[2],color[4]));
//        list.add(buildMovieInfo("category", title[5],
//                description, "", videoUrl[5], cardImageUrl[5], bgImageUrl[2],color[5]));
//        list.add(buildMovieInfo("category", title[6],
//                description, "", videoUrl[6], cardImageUrl[6], bgImageUrl[2],color[6]));

        return list;
    }

    private static Movie buildMovieInfo(String category, String title,
            String description, String studio, String videoUrl, String cardImageUrl,
            String bgImageUrl, int color) {
        Movie movie = new Movie();
        movie.setId(Movie.getCount());
        Movie.incCount();
        movie.setTitle(title);
        movie.setDescription(description);
        movie.setStudio(studio);
        movie.setCategory(category);
        movie.setCardImageUrl(cardImageUrl);
        movie.setBackgroundImageUrl(bgImageUrl);
        movie.setVideoUrl(videoUrl);
        movie.setColor(color);
        return movie;
    }
}
