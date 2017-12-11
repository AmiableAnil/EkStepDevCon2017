package org.ekstep.devcon.floorplan

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import org.ekstep.devcon.R

/**
 * Created by souvikmondal on 11/12/17.
 */

val FLOOR_KEY = "floor"

class FloorPlanDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId(intent.extras[FLOOR_KEY] as Int))
    }

    private fun layoutId(floor_no: Int): Int {
        when (floor_no) {
            0 -> return R.layout.first_floor
            1 -> return R.layout.second_floor
            else -> return R.layout.third_floor
        }

    }

}