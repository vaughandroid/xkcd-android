package features.comic.ui;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class ViewComicVM {

    static Builder builder() {
        return new AutoValue_ViewComicVM.Builder()
                .showAltText(false)
                .showError(false);
    }

    abstract boolean showAltText();
    abstract boolean showError();

    abstract Builder toBuilder();

    @AutoValue.Builder
    static abstract class Builder {

        abstract Builder showAltText(boolean show);
        abstract Builder showError(boolean show);

        abstract ViewComicVM build();
    }
}
