package org.ekstep.devcon.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.ekstep.devcon.R;
import org.ekstep.devcon.game.QRScanActivity;

/**
 * Created by Sneha on 12/12/2017.
 */

public class HomeFragment extends Fragment {
    String[] floorArray = new String[]{"FIRST FLOOR",
            "SECOND FLOOR", "FOURTH FLOOR", "TREASURE HUNT"};
    String[] subtitles = new String[]{"Adoption, Reliability, Mobility, Quality",
            "Agility, Scalability", "Innovation", "Play the game!!"};
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_floor_plan, container, false);
        initViews(rootView);
        ((LandingActivity) getActivity()).setTitle("WELCOME");
        return rootView;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_floor);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        FloorAdapter floorAdapter = new FloorAdapter(floorArray, subtitles);
        recyclerView.setAdapter(floorAdapter);
    }

    class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {
        private String[] titles;
        private String[] subTitles;

        public FloorAdapter(String[] titles, String[] subTitles) {
            this.titles = titles;
            this.subTitles = subTitles;
        }

        @Override
        public FloorAdapter.FloorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_floor, parent, false);

            FloorAdapter.FloorViewHolder viewHolder = new FloorAdapter.FloorViewHolder(view);
            return viewHolder;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onBindViewHolder(final FloorAdapter.FloorViewHolder holder, final int position) {
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
                        ((LandingActivity) getActivity()).setFloorFragment();
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
}
