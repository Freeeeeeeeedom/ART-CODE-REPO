package dt.mutant.Variance;
import dt.original.*;

// This is mutant program.
// Author : ysma

public class Variance_AOIS_21 extends Variance
{

    public double exe( int[] v )
    {
        double sum = 0;
        for (int i = 0; i < v.length; i++) {
            sum += v[i];
        }
        double mean = sum / (double) v.length;
        double var = 0;
        for (int i = 0; ++i < v.length; i++) {
            double dif = v[i] - mean;
            var += dif * dif;
        }
        return var;
    }

}
