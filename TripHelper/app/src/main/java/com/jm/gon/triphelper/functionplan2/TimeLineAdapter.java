package com.jm.gon.triphelper.functionplan2;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jm.gon.triphelper.CustomClickListener;
import com.jm.gon.triphelper.R;
import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<TimeLineModel> timeLineModelList;
    private CustomClickListener customClickListener;
    private int clickposition;
    private Context context;

    public interface ListClickHandler{
        void onClick(TimeLineModel timeLineModel);
    }

    public TimeLineAdapter(List<TimeLineModel> modelList, CustomClickListener listClickHandler, Context context){
        timeLineModelList = modelList;
        this.customClickListener = listClickHandler;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }


    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(final TimeLineViewHolder holder, int position) {
        clickposition = position;
        holder.title.setText(timeLineModelList.get(position).getTitle());
        holder.addr.setText(timeLineModelList.get(position).getAddr());
        Glide.with(context)
                .load(timeLineModelList.get(position).getUrl())
                .bitmapTransform(new CropCircleTransformation(context))
                .into(new SimpleTarget<GlideDrawable>(100,100) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.mTimelineView.setMarker(resource);
                    }
                });
    }
    @Override
    public int getItemCount() {
        return (timeLineModelList != null ? timeLineModelList.size() : 0);
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title;
        private TextView addr;
        private  TimelineView mTimelineView;
        private CardView cd_TimeLineAdapter;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_TimeLineAdapter_title);
            addr = (TextView)itemView.findViewById(R.id.tv_TimeLineAdapter_addr);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            cd_TimeLineAdapter = (CardView)itemView.findViewById(R.id.cd_TimeLineAdapter);
            cd_TimeLineAdapter.setOnClickListener(this);
            mTimelineView.setOnClickListener(this);
            title.setOnClickListener(this);
            mTimelineView.initLine(viewType);
        }
        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != RecyclerView.NO_POSITION){
                customClickListener.onClick(timeLineModelList.get(clickposition));
            }

        }
    }

}
