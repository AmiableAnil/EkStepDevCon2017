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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ekstep.devcon.R;
import org.ekstep.devcon.game.QRScanActivity;

/**
 * Created by Sneha on 12/12/2017.
 */

public class HomeFragment extends Fragment {
    //    String[] floorArray = new String[]{"FIRST FLOOR",
//            "SECOND FLOOR", "FOURTH FLOOR", "TREASURE HUNT"};
    String[] floorArray = new String[]{"FLOOR PLAN", "TREASURE HUNT"};
    String[] subtitles = new String[]{"Find your way!!", "Solve the puzzle and find the hidden treasure!!"};
    int[] icons = {R.drawable.map, R.drawable.treasure};
    //    String[] subtitles = new String[]{"Adoption, Reliability, Mobility, Quality",
//            "Agility, Scalability", "Innovation", "Play the game!!"};
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

        FloorAdapter floorAdapter = new FloorAdapter(floorArray, subtitles, icons);
        recyclerView.setAdapter(floorAdapter);

        View scanQRCode = view.findViewById(R.id.scan_qr_code);
        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = IntentIntegrator
                        .forSupportFragment(HomeFragment.this);
                intentIntegrator.initiateScan();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                if (getActivity() instanceof LandingActivity) {
                    int floor = -1;

                    switch (result.getContents()) {
                        case "RELIABILITY":
                        case "MOBILITY":
                        case "QUALITY":
                        case "ADOPTION":
                            floor = 1;
                            break;
                        case "SCALABILITY":
                        case "AGILITY":
                            floor = 2;
                            break;
                        case "INNOVATION":
                            floor = 3;
                            break;
                    }

                    if (floor > -1) {
                        ((LandingActivity) getActivity()).setFragment(FloorPlanFragment
                                .newInstance(floor, result.getContents()));
                    }
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    class FloorAdapter extends RecyclerView.Adapter<FloorAdapter.FloorViewHolder> {
        private String[] titles;
        private String[] subTitles;
        private int[] icons;

        public FloorAdapter(String[] titles, String[] subTitles, int[] icons) {
            this.titles = titles;
            this.subTitles = subTitles;
            this.icons = icons;
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
            holder.treasureIcon.setImageResource(icons[position]);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 1) {
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
