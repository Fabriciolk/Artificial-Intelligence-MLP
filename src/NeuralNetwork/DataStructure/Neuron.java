package NeuralNetwork.DataStructure;

import Function.ActivationFunction;

public class Neuron
{
    private ActivationFunction activationFunction;
    private double lastInputReceived;

    public Neuron (ActivationFunction activationFunction)
    {
        this.activationFunction = activationFunction;
    }

    public Neuron () {}

    public double getOutput()
    {
        if (activationFunction == null) return lastInputReceived;
        return activationFunction.getImage(lastInputReceived);
    }

    public double getImageFromDerivativeFunction(double input)
    {
        return activationFunction.getDerivative().getImage(input);
    }

    public double getLastInputReceived() {
        return lastInputReceived;
    }

    public void setLastInputReceived(double lastInputReceived) {
        this.lastInputReceived = lastInputReceived;
    }
}
