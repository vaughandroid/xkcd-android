package app;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.BindView;
import me.vaughandroid.xkcdreader.R;

public class CLEActivity extends BaseActivity {

    @BindView(R.id.content) protected View contentView;
    @BindView(R.id.loading) protected View loadingView;
    @BindView(R.id.error) protected View errorView;

    protected void showContent() {
        contentView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
    }

    protected void showLoading() {
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    protected void showError() {
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }
}
