package ru.radiationx.anilibria.ui.fragments.releases;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ru.radiationx.anilibria.R;
import ru.radiationx.anilibria.data.api.releases.ReleaseItem;
import ru.radiationx.anilibria.ui.adapters.BaseAdapter;
import ru.radiationx.anilibria.ui.adapters.BaseViewHolder;

/* Created by radiationx on 31.10.17. */

public class ReleasesAdapter extends BaseAdapter<ReleaseItem, BaseViewHolder> {
    private static final int RELEASE_LAYOUT = 1;
    private static final int LOAD_MORE_LAYOUT = 2;
    private ItemListener listener;

    public ReleasesAdapter(MvpDelegate<?> parentDelegate) {
        super(parentDelegate, String.valueOf(0));
    }

    public void setListener(ItemListener listener) {
        this.listener = listener;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case RELEASE_LAYOUT:
                return new ReleaseItemHolder(inflateLayout(parent, R.layout.item_release));
            case LOAD_MORE_LAYOUT:
                return new LoadMoreHolder(inflateLayout(parent, R.layout.item_load_more));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == RELEASE_LAYOUT) {
            ((ReleaseItemHolder) holder).bind(getItem(position), position);
        } else if (itemType == LOAD_MORE_LAYOUT) {
            holder.bind(position);
        }
    }

    @Override
    public int getItemCount() {
        int count = super.getItemCount();
        if (count > 0) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == (getItemCount() - 1)) {
            return LOAD_MORE_LAYOUT;
        }
        return RELEASE_LAYOUT;
    }

    public void insertMore(List<ReleaseItem> list) {
        int prevItems = getItemCount();
        this.items.addAll(list);
        Log.d("SUKA", "insertMore " + prevItems + " : " + getItemCount());
        notifyItemRangeInserted(prevItems, getItemCount());
    }

    class ReleaseItemHolder extends BaseViewHolder<ReleaseItem> {
        ImageView image;
        TextView title;
        TextView desc;

        ReleaseItemHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            desc = itemView.findViewById(R.id.item_desc);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getItem(getLayoutPosition()));
                }
            });
        }

        @Override
        public void bind(ReleaseItem item, int position) {
            title.setText(String.format("%s (%s)", item.getTitle(), item.getEpisodesCount()));
            desc.setText(item.getDescription());
            ImageLoader.getInstance().displayImage(item.getImage(), image);
        }
    }

    private class LoadMoreHolder extends BaseViewHolder {
        private LinearLayout container;
        private Button btn;

        LoadMoreHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.nl_lm_container);
            btn = itemView.findViewById(R.id.nl_lm_btn);
            btn.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }

        @Override
        public void bind(int position) {
            Log.d("SUKA", "BIND LOAD_MORE");
            if (listener != null) {
                listener.onLoadMore();
            }
        }
    }

    public interface ItemListener extends OnItemClickListener<ReleaseItem> {
        void onLoadMore();
    }
}
