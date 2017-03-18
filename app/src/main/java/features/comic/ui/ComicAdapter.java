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
import features.comic.domain.Comic;


public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    public interface OnComicClickedListener {
        void onComicClicked(Comic comic);
    }

    private final LayoutInflater layoutInflater;

    private List<Comic> comics = new ArrayList<>();

    @Nullable private OnComicClickedListener onComicClickedListener;

    public ComicAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public void setComics(List<Comic> comics) {
        this.comics = comics;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int idx) {
        viewHolder.setComic(comics.get(idx));
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    public void setOnComicClickedListener(@Nullable OnComicClickedListener onComicClickedListener) {
        this.onComicClickedListener = onComicClickedListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(android.R.id.text1) TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setComic(Comic comic) {
            textView.setText(comic.title());
            textView.setOnClickListener(v -> {
                if (onComicClickedListener != null) {
                    onComicClickedListener.onComicClicked(comic);
                }
            });
        }
    }
}
