package org.ekstep.devcon.floorplan;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ekstep.devcon.R;

/**
 * Created by Sneha on 12/12/2017.
 */

public class FloorPlanFragment extends Fragment {
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
        return rootView;
    }

    private void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_floor);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        FloorAdapter floorAdapter = new FloorAdapter(floorArray, subtitles);
        recyclerView.setAdapter(floorAdapter);
    }
}
