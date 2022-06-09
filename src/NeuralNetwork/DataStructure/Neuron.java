package NeuralNetwork.DataStructure;

import Function.ActivationFunction;

public class Neuron
{
    /*
    *  Esta classe é responsável por um neurônio. Ele
    *  possui a última entrada recebida e uma função
    *  de ativação para gerar sua saída.
    *
    *                 ******
    *              **        **
    *             **           **
    *  input --> **   Neuron    ** --> output
    *             **           **
    *              **         **
    *                 ******
    *
    * */


    private ActivationFunction activationFunction;
    private double lastInputReceived;

    public Neuron (ActivationFunction activationFunction)
    {
        this.activationFunction = activationFunction;
    }

    public Neuron () {}

    // Este método retorna a saída do neurônio. Se é um neurônio
    // da camada de entrada, não terá uma função de ativação e
    // portanto retornará a útima entrada recebida.
    public double getOutput()
    {
        if (activationFunction == null) return lastInputReceived;
        return activationFunction.getImage(lastInputReceived);
    }

    // Este método retorna o resultado da derivada da função
    // de ativação utilizando a última entrada recebida.
    public double getImageFromDerivativeFunction(double input)
    {
        return activationFunction.getDerivative().getImage(input);
    }

    // Este método retorna a última entrada recebida.
    public double getLastInputReceived() {
        return lastInputReceived;
    }

    // Este método altera a última entrada recebida.
    public void setLastInputReceived(double lastInputReceived) {
        this.lastInputReceived = lastInputReceived;
    }
}
