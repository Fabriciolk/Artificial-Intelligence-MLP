package Function.ArcTan;

import Function.ActivationFunction;

public class ArcTangentFunction implements ActivationFunction
{
    private final DerivativeArcTangentFunction derivativeArcTangentFunction = new DerivativeArcTangentFunction(this);

    @Override
    public double getImage(double input)
    {
        return (2 / Math.PI) * (Math.atan(input));
    }

    public DerivativeArcTangentFunction getDerivative()
    {
        return derivativeArcTangentFunction;
    }
}
