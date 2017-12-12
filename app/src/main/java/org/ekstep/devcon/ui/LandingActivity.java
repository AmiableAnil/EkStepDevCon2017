package org.ekstep.devcon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.ekstep.devcon.R;
import org.ekstep.devcon.customview.IndicatorsView;

/**
 * Created by Sneha on 12/11/2017.
 */

public class LandingActivity extends AppCompatActivity {
    private IndicatorsView mIndicatorsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
//        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        setFragment(new HomeFragment());
    }

    public void setFragment(Fragment fragment) {
        findViewById(R.id.rl_floor_viewpager).setVisibility(View.GONE);
        findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void setFloorFragment() {
        findViewById(R.id.rl_floor_viewpager).setVisibility(View.VISIBLE);
        findViewById(R.id.fragment_container).setVisibility(View.GONE);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TextView floorNumTv = (TextView) findViewById(R.id.tv_floor_num);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mIndicatorsView = findViewById(R.id.indicatorsView);
        mIndicatorsView.setViewPager(viewPager);
        mIndicatorsView.setFloorNumText(floorNumTv);
        mIndicatorsView.setSmoothTransition(true);
        mIndicatorsView.setIndicatorsClickChangePage(true);
        mIndicatorsView.setIndicatorsClickListener(new IndicatorsView.OnIndicatorClickListener() {
            @Override
            public void onClick(int indicatorNumber) {
            }
        });

        // back button
        findViewById(R.id.iv_backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setTitle(String title) {
//        getSupportActionBar().setTitle(title);
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        else return false;
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        getSupportActionBar().setHomeButtonEnabled(false);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//    }
    @Override
    public void onBackPressed() {
        if (findViewById(R.id.fragment_container).getVisibility() == View.VISIBLE) {
            finish();
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
            if (fragment instanceof FloorPlanFragment) {
                setFragment(new HomeFragment());
            } else {
                super.onBackPressed();
            }
        }
    }
}
