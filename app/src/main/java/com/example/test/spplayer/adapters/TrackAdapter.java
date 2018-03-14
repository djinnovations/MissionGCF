package com.example.test.spplayer.adapters;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test.spplayer.MyApplication;
import com.example.test.spplayer.R;
import com.example.test.spplayer.models.Track;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 14-03-2018.
 */

public class TrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GenericAdapterInterface{

    public static final int DEFAULT = 100;

    private List<Track> mTracks = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getRootLayout(viewType), parent, false);
        return getViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseItemHolder) holder).onItemViewUpdate(mTracks.get(position), holder, position);
    }

    @Override
    public void changeData(List dataList) throws Exception {
        if (dataList == null)
            return;
        if (dataList.size() <= 0)
            return;
        if (!(dataList.get(0) instanceof Track))
            throw new IllegalArgumentException("Required data type \"Track\"");
        this.mTracks.clear();
        this.mTracks.addAll(dataList);
        (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 100);
    }

    @Override
    public int getItemCount() {
        return mTracks.size();
    }


    @Override
    public int getRootLayout(int viewType) {
        if (viewType == DEFAULT)
            return R.layout.track_list_row;
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mTracks.get(position).getViewType();
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view, int viewType) {
        if (viewType == DEFAULT)
            return new TrackAdapter.ViewHolder(view);
        return null;
    }

    @Override
    public void setOnClickListener(RecyclerView.ViewHolder holder) {
        holder.itemView.setOnClickListener((View.OnClickListener) holder);
    }

    class ViewHolder extends BaseItemHolder implements View.OnClickListener{

        @BindView(R.id.track_image)
        ImageView trackImageView;
        @BindView(R.id.track_title)
        TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setOnClickListener(this);
        }

        @Override
        public void onItemViewUpdate(Object dataObject, RecyclerView.ViewHolder holder, int position) {
            Track data = (Track) dataObject;

            titleTextView.setText(data.getTitle());
            Picasso.with(MyApplication.getInstance())
                    .load(data.getArtworkURL())
                    .into(trackImageView);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(mTracks.get(getAdapterPosition()));
        }
    }
}
