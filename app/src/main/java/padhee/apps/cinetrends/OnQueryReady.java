package padhee.apps.cinetrends;

import org.json.JSONArray;

public interface OnQueryReady<T> {
    void onQueryReady(T queryResult);
}
