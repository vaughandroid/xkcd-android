package testutil;

import android.net.Uri;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.PagedComics;

public class TestModelFactory {

    public static Comic comic(int number) {
        return comic(number, "2017-03-10");
    }

    public static Comic comic(int number, String dateString) {
        return Comic.builder()
                .number(number)
                .date(LocalDate.parse(dateString))
                .title("title " + number)
                .altText("alt text " + number)
                .imageUri(null)
                .build();
    }

    public static PagedComics comicsPage(int firstComicNumber, String dateString, int count, boolean hasAnotherPage) {
        LocalDate localDate = LocalDate.parse(dateString);
        ComicNumber comicNumber = ComicNumber.of(firstComicNumber);
        List<Comic> comics = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            comics.add(comic(comicNumber.intVal(), localDate.toString()));
            comicNumber = comicNumber.next();
            localDate = localDate.plusDays(1);
        }
        return PagedComics.of(comics, hasAnotherPage ? comicNumber.next() : null);
    }
}
