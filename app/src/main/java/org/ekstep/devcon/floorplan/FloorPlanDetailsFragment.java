package org.ekstep.devcon.floorplan;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.ekstep.devcon.R;

/**
 * Created by Sneha on 12/12/2017.
 */

public class FloorPlanDetailsFragment extends Fragment {

    private int mScreen = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();

        if(bundle != null) {
            mScreen = bundle.getInt("screen", 0);
        }
    }

    private int layoutId(int floor) {
        switch (floor) {
            case 0:
                return R.layout.first_floor;
            case 1:
                return R.layout.second_floor;
                default:
                    return R.layout.third_floor;        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(mScreen), null);
        return view;
    }
}
