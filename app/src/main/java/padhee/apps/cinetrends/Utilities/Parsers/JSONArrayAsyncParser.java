package padhee.apps.cinetrends.Utilities.Parsers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

public class JSONArrayAsyncParser implements AsyncParser<JSONArray> {
   public JSONArray parse(InputStream in){
      JSONArray jsonMoviesArray = null;
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
         jsonMoviesArray =  jsonResult.getJSONArray("results");
      } catch (Exception e){
         e.printStackTrace();
      }
      return jsonMoviesArray;
   }
}
