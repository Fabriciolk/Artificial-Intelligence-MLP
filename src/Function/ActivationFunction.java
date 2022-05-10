package Function;

public interface ActivationFunction
{
    double getImage(double input);

    ActivationFunction getDerivative();
}
