package testutil;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.ComicResult;
import features.comic.domain.models.MissingComic;
import features.comic.domain.models.PagedComics;

public class TestModelFactory {

    @NonNull
    public static Comic comic(int number) {
        return comic(number, "2017-03-10");
    }

    @NonNull
    public static Comic comic(int number, String dateString) {
        return Comic.builder()
                .number(number)
                .date(LocalDate.parse(dateString))
                .title("title " + number)
                .altText("alt text " + number)
                .imageUri(null)
                .build();
    }

    @NonNull
    public static MissingComic missingComic(int comic) {
        return MissingComic.of(ComicNumber.of(comic));
    }

    @NonNull
    public static ComicResult comicResult(int number) {
        return ComicResult.of(comic(number));
    }


    @NonNull
    public static ComicResult comicResult(int number, String dateString) {
        return ComicResult.of(comic(number, dateString));
    }

    @NonNull
    public static ComicResult missingComicResult(int number) {
        return ComicResult.of(missingComic(number));
    }

    @NonNull
    public static PagedComics comicsPage(int firstComicNumber, String dateString, int count, boolean hasAnotherPage) {
        LocalDate localDate = LocalDate.parse(dateString);
        ComicNumber comicNumber = ComicNumber.of(firstComicNumber);
        List<ComicResult> comics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comics.add(comicResult(comicNumber.intVal(), localDate.toString()));
            comicNumber = comicNumber.next();
            localDate = localDate.plusDays(1);
        }
        return PagedComics.of(comics, hasAnotherPage ? comicNumber : null);
    }
}
