package padhee.apps.cinetrends.Utilities.Parsers;

import java.io.InputStream;

public interface AsyncParser<T> {
    public T parse(InputStream in);
}
