package testutil;

import android.net.Uri;

import org.threeten.bp.LocalDate;

import features.comic.domain.Comic;

public class StubFactory {

    public static Comic comic(int number) {
        return Comic.builder()
                .number(number)
                .date(LocalDate.parse("2017-03-10"))
                .title("title " + number)
                .altText("alt text " + number)
                .imageUri(Uri.EMPTY)
                .build();
    }
}
