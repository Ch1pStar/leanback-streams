package home.ned.lul;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v17.leanback.app.SearchFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LULSearchFragment extends SearchFragment
        implements SearchFragment.SearchResultProvider {

    private static final String TAG = "SearchFragment";

    public LULSearchFragment() {
        // Required empty public constructor
    }

    private static final int SEARCH_DELAY_MS = 300;
    private ArrayObjectAdapter mRowsAdapter;
    private Handler mHandler = new Handler();
    private static SearchTask mDelayedLoad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        setSearchResultProvider(this);

        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }

    @Override
    public ObjectAdapter getResultsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public boolean onQueryTextChange(String newQuery) {
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(newQuery)) {
            mDelayedLoad = new SearchTask();
            mDelayedLoad.execute(newQuery);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mRowsAdapter.clear();
        if (!TextUtils.isEmpty(query)) {
            mDelayedLoad = new SearchTask();
            mDelayedLoad.execute(query);
        }
        return true;
    }


    class SearchTask extends AsyncTask<String, Integer, Stack<ListRow>> {

        @Override
        protected Stack<ListRow> doInBackground(String... params) {
            String query = params[0];

            Pattern p = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            Map<String,  ArrayList<Movie>> categories = MovieList.setupMovies(getActivity());
            CardPresenter cardPresenter = new CardPresenter();
            Iterator it = categories.entrySet().iterator();

            Stack<ListRow> results = new Stack<ListRow>();
            while(it.hasNext()){
                Map.Entry pair = (Map.Entry)it.next();
                String cat = (String)pair.getKey();
                ArrayList<Movie> channels = (ArrayList<Movie>)pair.getValue();
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
                Iterator channelIt = channels.iterator();
                String pgRating = null;
                while(channelIt.hasNext()){
                    Movie channel = (Movie)channelIt.next();
                    Matcher m = p.matcher(channel.getTitle());
                    StringBuffer sb = new StringBuffer();
                    while (m.find()) {
                        listRowAdapter.add(channel);
                        pgRating = channel.getPgRating();
                        break;
                    }
                }

                if(listRowAdapter.size()>0){
                    HeaderItem header = new HeaderItem(0,cat);
                    results.push(new ListRow(header, listRowAdapter));
                }

            }

            return results;
        }

        @Override
        protected void onPostExecute(Stack<ListRow> list) {
            Iterator it = list.iterator();
            while(it.hasNext()){
                mRowsAdapter.add(it.next());
            }
        }
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            if (item instanceof Movie) {
                Movie movie = (Movie) item;
//                Log.i(TAG, "onItemClicked: "+((Movie) item).getTitle());
//                Intent intent = new Intent(getActivity(), DetailsActivity.class);
//                intent.putExtra(DetailsActivity.MOVIE, movie);
                Intent intent = new Intent(getActivity(), NativePlaybackActivity.class);
                intent.putExtra(DetailsActivity.MOVIE, movie);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(),
                        ((ImageCardView) itemViewHolder.view).getMainImageView(),
                        DetailsActivity.SHARED_ELEMENT_NAME).toBundle();

                getActivity().startActivity(intent, bundle);
            }
        }
    }

    private final class ItemViewSelectedListener implements OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
//            if (item instanceof Movie) {
//                Log.i(TAG, "onItemSelected: "+((Movie) item).getInfo());
//            }

        }
    }

}
