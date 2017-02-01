package com.example.nikita.sncleaderboardsearcher;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by NikitaShuvalov on 1/17/17.
 */

public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsRecyclerAdapter.ResultsViewHolder> {
    private ArrayList<Result> mResults;

    public ResultsRecyclerAdapter(ArrayList<Result> results) {
        mResults = results;
    }

    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_results,null);
        return new ResultsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, int position) {
        Result result =  mResults.get(position);
        if(ResultsManager.getInstance().getSearchName().toLowerCase().equals(result.getName().toLowerCase())) {
            holder.mRelativeLayout.setBackgroundColor(Color.argb(75,0,200,0));
        }else{
            holder.mRelativeLayout.setBackgroundColor(Color.WHITE);
        }

        holder.bindDataToViews(mResults.get(position));

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder{
        private TextView mPositionView, mNameView, mPointsView, mCurrentView, mProjectedView;
        private RelativeLayout mRelativeLayout;

        public ResultsViewHolder(View itemView) {
            super(itemView);
            mPositionView = (TextView) itemView.findViewById(R.id.position_text);
            mNameView =(TextView) itemView.findViewById(R.id.name_text);
            mPointsView = (TextView)itemView.findViewById(R.id.points_text);
            mCurrentView = (TextView)itemView.findViewById(R.id.current_cash_text);
            mProjectedView = (TextView)itemView.findViewById(R.id.projected_cash_text);
            mRelativeLayout = (RelativeLayout)itemView.findViewById(R.id.result_container);
        }

        public void bindDataToViews(Result result){
            Log.d("Result", "bindDataToViews: " + result.getPosition());
            mPositionView.setText(result.getPosition());
            mNameView.setText(result.getName());
            mPointsView.setText(result.getPoints());
            String currentCash = result.getCurrentCash();
            if(currentCash.length()<8 && Float.parseFloat(result.getCurrentCash().substring(1))==0f){
                mCurrentView.setTextColor(Color.argb(255,120,0,0));
            }else{
                mCurrentView.setTextColor(Color.argb(255,0,120,0));
            }
            mCurrentView.setText(currentCash);

            String projectedCash = result.getProjectedCash();
            if(projectedCash.length()<8 && Float.parseFloat(result.getProjectedCash().substring(1))==0f){
                mProjectedView.setTextColor(Color.argb(255,120,0,0));
            }else{
                mProjectedView.setTextColor(Color.argb(255,0,120,0));
            }
            mProjectedView.setText(projectedCash);
        }
    }
}
