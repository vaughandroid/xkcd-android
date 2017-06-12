package features.comic.ui;

import common.domain.Action;
import common.domain.Result;
import features.comic.domain.actions.FetchComic;
import features.comic.domain.results.HideAltText;
import features.comic.domain.results.ShowAltText;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class ViewComicDispatcher {

    public ObservableTransformer<ViewComicUiEvent, Action> uiEventToAction() {
        return events -> events.publish(shared -> Observable.merge(
                shared.ofType(ViewComicUiEvent.Init.class).map(init -> (Action) FetchComic.create(init.comicNumber())),
                // XXX temp until there are 2 or more Actions
                shared.ofType(ViewComicUiEvent.Init.class).map(init -> (Action) FetchComic.create(init.comicNumber()))
        ));
    }

    public ObservableTransformer<ViewComicUiEvent, Result> uiEventToResult() {
        return events -> events.publish(shared -> Observable.merge(
                shared.ofType(ViewComicUiEvent.FabClicked.class).map(init -> new ShowAltText()),
                shared.ofType(ViewComicUiEvent.AltTextClicked.class).map(init -> new HideAltText())
        ));
    }
}
