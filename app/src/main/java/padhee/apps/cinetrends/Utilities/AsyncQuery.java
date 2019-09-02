package padhee.apps.cinetrends.Utilities;

import android.os.AsyncTask;
import padhee.apps.cinetrends.OnQueryReady;
import padhee.apps.cinetrends.Utilities.Parsers.AsyncParser;

import java.io.InputStream;
import java.net.URL;

public class AsyncQuery<T> extends AsyncTask<URL, Void, T> {
    private OnQueryReady caller;
    private AsyncParser<T> parser;

    public AsyncQuery(OnQueryReady caller, AsyncParser<T> parser){
        this.caller = caller;
        this.parser = parser;
    }

    @Override
    protected T doInBackground(URL... urls) {
        T queryResult = null;
        try{
            InputStream in = NetworkUtils.getStreamFromHttpUrl(urls[0]);
            queryResult = parser.parse(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return queryResult;
    }

    @Override
    protected void onPostExecute(T jsonMoviesArray) {
        caller.onQueryReady(jsonMoviesArray);
    }
}

