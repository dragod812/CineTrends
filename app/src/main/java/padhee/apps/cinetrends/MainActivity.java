package padhee.apps.cinetrends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import padhee.apps.cinetrends.Utilities.Parsers.AsyncParser;
import padhee.apps.cinetrends.Utilities.Parsers.JSONArrayAsyncParser;
import padhee.apps.cinetrends.Utilities.AsyncQuery;
import padhee.apps.cinetrends.Utilities.BitmapCache;
import padhee.apps.cinetrends.Utilities.NetworkUtils;
import padhee.apps.cinetrends.Utilities.PaginationScrollListener;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnQueryReady<JSONArray> {
    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageDisplay;

    private MovieAdapter mAdapter;

    private RecyclerView mMovieList;

    private int currentPage;

    private int totalPageCount = -1;

    MainActivity(){
    }

    private void showErrorMessage(){
        mMovieList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showLoading(){
        mMovieList.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void doneLoading(){
        mMovieList.setVisibility(View.VISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    public void onQueryReady(JSONArray jsonMoviesArray) {
        if(jsonMoviesArray == null){
            showErrorMessage();
            return;
        }
        mAdapter.addMovieArray(jsonMoviesArray);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display) ;
        currentPage = 1;


        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this) ;
        mMovieList.setLayoutManager(layoutManager);
        BitmapCache bitmapCache = new BitmapCache(BitmapCache.getCacheSize());
        mAdapter = new MovieAdapter(bitmapCache);
        mMovieList.setAdapter(mAdapter);

        mMovieList.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                currentPage += 1;
                loadPage();
            }

            @Override
            public int getTotalPageCount() {
                return totalPageCount;
            }

            @Override
            public boolean isLastPage() {
                return currentPage == totalPageCount;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });

        loadPage();

    }

    private void loadPage(){
        URL trendingURL = NetworkUtils.buildMovieAPIUrl(currentPage);
        AsyncParser<JSONArray> parser = new JSONArrayAsyncParser();
        new AsyncQuery<JSONArray>(this, parser).execute(trendingURL);
    }
}
