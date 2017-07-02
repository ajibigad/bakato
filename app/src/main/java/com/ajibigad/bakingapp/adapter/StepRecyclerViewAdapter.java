package com.ajibigad.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ajibigad.bakingapp.R;
import com.ajibigad.bakingapp.data.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ajibigad on 18/06/2017.
 */

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.ViewHolder> {

    private List<Step> steps = new ArrayList<>();
    private final OnListFragmentInteractionListener<Step> mListener;
    private Context context;

    public StepRecyclerViewAdapter(Context context, OnListFragmentInteractionListener listener, List<Step> steps) {
        mListener = listener;
        this.context = context;
        this.steps = steps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_item, parent, false);
        return new StepRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Step selectedStep = steps.get(position);
        String formattedShortDescription = String.format("%d. %s", position + 1, selectedStep.getShortDescription());
        holder.tvShortDescription.setText(formattedShortDescription);
        if(selectedStep.getThumbnailURL().isEmpty()){
            Picasso.with(context)
                    .load(selectedStep.getThumbnailURL().isEmpty() ? null : selectedStep.getThumbnailURL())
                    .placeholder(R.drawable.step_default_thumbnail)
                    .into(holder.thumbnail);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(selectedStep);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void setSteps(List<Step> steps){
        this.steps = steps;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;

        @BindView(R.id.tv_short_description)
        public TextView tvShortDescription;

        @BindView(R.id.step_thumbnail)
        public ImageView thumbnail;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
