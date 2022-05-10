package Function.Sigmoid;

import Function.ActivationFunction;

public class SigmoidFunction implements ActivationFunction
{
    private final DerivativeSigmoidFunction derivativeSigmoidFunction = new DerivativeSigmoidFunction(this);

    @Override
    public double getImage(double input)
    {
        return 1 / (1 + Math.exp(-input));
    }

    public DerivativeSigmoidFunction getDerivative()
    {
        return derivativeSigmoidFunction;
    }
}
