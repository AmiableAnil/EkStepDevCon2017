package org.ekstep.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ekstep.devcon.R;
import org.ekstep.devcon.util.Constant;

/**
 * Created by Sneha on 12/12/2017.
 */

public class FloorPlanFragment extends Fragment {

    private int mScreen = 0;

//    private CategoryFragmentInteractionListener mListener;

    public FloorPlanFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();

        if (bundle != null) {
            mScreen = bundle.getInt(Constant.BUNDLE_KEY_SCREEN_NUM, 1);
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FloorPlanFragment newInstance(int sectionNumber) {
        FloorPlanFragment fragment = new FloorPlanFragment();
        Bundle args = new Bundle();
        args.putInt(Constant.BUNDLE_KEY_SCREEN_NUM, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private int layoutId(int floor) {
        switch (floor) {
            case 1:
                return R.layout.first_floor;
            case 2:
                return R.layout.second_floor;
            default:
                return R.layout.third_floor;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(mScreen), null);
        return view;
    }
}
