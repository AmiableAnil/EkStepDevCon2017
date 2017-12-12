package org.ekstep.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.ekstep.devcon.R;

/**
 * Created by Sneha on 12/12/2017.
 */

public class FloorPlanFragment extends Fragment {

    private static final String TAG = "FloorPlanFragment";

    public enum Stall {
        RELIABILITY,
        MOBILITY,
        QUALITY,
        ADOPTION,
        SCALABILITY,
        AGILITY,
        INNOVATION
    }

    private int mScreen = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();

        if (bundle != null) {
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
                return R.layout.third_floor;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layoutId(mScreen), null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mScreen == 0) {
            setUpClickListener(view, R.id.mobility_layout, Stall.MOBILITY, R.color.color_mobility);
            setUpClickListener(view, R.id.quality_layout, Stall.QUALITY, R.color.color_quality);
            setUpClickListener(view, R.id.stall_3_layout, Stall.ADOPTION, R.color.color_adoption);
            setUpClickListener(view, R.id.innovation_layout, Stall.RELIABILITY, R.color.color_reliability);
        } else if (mScreen == 1) {
//            setUpClickListener(view, R.id.mobility_layout, Stall.MOBILITY);
            setUpClickListener(view, R.id.quality, Stall.AGILITY, R.color.color_agility);
            setUpClickListener(view, R.id.stall_3, Stall.AGILITY, R.color.color_agility);
            setUpClickListener(view, R.id.innovation_layout, Stall.SCALABILITY, R.color.color_scalibility);
        } else {
            setUpClickListener(view, R.id.innovation_layout, Stall.INNOVATION, R.color.color_innovation);
        }

    }

    private void setUpClickListener(View view, int id, final Stall stall, final int colorResId) {
        final View button = view.findViewById(id);

        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDetailDialog(button, stall, ResourcesCompat.getColor(getResources(), colorResId,
                            getActivity().getTheme()));
                }
            });
        }
    }

    private void showDetailDialog(View view, Stall stall, int colorValue) {
        Log.i(TAG, "showDetailDialog: " + stall);
        String title = stall.toString();

        float pivotX = view.getPivotX() / 2f;
        float pivotY = view.getPivotY() / 2f;
        FloorDetailDialogFragment fragment = FloorDetailDialogFragment
                .newInstance(title, "http://github.com/", colorValue, view.getRight(), view.getBottom());
        fragment.show(getChildFragmentManager(), FloorDetailDialogFragment.class.toString());
    }
}
