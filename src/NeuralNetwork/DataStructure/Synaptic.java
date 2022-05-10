package NeuralNetwork.DataStructure;

import java.util.Arrays;
import java.util.Random;

public class Synaptic
{
    private final Layer originLayer;
    private final Layer destinyLayer;
    private double[][] weights;
    private double[][] initialWeights;
    private double[] destinyLayerBias;
    private double[] initialDestinyLayerBias;
    private boolean initialWeightsAndBiasDefined = false;

    Synaptic (Layer originLayer, Layer destinyLayer)
    {
        this.originLayer = originLayer;
        this.destinyLayer = destinyLayer;
        weights = new double[originLayer.getNeurons().length][destinyLayer.getNeurons().length];
        destinyLayerBias = new double[destinyLayer.getNeurons().length];
    }

    public void makeSynapse()
    {
        double linearCombination = 0;

        for (int i = 0; i < destinyLayer.getNeurons().length; i++)
        {
            for (int j = 0; j < originLayer.getNeurons().length; j++)
            {
                linearCombination += originLayer.getNeurons()[j].getOutput() * weights[j][i];
            }

            linearCombination += 1 * destinyLayerBias[i];
            destinyLayer.getNeurons()[i].setLastInputReceived(linearCombination);
        }

        System.out.println("Camada Oculta:");
        for (int i = 0; i < destinyLayer.getNeurons().length; i++)
        {
            System.out.printf("Neuronio %d: input %.2f e output %.2f\n", i + 1, destinyLayer.getNeurons()[i].getLastInputReceived(), destinyLayer.getNeurons()[i].getOutput());
        }
        System.out.println();
    }

    public double[] adjustWeightsAndBias(double learningRate, double[] correctionErrorsFromDestinyLayer)
    {
        double[] correctionErrorsToOriginLayer = getCorrectionErrorsForOriginLayer(correctionErrorsFromDestinyLayer);

        for (int i = 0; i < correctionErrorsFromDestinyLayer.length; i++)
        {
            for (int j = 0; j < weights.length; j++)
            {
                weights[j][i] += learningRate * correctionErrorsFromDestinyLayer[i] * originLayer.getNeurons()[j].getOutput();
            }

            destinyLayerBias[i] += learningRate * correctionErrorsFromDestinyLayer[i];
        }

        return correctionErrorsToOriginLayer;
    }

    private double[] getCorrectionErrorsForOriginLayer (double[] correctionErrorsFromDestinyLayer)
    {
        if (originLayer.isEntryLayer()) return null;
        double[] correctionErrorsForOriginLayer = new double[originLayer.getNeurons().length];

        for (int i = 0; i < originLayer.getNeurons().length; i++)
        {
            double correctionErrorIn = 0;

            for (int j = 0; j < destinyLayer.getNeurons().length; j++)
            {
                correctionErrorIn += correctionErrorsFromDestinyLayer[j] * weights[i][j];
            }

            correctionErrorsForOriginLayer[i] = correctionErrorIn * originLayer.getNeurons()[i].getImageFromDerivativeFunction(originLayer.getNeurons()[i].getLastInputReceived());
        }

        return correctionErrorsForOriginLayer;
    }

    public void setWeightsAndBiasRandomly()
    {
        if (initialWeightsAndBiasDefined) return;
        initialWeights = new double[originLayer.getNeurons().length][destinyLayer.getNeurons().length];
        initialDestinyLayerBias = new double[destinyLayer.getNeurons().length];
        Random random = new Random();

        for (int i = 0; i < initialWeights.length; i++)
        {
            for (int j = 0; j < initialWeights[0].length; j++)
            {
                initialWeights[i][j] = random.nextDouble();
                weights[i][j] = initialWeights[i][j];
            }
        }

        for (int i = 0; i < destinyLayerBias.length; i++)
        {
            destinyLayerBias[i] = random.nextDouble();
            initialDestinyLayerBias[i] = destinyLayerBias[i];
        }

        initialWeightsAndBiasDefined = true;
    }

    public void resetWeightsAndBias()
    {
        weights = new double[weights.length][weights[0].length];
        destinyLayerBias = new double[weights[0].length];
        initialWeights = null;
        initialWeightsAndBiasDefined = false;
    }

    public void printWeightsAndBias()
    {
        System.out.println();

        for (int i = 0; i < weights.length; i++)
        {
            System.out.print("Neuronio Origem " + (i + 1) + "\t");

            for (int j = 0; j < weights[0].length; j++)
            {
                System.out.printf("%.2f \t", weights[i][j]);
            }

            System.out.println();
        }

        System.out.print("\nDestiny Bias: ");

        for (int i = 0 ; i < destinyLayerBias.length; i++)
        {
            System.out.printf("%.2f \t", destinyLayerBias[i]);
        }

        System.out.println();
    }

    @Override
    public String toString() {
        return "Synaptic{" +
                "originLayer=" + originLayer.toString() +
                ", destinyLayer=" + destinyLayer.toString() +
                ", weights=" + Arrays.toString(weights) +
                ", destinyLayerBias=" + Arrays.toString(destinyLayerBias) +
                '}';
    }
}
