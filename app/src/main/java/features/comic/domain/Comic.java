package features.comic.domain;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

import org.threeten.bp.LocalDate;

import java.net.URL;

@AutoValue
public abstract class Comic {

    public static Builder builder() {
        return new AutoValue_Comic.Builder();
    }

    public abstract ComicId id();

    public abstract String title();

    public abstract LocalDate date();

    @Nullable public abstract Uri imageUri();

    public abstract String altText();

    @AutoValue.Builder
    public static abstract class Builder {

        public abstract Builder id(ComicId id);
        public Builder id(int id) {
            return id(ComicId.create(id));
        }

        public abstract Builder title(String title);

        public abstract Builder date(LocalDate date);

        public abstract Builder imageUri(Uri imageUri);

        public abstract Builder altText(String altText);

        public abstract Comic build();
    }
}
