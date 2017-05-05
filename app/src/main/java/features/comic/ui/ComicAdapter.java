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
import features.comic.domain.models.PagedComics;
import me.vaughandroid.xkcdreader.R;


class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    private static final int ITEM_COMIC = 0;
    private static final int ITEM_LOADING = 1;

    public interface OnComicClickedListener {
        void onComicClicked(Comic comic);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(ComicNumber nextComicNumber);
    }

    private final List<Comic> comics = new ArrayList<>();

    private final Context context;
    private final LayoutInflater layoutInflater;

    private final OnComicClickedListener onComicClickedListener;
    private final OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    @Nullable private ComicNumber nextComicNumber;

    public ComicAdapter(Context context,
                        OnComicClickedListener onComicClickedListener,
                        OnLoadMoreListener onLoadMoreListener) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.onComicClickedListener = onComicClickedListener;
        this.onLoadMoreListener = onLoadMoreListener;
        setHasStableIds(true);
    }

    public void addPage(PagedComics page) {
        comics.addAll(page.comics());
        nextComicNumber = page.nextComicNumber();
        isLoading = false;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_COMIC:
                return new ComicViewHolder(layoutInflater.inflate(R.layout.item_comic, parent, false));
            default:
                return new LoadingViewHolder(layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int idx) {
        if (viewHolder instanceof ComicViewHolder) {
            ((ComicViewHolder) viewHolder).setComic(comics.get(idx));
        } else if (viewHolder instanceof LoadingViewHolder) {
            if (!isLoading) {
                onLoadMoreListener.onLoadMore(nextComicNumber);
                isLoading = true;
            }
        }
    }

    @Override
    public int getItemCount() {
        return comics.size() + (canLoadMoreComics() ? 1 : 0);
    }

    private boolean canLoadMoreComics() {
        return nextComicNumber != null;
    }

    @Override
    public long getItemId(int position) {
        if (getItemViewType(position) == ITEM_COMIC) {
            return comics.get(position).number().intVal();
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        return position < comics.size() || !canLoadMoreComics() ? ITEM_COMIC : ITEM_LOADING;
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

    class LoadingViewHolder extends ViewHolder {

        @BindView(android.R.id.text1) TextView textView;

        LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textView.setText(R.string.loading);
        }
    }
}
