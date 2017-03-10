package features.comic.data;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ComicService {

    @GET("/{num}/info.0.json")
    Single<ComicDto> getComic(@Path(("num")) int id);

    @GET("/info.0.json")
    Single<ComicDto> getLatestComic();
}
