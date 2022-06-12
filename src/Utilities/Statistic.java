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

    public static double getMax(double[] values)
    {
        if (values.length == 0) new Exception().printStackTrace();
        return values[getIndexMax(values)];
    }

    public static int getIndexMax(double[] values)
    {
        int index = 0;
        double max = values[0];

        for (int i = 0; i < values.length; i++)
        {
            if (values[i] > max)
            {
                max = values[i];
                index = i;
            }
        }

        return index;
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
