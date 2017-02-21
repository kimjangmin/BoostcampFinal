package com.example.gon.triphelper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vipul.hp_hp.timelineview.TimelineView;

import java.util.List;

/**
 * Created by 김장민 on 2017-02-21.
 */

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<TimeLineModel> timeLineModelList;
    public TimeLineAdapter(List<TimeLineModel> modelList){
        timeLineModelList = modelList;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }


    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = view = View.inflate(parent.getContext(), R.layout.timeline, null);
        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {
        holder.name.setText("title = "+timeLineModelList.get(position).getTel()+ "\n " +
                "addr = "+timeLineModelList.get(position).getAddr() + "\n tel ="+timeLineModelList.get(position).getTel()
        );
    }

    @Override
    public int getItemCount() {
        return (timeLineModelList != null ? timeLineModelList.size() : 0);
    }


    public class TimeLineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public  TimelineView mTimelineView;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tx_name);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            mTimelineView.initLine(viewType);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
