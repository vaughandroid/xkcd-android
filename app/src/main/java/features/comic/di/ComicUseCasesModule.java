package features.comic.di;

import dagger.Binds;
import dagger.Module;
import features.comic.domain.usecases.ComicUseCases;
import features.comic.domain.usecases.GetComicImpl;
import features.comic.domain.usecases.GetLatestComicImpl;
import features.comic.domain.usecases.GetLatestComicNumberImpl;
import features.comic.domain.usecases.GetNextPageOfComicsImpl;

@Module
public abstract class ComicUseCasesModule {

    @Binds public abstract ComicUseCases.GetComic getComic(GetComicImpl impl);
    @Binds public abstract ComicUseCases.GetLatestComic getLatestComic(GetLatestComicImpl impl);
    @Binds public abstract ComicUseCases.GetLatestComicNumber getMaximumComicNumber(GetLatestComicNumberImpl impl);
    @Binds public abstract ComicUseCases.GetNextPageOfComics getNextPageOfComics(GetNextPageOfComicsImpl impl);
}
