package org.ekstep.devcon.floorplan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ekstep.devcon.R;
import org.ekstep.devcon.game.QRScanActivity;

/**
 * Created by Sneha on 12/11/2017.
 */

public class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {
    private String[] titles;
    private String[] subTitles;

    public FloorAdapter(String[] titles, String[] subTitles) {
        this.titles = titles;
        this.subTitles = subTitles;
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
    public void onBindViewHolder(final FloorViewHolder holder, final int position) {
        holder.floorName.setText(titles[position]);
        holder.subTitle.setText(subTitles[position]);
        if (position == 3) {
            holder.treasureIcon.setVisibility(View.VISIBLE);
        } else {
            holder.treasureIcon.setVisibility(View.GONE);
        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 3) {
                    Intent intent = new Intent(holder.cv.getContext(), QRScanActivity.class);
                    holder.cv.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(holder.cv.getContext(), FloorPlanDetailsActivity.class);
                    intent.putExtra(Floor_planKt.getFLOOR_KEY(), position);
                    holder.cv.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class FloorViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView floorName;
        TextView subTitle;
        ImageView treasureIcon;

        public FloorViewHolder(final View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.parent);
            subTitle = itemView.findViewById(R.id.subtitle);
            floorName = itemView.findViewById(R.id.tv_floorname);
            treasureIcon = itemView.findViewById(R.id.treasure_icon);
        }
    }
}
