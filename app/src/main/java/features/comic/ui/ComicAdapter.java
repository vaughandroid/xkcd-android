package features.comic.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import features.comic.domain.models.Comic;
import features.comic.domain.models.ComicNumber;
import features.comic.domain.models.ComicResult;
import features.comic.domain.models.MissingComic;
import features.comic.domain.models.PagedComics;
import me.vaughandroid.xkcdreader.R;


// TODO: Can use Robolectric to test this
class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    private static final int ITEM_COMIC = 0;
    private static final int ITEM_LOADING = 1;
    private static final int ITEM_MISSING_COMIC = 2;

    public interface OnComicClickedListener {
        void onComicClicked(Comic comic);
    }

    public interface OnMissingComicClickedListener {
        void onMissingComicClicked(MissingComic missingComic);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(ComicNumber nextComicNumber);
    }

    private final List<ComicResult> items = new ArrayList<>();

    private final Context context;
    private final LayoutInflater layoutInflater;

    private final OnComicClickedListener onComicClickedListener;
    private final OnMissingComicClickedListener onMissingComicClickedListener;
    private final OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    @Nullable private ComicNumber nextComicNumber;

    public ComicAdapter(Context context,
                        OnComicClickedListener onComicClickedListener,
                        OnMissingComicClickedListener onMissingComicClickedListener,
                        OnLoadMoreListener onLoadMoreListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.onComicClickedListener = onComicClickedListener;
        this.onMissingComicClickedListener = onMissingComicClickedListener;
        this.onLoadMoreListener = onLoadMoreListener;
        setHasStableIds(true);
    }

    public void addPage(PagedComics page) {
        items.addAll(page.items());
        nextComicNumber = page.nextComicNumber();
        isLoading = false;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_COMIC:
                return new ComicViewHolder(layoutInflater.inflate(R.layout.item_comic, parent, false));
            case ITEM_MISSING_COMIC:
                return new MissingComicViewHolder(layoutInflater.inflate(R.layout.item_missing_comic, parent, false));
            default:
                return new LoadingViewHolder(layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int idx) {
        if (viewHolder instanceof LoadingViewHolder) {
            if (!isLoading) {
                onLoadMoreListener.onLoadMore(nextComicNumber);
                isLoading = true;
            }
        } else {
            //noinspection ConstantConditions
            items.get(idx).continued(
                    ((ComicViewHolder) viewHolder)::setComic,
                    ((MissingComicViewHolder) viewHolder)::setMissingComic
            );
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + (canLoadMoreComics() ? 1 : 0);
    }

    private boolean canLoadMoreComics() {
        return nextComicNumber != null;
    }

    @Override
    public long getItemId(int position) {
        if (position == items.size()) {
            return -1;
        }
        return items.get(position).number().intVal();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == items.size()) {
            return ITEM_LOADING;
        }
        return items.get(position).join(comic -> ITEM_COMIC, missingComic -> ITEM_MISSING_COMIC);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class ComicViewHolder extends ViewHolder {

        @BindView(R.id.comic_number) TextView numberView;
        @BindView(R.id.comic_date) TextView dateView;
        @BindView(R.id.comic_title) TextView titleView;

        ComicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setComic(Comic comic) {
            numberView.setText(context.getString(R.string.comic_number, comic.number().intVal()));
            dateView.setText(comic.date().toString());
            titleView.setText(comic.title());
            itemView.setOnClickListener(v -> {
                if (onComicClickedListener != null) {
                    onComicClickedListener.onComicClicked(comic);
                }
            });
        }
    }

    class MissingComicViewHolder extends ViewHolder {

        @BindView(R.id.missing_comic_number) TextView numberView;

        MissingComicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setMissingComic(MissingComic missingComic) {
            numberView.setText(context.getString(R.string.comic_number, missingComic.number().intVal()));
            itemView.setOnClickListener(v -> {
                if (onMissingComicClickedListener != null) {
                    onMissingComicClickedListener.onMissingComicClicked(missingComic);
                }
            });
        }
    }

    class LoadingViewHolder extends ViewHolder {

        @BindView(android.R.id.text1) TextView textView;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView.setText(R.string.loading);
        }
    }
}
