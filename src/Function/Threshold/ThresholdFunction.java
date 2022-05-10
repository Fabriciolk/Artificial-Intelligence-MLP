package Function.Threshold;

import Function.ActivationFunction;

public class ThresholdFunction implements ActivationFunction
{
    private final double THRESHOLD;
    private final double[] binaryResults;

    public ThresholdFunction (double THRESHOLD, double[] binaryResults) throws Exception
    {
        this.THRESHOLD = THRESHOLD;
        this.binaryResults = binaryResults;
        if (binaryResults.length != 2) throw new Exception();
    }

    @Override
    public ActivationFunction getDerivative() {
        return null;
    }

    @Override
    public double getImage(double input) {
        if (input < THRESHOLD) {
            return binaryResults[0];
        }
        else {
            return binaryResults[1];
        }
    }
}
