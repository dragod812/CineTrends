package padhee.apps.cinetrends.pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MoviePage {
    private int pageNumber;
    private JSONArray jsonMovieArray;
    private int totalPageCount;
    public MoviePage( JSONArray jsonMovieArray, int pageNumber, int totalPageCount){
        this.pageNumber = pageNumber;
        this.jsonMovieArray = jsonMovieArray;
        this.totalPageCount = totalPageCount;
    }
    public int getPageNumber(){
        return pageNumber;
    }
    public int getMovieCount(){
        return jsonMovieArray.length();
    }
    public int getTotalPageCount(){
        return totalPageCount;
    }
    public JSONObject getMovie(int idx) throws JSONException {
        return jsonMovieArray.getJSONObject(idx);
    }
}
