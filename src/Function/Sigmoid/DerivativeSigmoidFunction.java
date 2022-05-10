package Function.Sigmoid;

import Function.ActivationFunction;

public class DerivativeSigmoidFunction implements ActivationFunction
{
    private SigmoidFunction sigmoidFunction;

    DerivativeSigmoidFunction(SigmoidFunction sigmoidFunction)
    {
        this.sigmoidFunction = sigmoidFunction;
    }

    @Override
    public double getImage(double input)
    {
        return sigmoidFunction.getImage(input) * (1 - sigmoidFunction.getImage(input));
    }

    @Override
    public ActivationFunction getDerivative() {
        return null;
    }

    public SigmoidFunction getSigmoidFunction()
    {
        return sigmoidFunction;
    }
}
