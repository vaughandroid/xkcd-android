package features.comic.data;

import android.support.annotation.NonNull;

import app.XKCDroidApp;
import features.comic.domain.Comic;
import features.comic.domain.ComicNumber;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import rx.SchedulerProvider;

public class ComicRepositoryImpl implements ComicRepository {

    private final ComicService service;
    @NonNull
    private final SchedulerProvider schedulerProvider;

    private final Function<ComicDto, Comic> dtoToComic = dto -> Comic.builder()
            .number(dto.num)
            .title(dto.title)
            .altText(dto.alt)
            .imageUri(dto.imgUri())
            .date(dto.date())
            .build();

    public ComicRepositoryImpl() {
        this(XKCDroidApp.retrofit().create(ComicService.class), XKCDroidApp.schedulerProvider());
    }

    public ComicRepositoryImpl(@NonNull ComicService service, @NonNull SchedulerProvider schedulerProvider) {
        this.service = service;
        this.schedulerProvider = schedulerProvider;
    }

    @Override
    public Single<Comic> getLatestComic() {
        return service.getLatestComic()
                .subscribeOn(schedulerProvider.io())
                .map(dtoToComic);
    }

    @Override
    public Single<Comic> getComic(@NonNull ComicNumber id) {
        return service.getComic(id.intVal())
                .subscribeOn(schedulerProvider.io())
                .map(dtoToComic);
    }
}
