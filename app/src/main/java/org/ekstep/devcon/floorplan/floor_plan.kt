package org.ekstep.devcon.floorplan

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.ekstep.devcon.R

/**
 * Created by souvikmondal on 11/12/17.
 */

enum class FLOOR_NO(i: String) {
    FIRST("1"), SECOND("2"), THIRD("3")
}

val FLOOR_KEY = "floor"

class FloorplanFragment: Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val floor: FLOOR_NO = FLOOR_NO.valueOf(arguments[FLOOR_KEY] as String)
        val view: View = inflater!!.inflate(layoutId(floor), container, false)
        return view
    }

    private fun layoutId(floor_no: FLOOR_NO): Int {
        when (floor_no) {
            FLOOR_NO.FIRST -> return R.layout.first_floor
            FLOOR_NO.SECOND -> return R.layout.second_floor
            else -> return R.layout.third_floor
        }

    }

}