package NeuralNetwork.DataStructure;

import Function.ActivationFunction;

public class Layer
{
    private Neuron[] neurons;
    private boolean isEntryLayer = false;

    public Layer (int neuronAmount, ActivationFunction activationFunction)
    {
        neurons = new Neuron[neuronAmount];
        for (int i = 0; i < neurons.length; i++) neurons[i] = new Neuron(activationFunction);
    }

    Layer (int neuronAmount)
    {
        neurons = new Neuron[neuronAmount];
        isEntryLayer = true;
        for (int i = 0; i < neurons.length; i++) neurons[i] = new Neuron();
    }

    public void setInputs(double[] inputs)
    {
        if (inputs.length != neurons.length) return;
        for (int i = 0; i < inputs.length; i++) neurons[i].setLastInputReceived(inputs[i]);
    }

    public double[] getOutputs()
    {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) outputs[i] = neurons[i].getOutput();
        return outputs;
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public boolean isEntryLayer() {
        return isEntryLayer;
    }

    @Override
    public String toString() {
        return "Layer{" +
                "neurons=" + neurons.length +
                '}';
    }
}
