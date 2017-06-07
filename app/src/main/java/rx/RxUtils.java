package rx;

import io.reactivex.android.BuildConfig;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public final class RxUtils {

    public static final String LOG_TAG = "Rx";

    public static Consumer<Throwable> errorHandler() {
        return error -> {
            if (error instanceof UndeliverableException) {
                Timber.tag(LOG_TAG).e(error.getCause(), "Undeliverable error");
            } else {
                Timber.tag(LOG_TAG).e(error);
                if (BuildConfig.DEBUG) {
                    Thread currentThread = Thread.currentThread();
                    currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, error);
                }
            }
        };
    }
}
