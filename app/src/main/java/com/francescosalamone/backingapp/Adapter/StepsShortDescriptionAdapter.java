package com.francescosalamone.backingapp.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.francescosalamone.backingapp.Model.Steps;
import com.francescosalamone.backingapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Francesco on 11/04/2018.
 */

public class StepsShortDescriptionAdapter extends RecyclerView.Adapter<StepsShortDescriptionAdapter.ViewHolder>{
    private List<Steps> steps = new ArrayList<>();
    private final ItemClickListener clickListener;
    private int background = -1;
    private int textColor = -1;

    public StepsShortDescriptionAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ItemClickListener{
        void onItemClicked(int position);
    }

    @Override
    public StepsShortDescriptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.steps_short_description_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepsShortDescriptionAdapter.ViewHolder holder, int position) {
        Steps step = steps.get(position);

        holder.stepNumber.setText(String.valueOf(position+1));

        if(!step.getShortDescription().equals("")){
            holder.shortDescription.setText(step.getShortDescription());
        } else {
            holder.shortDescription.setText(R.string.step_not_available);
        }

        if(position == 0){
            holder.lineUp.setVisibility(GONE);
        } else {
            holder.lineUp.setVisibility(View.VISIBLE);
        }

        if(position == steps.size()-1){
            holder.lineDown.setVisibility(GONE);
        } else {
            holder.lineDown.setVisibility(View.VISIBLE);
        }

        if(background != -1){
            holder.lineUp.setBackgroundColor(background);
            holder.lineDown.setBackgroundColor(background);
            holder.circleStep.setColorFilter(background);
        }
        if(textColor != -1){
            holder.stepNumber.setTextColor(textColor);
        }
    }

    @Override
    public int getItemCount() {
        if(steps == null) {
            return 0;
        } else {
            return steps.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView stepNumber;
        private TextView shortDescription;
        private ImageView circleStep;
        private View lineUp;
        private View lineDown;
        private ImageView forkAndKnife;

        public ViewHolder(View itemView) {
            super(itemView);

            stepNumber = itemView.findViewById(R.id.step_number_fragment_tv);
            shortDescription = itemView.findViewById(R.id.short_description_fragment_tv);
            lineUp = itemView.findViewById(R.id.line_up);
            lineDown = itemView.findViewById(R.id.line_down);
            circleStep = itemView.findViewById(R.id.step_circle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int itemClicked = getAdapterPosition();
            clickListener.onItemClicked(itemClicked);

            launchDetailStep(view.getContext(), itemClicked);
        }
    }

    private void launchDetailStep(Context context, int position){
        //TODO call the step activity / fragment
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
