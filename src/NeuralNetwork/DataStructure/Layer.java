package NeuralNetwork.DataStructure;

import Function.ActivationFunction;

public class Layer
{
    /*
    *  Esta classe é responsável por uma camada de neurônios
    *  na estrura de dados da rede neural. Ela contém um ou
    *  mais neurônios, representada pelo esquema abaixo.
    *
    *                +-------+
    *                | 1st N.|
    *                | 2nd N.|
    *                | 3rd N.|
    *                |  ...  |
    *                |       |
    *                +-------+
    *
    * */

    private Neuron[] neurons;
    private boolean isEntryLayer = false;
    private String activationFunction;

    public Layer (int neuronAmount, ActivationFunction activationFunction)
    {
        neurons = new Neuron[neuronAmount];
        this.activationFunction = activationFunction.toString();
        for (int i = 0; i < neurons.length; i++) neurons[i] = new Neuron(activationFunction);
    }

    Layer (int neuronAmount) // constroi input layer
    {
        neurons = new Neuron[neuronAmount];
        isEntryLayer = true;
        for (int i = 0; i < neurons.length; i++) neurons[i] = new Neuron();
    }

    // Este método recebe um vetor de entradas e as utiliza
    // para definir a entrada de cada neurônio.
    public void setInputs(double[] inputs)
    {
        if (inputs.length != neurons.length) return;
        for (int i = 0; i < inputs.length; i++) neurons[i].setLastInputReceived(inputs[i]);
    }

    // Este método retorna as saídas da camada utilizando
    // a função de ativação. Obs.: se a camada for de entrada
    // na rede neural, será retornado a mesma entrada que o
    // neurônio recebeu.
    public double[] getOutputs()
    {
        double[] outputs = new double[neurons.length];
        for (int i = 0; i < neurons.length; i++) outputs[i] = neurons[i].getOutput();
        return outputs;
    }

    // Este método retorna o vetor de neurônios.
    public Neuron[] getNeurons() {
        return neurons;
    }

    // Este método retorna a função de ativação utilizada pela camada
    public String getActivationFunction() {
        return activationFunction;
    }

    // Este método retorna um booleano indicando
    // se a camada é de entrada ou não.
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
