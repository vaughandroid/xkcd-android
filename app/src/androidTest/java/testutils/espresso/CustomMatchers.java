package testutils.espresso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.test.espresso.core.deps.guava.io.Closeables;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import testutils.Drawables;

import static org.hamcrest.CoreMatchers.not;

public class CustomMatchers {

    private CustomMatchers() {
        throw new AssertionError("No instances.");
    }

    @NonNull
    public static Matcher<View> imageViewWithBitmap(@NonNull final String targetAssetPath) {
        return new TypeSafeMatcher<View>(ImageView.class) {
            private String failureReason;

            @Override
            protected boolean matchesSafely(View view) {
                Uri uri = Uri.parse(targetAssetPath);
                InputStream inputStream = null;
                try {
                    // Despite efforts, it looks like this is still loading resources from the test context. :/
                    inputStream = view.getContext().getContentResolver().openInputStream(uri);
                    Bitmap expected = BitmapFactory.decodeStream(inputStream);

                    Bitmap bitmap = Drawables.bitmapFromDrawable(((ImageView) view).getDrawable());
                    return bitmap.sameAs(expected);
                } catch (Exception e) {
                    failureReason = String.format(
                            "%s: %s",
                            e.getClass(),
                            e.getMessage()
                    );
                    return false;
                } finally {
                    Closeables.closeQuietly(inputStream);
                }
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with the bitmap at " + targetAssetPath);
                if (failureReason != null) {
                    description.appendText(String.format(" (%s)", failureReason));
                }
            }
        };
    }

}
