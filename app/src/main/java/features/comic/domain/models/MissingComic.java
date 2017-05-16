package features.comic.domain.models;

import android.net.Uri;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MissingComic {

    public static MissingComic of(ComicNumber comicNumber) {
        return new AutoValue_MissingComic(comicNumber);
    }

    public abstract ComicNumber number();

    public Uri uri() {
        return Uri.parse("https://xkcd.com/" + number().intVal() + "/");
    }
}
