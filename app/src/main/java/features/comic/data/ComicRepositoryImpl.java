package features.comic.data;

import android.support.annotation.NonNull;

import org.threeten.bp.LocalDate;

import app.XKCDroidApp;
import features.comic.domain.Comic;
import features.comic.domain.ComicId;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class ComicRepositoryImpl implements ComicRepository {

    @NonNull private final ComicService service;

    public ComicRepositoryImpl() {
        this(XKCDroidApp.getRetrofit().create(ComicService.class));
    }

    public ComicRepositoryImpl(@NonNull ComicService service) {
        this.service = service;
    }

    @Override
    public Single<Comic> getComic(@NonNull ComicId id) {
        return service.getComic(id.intVal())
                .subscribeOn(Schedulers.io())
                .map(dto -> Comic.builder()
                        .id(dto.num)
                        .title(dto.title)
                        .altText(dto.alt)
                        .imageUri(dto.imgUri())
                        .date(dto.date())
                        .build());
    }
}
