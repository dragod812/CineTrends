package padhee.apps.cinetrends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import padhee.apps.cinetrends.Utilities.Parsers.AsyncParser;
import padhee.apps.cinetrends.Utilities.AsyncQuery;
import padhee.apps.cinetrends.Utilities.BitmapCache;
import padhee.apps.cinetrends.Utilities.NetworkUtils;
import padhee.apps.cinetrends.Utilities.PaginationScrollListener;
import padhee.apps.cinetrends.Utilities.Parsers.MoviePageAsyncParser;
import padhee.apps.cinetrends.pojo.MoviePage;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnQueryReady<MoviePage> {

    private static  final String TAG = MainActivity.class.getSimpleName();

    private TextView mErrorMessageDisplay;

    private MovieAdapter mAdapter;

    private RecyclerView mMovieList;

    private int currentPage;

    private int totalPageCount;

    boolean isLoading;

    MainActivity(){
    }

    private void showErrorMessage(){
        mMovieList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public void onQueryReady(MoviePage page) {
        if(page == null){
            mAdapter.removeLoadingFooter();
            showErrorMessage();
            return;
        }
        //if its the first page of the result get the totalPageCount.
        if(page.getPageNumber() == 1)
            totalPageCount = page.getTotalPageCount();
        Log.d(TAG, "Total Number of Pages: " + page.getTotalPageCount() + ", Current Page: " + page.getPageNumber());
        isLoading = false;
        mAdapter.addMoviePage(page);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewModelProvider provider = ViewModelProviders.of(this);
        final MoviePageViewModel currentPageViewModel = provider.get(MoviePageViewModel.class);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        currentPage = 0;
        totalPageCount = -1;
        isLoading = false;


        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this) ;
        mMovieList.setLayoutManager(layoutManager);
        BitmapCache bitmapCache = new BitmapCache(BitmapCache.getCacheSize());
        mAdapter = new MovieAdapter(bitmapCache);
        mMovieList.setAdapter(mAdapter);

        mMovieList.addOnScrollListener(new PaginationScrollListener(layoutManager) {
            @Override
            protected void loadMoreItems() {
                if(currentPage < totalPageCount){
//                    loadPage();
                    currentPageViewModel.loadNextPage(page -> {
                        mAdapter.addMoviePage(page);
                    });
                }
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
                return isLoading;
            }
        });

        loadPage();

    }

    private void loadPage(){
        currentPage += 1;
        URL trendingURL = NetworkUtils.buildTrendingMoviesURL(currentPage);
        AsyncParser<MoviePage> parser = new MoviePageAsyncParser();
        isLoading = true;
        new AsyncQuery<MoviePage>(this, parser).execute(trendingURL);
    }
}
