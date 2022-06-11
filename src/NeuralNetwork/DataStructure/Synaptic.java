package NeuralNetwork.DataStructure;

import java.util.Arrays;
import java.util.Random;

import Exception.FailedToSetWeightsAndBiasException;

public class Synaptic
{
    /* Esta classe é responsável por uma única sinapse. Ela
     * enxerga as camadas à sua volta (de origem e destino)
     * e contém os pesos e baias necessários para gerar os re-
     * sultados para a camada de destino.
     *
     *    +---------+                    +---------+
     *    |         |   +------------+   |         |
     *    |  Origin |   |            |   | Destiny |
     *    |         |   |  Synaptic  |   |         |
     *    |  Layer  |   |            |   | Layer   |
     *    |         |   +------------+   |         |
     *    +---------+                    +---------+
     *
     * */


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

    // Este método é responsável por executar a sinapse propriamente
    // dita. Envolve calcular uma combinação linear para cada neurônio
    // da camada de destino, onde as saídas dos neurônios da camada
    // de origem formam o vetor e os pesos da sinapse formam os componentes
    // da combinação.
    public void makeSynapse()
    {
        for (int i = 0; i < destinyLayer.getNeurons().length; i++)
        {
            double linearCombination = 0;

            for (int j = 0; j < originLayer.getNeurons().length; j++)
            {
                linearCombination += originLayer.getNeurons()[j].getOutput() * weights[j][i];
            }

            linearCombination += 1 * destinyLayerBias[i];
            destinyLayer.getNeurons()[i].setLastInputReceived(linearCombination);
        }
    }

    // Este método imprime todas as saídas da camada de destino,
    // utilizando as últimas entradas recebidas.
    public void printDestinyLayerOutputs ()
    {
        System.out.println();
        for (int i = 0; i < destinyLayer.getNeurons().length; i++) System.out.printf("Neuronio %d: output %.3f\n", (i+1), destinyLayer.getOutputs()[i]);
        System.out.println();
    }

    // Este método ajusta os pesos a partir da correção de erros recebida
    // e retorna a correção de erros necessária para a sinapse anterior.
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

    // Este método retorna a correção de erros necessária para a sinapse anterior.
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

    public double[][] getWeights(boolean isInitial)
    {
        double[][] weightsCopy = new double[weights.length][weights[0].length];

        for (int i = 0; i < weights.length; i++)
        {
            if (isInitial) System.arraycopy(initialWeights[i], 0, weightsCopy[i], 0, initialWeights[i].length);
            else System.arraycopy(weights[i], 0, weightsCopy[i], 0, weights[i].length);
        }

        return weightsCopy;
    }

    public double[] getDestinyLayerBias(boolean isInitial)
    {
        double[] biasCopy = new double[destinyLayerBias.length];

        if (isInitial) System.arraycopy(initialDestinyLayerBias, 0, biasCopy, 0, initialDestinyLayerBias.length);
        else System.arraycopy(destinyLayerBias, 0, biasCopy, 0, destinyLayerBias.length);

        return biasCopy;
    }

    // Este método gera os pesos e bias aleatoriamente.
    public void setWeightsAndBiasRandomly()
    {
        try { if (initialWeightsAndBiasDefined) throw new FailedToSetWeightsAndBiasException(); }
        catch (FailedToSetWeightsAndBiasException e) { e.printStackTrace(); return; }

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

    // Este método reseta todos os pesos e baias da sinpase,
    // alterando para 0 todos eles.
    public void resetWeightsAndBias()
    {
        weights = new double[weights.length][weights[0].length];
        destinyLayerBias = new double[weights[0].length];
        initialWeights = null;
        initialWeightsAndBiasDefined = false;
    }

    // Este método imprime todos os pesos e bias da sinapse.
    public void printWeightsAndBias()
    {
        System.out.println();

        for (int i = 0; i < weights.length; i++)
        {
            System.out.print("Neuronio Origem " + (i + 1) + "\t");

            for (int j = 0; j < weights[0].length; j++)
            {
                System.out.printf("%.4f \t", weights[i][j]);
            }

            System.out.println();
        }

        System.out.print("Destiny Bias: \t\t");

        for (int i = 0 ; i < destinyLayerBias.length; i++)
        {
            System.out.printf("%.4f \t", destinyLayerBias[i]);
        }

        System.out.println("\n");
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
