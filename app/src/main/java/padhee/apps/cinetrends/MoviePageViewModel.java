package padhee.apps.cinetrends;

import android.arch.lifecycle.ViewModel;
import padhee.apps.cinetrends.pojo.MoviePage;

public class MoviePageViewModel extends ViewModel {
    private int currentPageNumber;

    private int totalPageCount;

    private boolean isLoading;

    private MoviePage currentPage;

    public void loadNextPage(OnQueryReady<MoviePage> queryMaker){

    }

}
