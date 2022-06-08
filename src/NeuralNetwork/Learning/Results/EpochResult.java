package NeuralNetwork.Learning.Results;

import Utilities.Statistic;

import java.util.LinkedList;

public class EpochResult
{
    /*
    * Esta classe é responsável por todas as saídas geradas a partir
    * do fornecimento de um dado como entrada. Inclui portanto os resultados
    * obtidos na camada de saída e os erros cometidos pela rede neural
    * a partir desses valores.
    * */

    private LinkedList<double[]> outputLayerResults = new LinkedList<>();
    private LinkedList<double[]> outputLayerErrors = new LinkedList<>();

    public void registerOutputs(double[] obtainedResults, double[] expectedResults)
    {
        if (obtainedResults.length != expectedResults.length) return;
        double[] error = new double[obtainedResults.length];

        for (int i = 0; i < error.length; i++) error[i] = obtainedResults[i] - expectedResults[i];

        outputLayerResults.add(obtainedResults);
        outputLayerErrors.add(error);
    }

    // Este método retorna a média quadrática dos erros da saída da época.
    public double getErrorsMean()
    {
        double[] epochErrorSums = new double[outputLayerErrors.size()];

        for (int i = 0; i < epochErrorSums.length; i++)
        {
            for (double[] errors : outputLayerErrors) epochErrorSums[i] = getOutputErrorsSum(errors);
        }

        return Statistic.getMean(epochErrorSums);
    }

    // Este método retorna a soma quadrática dos erros da saída da época.
    private double getOutputErrorsSum(double[] outputError)
    {
        double outputLayerErrorsSum = 0;

        for (int i = 0; i < outputError.length; i++) outputLayerErrorsSum += outputError[i] * outputError[i];

        return outputLayerErrorsSum;
    }

    public LinkedList<double[]> getOutputLayerResults() {
        return outputLayerResults;
    }

    public LinkedList<double[]> getOutputLayerErrors() {
        return outputLayerErrors;
    }
}
