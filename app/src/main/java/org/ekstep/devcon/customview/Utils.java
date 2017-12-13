package org.ekstep.devcon.customview;

import android.content.res.Resources;

import java.util.Locale;

public final class Utils {

    private Utils() {
    }

    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp) {
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    /**
     * Converts long into Human Readable String
     *
     * @param size
     * @return String
     */
    public static String bytesToHuman(long size) {
        long Kb = 1 * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size < Kb) return floatForm(size) + "";
        if (size >= Kb && size < Mb) return floatForm((double) size / Kb) + "";
        if (size >= Mb && size < Gb) return floatForm((double) size / Mb) + "";
        if (size >= Gb && size < Tb) return floatForm((double) size / Gb) + "";
        if (size >= Tb && size < Pb) return floatForm((double) size / Tb) + "";
        if (size >= Pb && size < Eb) return floatForm((double) size / Pb) + "";
        if (size >= Eb) return floatForm((double) size / Eb) + "";

        return "0.00";
    }

    /**
     * Formats double to string
     *
     * @return String
     */
    public static String floatForm(double d) {
        return String.format(Locale.US, "%.2f", d);
    }
}