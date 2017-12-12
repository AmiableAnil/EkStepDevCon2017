package org.ekstep.devcon.floorplan;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import org.ekstep.devcon.R;

/**
 * Created by Sneha on 12/11/2017.
 */

public class FloorPlanActivity extends Activity {
//    String[] floorArray = new String[]{"FIRST FLOOR",
//            "SECOND FLOOR", "FOURTH FLOOR", "TREASURE HUNT"};
//    String[] subtitles = new String[]{"Adoption, Reliability, Mobility, Quality",
//            "Agility, Scalability", "Innovation", "Play the game!!"};
//    RecyclerView.LayoutManager layoutManager;
//    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_floor_plan);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, new FloorPlanFragment())
                .commit();

//        recyclerView = (RecyclerView) findViewById(R.id.rv_floor);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//
//        FloorAdapter floorAdapter = new FloorAdapter(floorArray, subtitles);
//        recyclerView.setAdapter(floorAdapter);
    }
}
