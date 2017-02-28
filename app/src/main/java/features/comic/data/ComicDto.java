package features.comic.data;

import android.net.Uri;
import android.support.annotation.Nullable;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

class ComicDto {

    String month;
    int num;
    String link;
    String year;
    String news;
    String safe_title;
    String transcript;
    String alt;
    String img;
    String title;
    String day;

    @Nullable public Uri imgUri() {
        return Uri.parse(img);
    }

    public LocalDate date() {
        return LocalDate.parse(String.format("%s-%s-%s", year, month, day),
                DateTimeFormatter.ofPattern("y-M-d"));
    }
}
