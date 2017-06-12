package features.comic.ui;

import com.google.auto.value.AutoValue;

import features.comic.domain.models.ComicNumber;

abstract class ViewComicUiEvent {

    static class FabClicked extends ViewComicUiEvent {

    }

    static class AltTextClicked extends ViewComicUiEvent {

    }

    @AutoValue
    abstract static class Init {

        public static Init create(ComicNumber comicNumber) {
            return new AutoValue_ViewComicUiEvent_Init(comicNumber);
        }

        abstract ComicNumber comicNumber();
    }
}
