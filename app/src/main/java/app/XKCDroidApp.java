package app;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.AndroidSchedulerProvider;
import rx.SchedulerProvider;

public class XKCDroidApp extends Application {

    // TODO: Inject this
    private static Retrofit retrofit;
    public static Retrofit retrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://xkcd.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // TODO: Inject this
    private static SchedulerProvider schedulerProvider;
    public static SchedulerProvider schedulerProvider() {
        if (schedulerProvider == null) {
            schedulerProvider = new AndroidSchedulerProvider();
        }
        return schedulerProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
    }
}
