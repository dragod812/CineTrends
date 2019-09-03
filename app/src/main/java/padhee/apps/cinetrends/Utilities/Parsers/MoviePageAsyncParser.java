package padhee.apps.cinetrends.Utilities.Parsers;

import org.json.JSONArray;
import org.json.JSONObject;
import padhee.apps.cinetrends.pojo.MoviePage;

import java.io.InputStream;
import java.util.Scanner;

public class MoviePageAsyncParser implements AsyncParser<MoviePage> {
   public MoviePage parse(InputStream in){
      JSONArray jsonMoviesArray = null;
      int currentPage = -1;
      int totalPageCount = -1;
      try{
         Scanner scanner = new Scanner(in);
         scanner.useDelimiter("\\A");

         boolean hasInput = scanner.hasNext();
         String result;
         if (hasInput) {
            result = scanner.next();
         } else {
            result = "";
         }

         JSONObject jsonResult = new JSONObject(result);
         currentPage = Integer.valueOf(jsonResult.getString("page"));
         totalPageCount = Integer.valueOf(jsonResult.getString("total_pages"));
         jsonMoviesArray =  jsonResult.getJSONArray("results");
      } catch (Exception e){
         e.printStackTrace();
      }
      return new MoviePage(jsonMoviesArray, currentPage, totalPageCount);
   }
}
