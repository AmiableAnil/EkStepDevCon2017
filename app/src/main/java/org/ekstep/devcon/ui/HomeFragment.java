package org.ekstep.devcon.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import org.ekstep.devcon.telemetry.ImpressionType;
import org.ekstep.devcon.telemetry.TelemetryBuilder;
import org.ekstep.devcon.telemetry.TelemetryHandler;
import org.ekstep.genieservices.commons.bean.enums.InteractionType;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

/**
 * Created by Sneha on 12/12/2017.
 */

public class HomeFragment extends Fragment {
    String[] floorArray = new String[]{"FLOOR PLAN", "TREASURE HUNT"};
    String[] subtitles = new String[]{"Find your way!!", "Solve the puzzle and find the hidden treasure!!"};
    int[] icons = {R.drawable.map, R.drawable.treasure};

    public static final String WINNER_ACTION = "org.ekstep.WINNER";

    private RecyclerView recyclerView;
    private KonfettiView mConfetti;

    private BroadcastReceiver mWinner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(WINNER_ACTION)) {
                floorArray[1] = "WINNER";
                subtitles[1] = "Congratzzz you won the game....!";
                recyclerView.getAdapter().notifyDataSetChanged();
                showConfetti();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_floor_plan, container, false);
        initViews(rootView);

        TelemetryHandler.saveTelemetry(TelemetryBuilder.buildImpressionEvent("Home", ImpressionType.VIEW, null));

        ((LandingActivity) getActivity()).setTitle("WELCOME");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mWinner,
                new IntentFilter(WINNER_ACTION));
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rv_floor);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        FloorAdapter floorAdapter = new FloorAdapter(floorArray, subtitles, icons);
        recyclerView.setAdapter(floorAdapter);

        View scanQRCode = view.findViewById(R.id.scan_qr_code);
        scanQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.TOUCH, null, "Home", "QRCode"));
                IntentIntegrator intentIntegrator = IntentIntegrator
                        .forSupportFragment(HomeFragment.this);
                intentIntegrator.initiateScan();
            }
        });

        mConfetti = view.findViewById(R.id.viewKonfetti);
    }

    private void showConfetti() {
        mConfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5f), new Size(16, 6f))
                .setPosition(mConfetti.getX() + mConfetti.getWidth() / 2,
                        mConfetti.getY() + mConfetti.getHeight() / 3)
                .stream(300, 5000L);
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
                        TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.TOUCH, null, "Home", "DevCon"));
                        Intent intent = new Intent(holder.cv.getContext(), QRScanActivity.class);
                        holder.cv.getContext().startActivity(intent);
                    } else {
                        TelemetryHandler.saveTelemetry(TelemetryBuilder.buildInteractEvent(InteractionType.TOUCH, null, "Home", "FloorPlan"));
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
