package padhee.apps.cinetrends.Utilities.Parsers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class BitmapAsyncParser implements AsyncParser<Bitmap> {
    public Bitmap parse(InputStream in){
        return BitmapFactory.decodeStream(in);
    }
}
