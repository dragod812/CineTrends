package padhee.apps.cinetrends;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import padhee.apps.cinetrends.Utilities.Parsers.AsyncParser;
import padhee.apps.cinetrends.Utilities.Parsers.BitmapAsyncParser;
import padhee.apps.cinetrends.Utilities.AsyncQuery;
import padhee.apps.cinetrends.Utilities.BitmapCache;

import java.net.URL;

public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();

    private JSONArray movieArray;
    private BitmapCache bitmapCache;
    public static final int MOVIE = 1, LOADING = 0;
    private boolean isLoadingAdded = false;


    public MovieAdapter(BitmapCache bitmapCache) {
        movieArray = new JSONArray();
        this.bitmapCache = bitmapCache;
        addLoadingFooter();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        boolean shouldAttachToParentImmediately = false;
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType){
            case MOVIE:
                view = inflater.inflate(R.layout.movie_list_item, viewGroup, shouldAttachToParentImmediately);
                viewHolder = new MovieViewHolder(view, bitmapCache);
                break;
            case LOADING:
                view = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == MOVIE){
            MovieViewHolder mvh = (MovieViewHolder) holder;
            try{
                Log.d(TAG, "#" + movieArray.getJSONObject(position).toString());
                JSONObject details = movieArray.getJSONObject(position);
                String title = details.getString("title");
                String posterPath = details.getString("poster_path");
                mvh.bind(title, posterPath);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        return movieArray.length();
    }

    public int getItemViewType(int position) {
        return (position == movieArray.length() - 1 && isLoadingAdded) ? LOADING : MOVIE;
    }


    //helper methods
    public void addMovie(JSONObject movie){
        movieArray.put(movie);
        notifyItemInserted(movieArray.length()-1);
    }

    public void addMovieArray(JSONArray jsonMoviesArray){
        removeLoadingFooter();
        for(int i = 0;i<jsonMoviesArray.length();i++){
            try{
                addMovie(jsonMoviesArray.getJSONObject(i));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
        addLoadingFooter();
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        addMovie(new JSONObject());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieArray.length() - 1;
        try{
            JSONObject item = movieArray.getJSONObject(position);
            if (item != null) {
                movieArray.remove(position);
                notifyItemRemoved(position);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    /**
     * Cache of the children views for a list item.
     */
    class MovieViewHolder extends RecyclerView.ViewHolder implements OnQueryReady<Bitmap> {

        TextView listMovieItemView;
        ImageView ivPoster;
        String baseImageUrl;
        BitmapCache bitmapCache;
        String posterPath;

        public void onQueryReady(Bitmap queryResult ){
            ivPoster.setImageBitmap(queryResult);
            bitmapCache.setBitmap(posterPath, queryResult);
        }

        public MovieViewHolder(View itemView, BitmapCache bitmapCache) {
            super(itemView);

            listMovieItemView = (TextView) itemView.findViewById(R.id.tv_movie_item);
            ivPoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            ivPoster.setImageResource(R.drawable.placeholder);
            baseImageUrl = "https://image.tmdb.org/t/p/w200";
            this.bitmapCache = bitmapCache;

        }


        void bind(String movieTitle, String posterPath) {
            listMovieItemView.setText(movieTitle);
            try{
                Bitmap img = bitmapCache.getBitmap(posterPath);
                if (img == null){
                    URL posterURL = new URL(baseImageUrl + posterPath);
                    this.posterPath = posterPath;
                    AsyncParser<Bitmap> parser = new BitmapAsyncParser();
                    new AsyncQuery<Bitmap>(this, parser).execute(posterURL);
                }
                else{
                    ivPoster.setImageBitmap(img);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}
