package NeuralNetwork.DataStructure;

import java.util.Arrays;
import java.util.Random;

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

    // Este método gera os pesos e bias aleatoriamente.
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

    public void setWeightsAndBias (double[][] weights, double[] bias)
    {
        for (int i = 0; i < weights.length; i++)
        {
            for (int j = 0; j < weights[0].length; j++)
            {
                this.weights[i][j] = weights[i][j];
            }
        }

        for (int i = 0; i < bias.length; i++) this.destinyLayerBias[i] = bias[i];
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
