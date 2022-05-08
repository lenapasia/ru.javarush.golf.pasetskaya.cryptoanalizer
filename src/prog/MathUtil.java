package prog;

public class MathUtil {
    /**
     * взято из org.apache.commons.math3.stat.inference.ChiSquareTest
     */
    public static double chiSquare(final double[] expected, final long[] observed) {
        double sumExpected = 0d;
        double sumObserved = 0d;
        for (int i = 0; i < observed.length; i++) {
            sumExpected += expected[i];
            sumObserved += observed[i];
        }
        double ratio = 1.0d;
        boolean rescale = false;
        if (abs(sumExpected - sumObserved) > 10E-6) {
            ratio = sumObserved / sumExpected;
            rescale = true;
        }
        double sumSq = 0.0d;
        for (int i = 0; i < observed.length; i++) {
            if (rescale) {
                final double dev = observed[i] - ratio * expected[i];
                sumSq += dev * dev / (ratio * expected[i]);
            } else {
                final double dev = observed[i] - expected[i];
                sumSq += dev * dev / expected[i];
            }
        }
        return sumSq;
    }

    /** Взято из org.apache.commons.math3.util.FastMath */
    private static final long MASK_NON_SIGN_LONG = 0x7fffffffffffffffl;

    /**
     * Взято из org.apache.commons.math3.util.FastMath
     */
    public static double abs(double x) {
        return Double.longBitsToDouble(MASK_NON_SIGN_LONG & Double.doubleToRawLongBits(x));
    }
}