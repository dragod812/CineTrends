/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package padhee.apps.cinetrends.Utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    final static String MOVIEDB_API_BASEURL = "https://api.themoviedb.org/3/discover/movie";


    final static String PARAM_SORT = "sort_by";
    final static String sortBy = "popularity.desc";

    final static String PARAM_APIKEY = "api_key";
    final static String apiKey = "812403f79cc676b7bb32f4ed688f4f91";

    final static String PARAM_PAGE = "page";

    public static URL buildMovieAPIUrl(int page) {
        Uri builtUri = Uri.parse(MOVIEDB_API_BASEURL).buildUpon().appendQueryParameter(PARAM_SORT, sortBy).appendQueryParameter(PARAM_APIKEY, apiKey).appendQueryParameter(PARAM_PAGE, Integer.toString(page)).build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }



    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static InputStream getStreamFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        return urlConnection.getInputStream();
    }
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static Bitmap getImage(String imageUrl){
        try {
            InputStream in = new java.net.URL(imageUrl).openStream();
            return BitmapFactory.decodeStream(in);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}