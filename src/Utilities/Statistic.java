package Utilities;

public class Statistic
{
    /*
    * Esta classe é responsável por conter métodos
    * gerais relacionados a estatística.
    * */

    public static double getMean(double[] values)
    {
        double sum = 0;

        for (double value : values) sum += value;

        return sum / values.length;
    }

    public static double getVariance(double[] values)
    {
        double mean = getMean(values);
        double internSum = 0;

        for (double value : values) internSum += Math.pow(value - mean, 2);

        return internSum / values.length;
    }

    public static double getStandardDeviation(double[] values)
    {
        double variance = getVariance(values);
        return Math.sqrt(variance);
    }
}
