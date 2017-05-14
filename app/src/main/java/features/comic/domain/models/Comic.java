package features.comic.domain.models;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import org.threeten.bp.LocalDate;

import java.util.Comparator;

@AutoValue
public abstract class Comic {

    public static Builder builder() {
        return new AutoValue_Comic.Builder();
    }

    public abstract ComicNumber number();

    public abstract String title();

    public abstract LocalDate date();

    @Nullable public abstract Uri imageUri();

    public abstract String altText();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder number(ComicNumber number);
        public Builder number(int number) {
            return number(ComicNumber.of(number));
        }

        public abstract Builder title(String title);

        public abstract Builder date(LocalDate date);

        public abstract Builder imageUri(Uri imageUri);

        public abstract Builder altText(String altText);

        public abstract Comic build();
    }
}
