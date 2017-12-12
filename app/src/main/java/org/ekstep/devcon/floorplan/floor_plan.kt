package org.ekstep.devcon.floorplan

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.ekstep.devcon.R

/**
 * Created by souvikmondal on 11/12/17.
 */

val FLOOR_KEY = "floor"

class FloorPlanDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_floor_plan)

//        setContentView(layoutId(intent.extras[FLOOR_KEY] as Int))

        var floorPlanDetailFragment : FloorPlanDetailsFragment = FloorPlanDetailsFragment();

        var bundle : Bundle = Bundle();
        bundle.putInt("screen",intent.extras[FLOOR_KEY] as Int)

        floorPlanDetailFragment.arguments = bundle

        fragmentManager.beginTransaction().add(R.id.container, floorPlanDetailFragment).commit()
    }

//    private fun layoutId(floor_no: Int): Int {
//        when (floor_no) {
//            0 -> return R.layout.first_floor
//            1 -> return R.layout.second_floor
//            else -> return R.layout.third_floor
//        }
//    }

}