package org.ekstep.devcon;

import android.annotation.SuppressLint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Sneha on 12/11/2017.
 */

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {
    private String[] floorData;

    public FloorAdapter(String[] floorArray) {
        this.floorData = floorArray;
    }

    @Override
    public FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_floor, parent, false);

        FloorViewHolder viewHolder = new FloorViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(FloorViewHolder holder, int position) {
        holder.floorName.setText(floorData[position]);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on click of floor
            }
        });
    }

    @Override
    public int getItemCount() {
        return floorData.length;
    }

    public class FloorViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView floorName;

        public FloorViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.parent);
            floorName = (TextView) itemView.findViewById(R.id.tv_floorname);
        }
    }
}
