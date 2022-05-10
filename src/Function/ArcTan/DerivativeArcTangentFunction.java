package Function.ArcTan;

import Function.ActivationFunction;

public class DerivativeArcTangentFunction implements ActivationFunction
{
    private ArcTangentFunction arcTangentFunction;

    DerivativeArcTangentFunction (ArcTangentFunction arcTangentFunction)
    {
        this.arcTangentFunction = arcTangentFunction;
    }

    @Override
    public double getImage(double input) {
        return 2 / (Math.PI * (input * input + 1));
    }

    public ArcTangentFunction getArcTangentFunction()
    {
        return arcTangentFunction;
    }

    @Override
    public ActivationFunction getDerivative() {
        return null;
    }
}
