package NeuralNetwork.Learning;

import NeuralNetwork.Data.Data;
import NeuralNetwork.Data.TrainingDataSet;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.DataStructure.Neuron;
import NeuralNetwork.Learning.Results.EpochResult;
import NeuralNetwork.Learning.Results.ResultManager;

import java.sql.SQLOutput;

public class NeuralNetworkTraining
{
    private final NeuralNetwork neuralNetwork;
    private ResultManager resultManager;
    private TrainingDataSet trainingDataSet;
    private boolean stopOnNextEpoch = false;
    private double learningRate = 0.01;

    public NeuralNetworkTraining(NeuralNetwork neuralNetwork, TrainingDataSet trainingDataSet)
    {
        this.neuralNetwork = neuralNetwork;
        this.trainingDataSet = trainingDataSet;
    }

    /*****************************\
     **                         **
     **      Public Methods     **
     **                         **
    \*****************************/

    public void start(int numberOfEpoch)
    {
        System.out.println("Iniciando treinamento da Rede Neural Artificial...");
        resultManager = new ResultManager();
        System.out.println("Definindo pesos e bias iniciais aleatoriamente...");
        boolean successOnSetWeightsRandomly = tryToSetAllWeightsAndBiasRandomly();

        if (successOnSetWeightsRandomly)
        {
            System.out.println("Definição aleatória de pesos e bias com sucesso!");
            System.out.println("Imprimindo pesoas e bias:");
            neuralNetwork.printLayerWeights();
        }
        else
        {
            System.out.println("Nao foi possível definir pesos bias. A ação já foi feita.");
        }

        System.out.println("Treinando...");
        for (int i = 0; i < numberOfEpoch; i++)
        {
            EpochResult epochResult = new EpochResult();

            while (!trainingDataSet.gotAllData())
            {
                Data dataToTrain = trainingDataSet.getNextData();
                setDataOnInputLayer(dataToTrain.getData());
                runFeedforward();
                epochResult.registerOutputs(neuralNetwork.getOutputLayer().getOutputs(), dataToTrain.getExceptedResult());
                runBackpropagation(neuralNetwork.getOutputLayer().getOutputs(), dataToTrain.getExceptedResult());
            }

            trainingDataSet.shuffleAllData();
            resultManager.addEpochResult(epochResult);
            System.out.println("Erro médio da época " + (i + 1) + ": " + epochResult.getErrorsMean());
            if (stopOnNextEpoch) break;
        }

        System.out.println("\nPesos e bias finais:\n");
        neuralNetwork.printLayerWeights();
    }

    public NeuralNetwork getNeuralNetwork()
    {
        return neuralNetwork;
    }

    public ResultManager getResultManager()
    {
        return resultManager;
    }

    public void stop()
    {
        stopOnNextEpoch = true;
    }

    public void setLearningRate(double rate)
    {
        if (rate <= 0 || rate > 1) return;
        learningRate = rate;
    }

    /******************************\
     **                          **
     **      Private Methods     **
     **                          **
    \******************************/

    private boolean tryToSetAllWeightsAndBiasRandomly()
    {
        return neuralNetwork.setAllWeightsAndBiasRandomly();
    }

    private void resetAllWeights()
    {
        neuralNetwork.resetAllWeights();
    }

    private void runFeedforward()
    {
        neuralNetwork.makeAllSynapse();
    }

    private void runBackpropagation(double[] obtainedResults, double[] expectedResults)
    {
        neuralNetwork.makeBackpropagation(learningRate, getCorrectionErrorTerms(obtainedResults, expectedResults));
    }

    private double[] getCorrectionErrorTerms(double[] obtainedResults, double[] expectedResults)
    {
        if (obtainedResults.length != expectedResults.length) return null;
        if (obtainedResults.length != neuralNetwork.getOutputLayer().getNeurons().length) return null;
        double[] errorTerms = new double[obtainedResults.length];

        for (int i = 0; i < obtainedResults.length; i++)
        {
            Neuron neuronToAnalyse = neuralNetwork.getOutputLayer().getNeurons()[i];
            errorTerms[i] = (expectedResults[i] - obtainedResults[i]) * neuronToAnalyse.getImageFromDerivativeFunction(neuronToAnalyse.getLastInputReceived());
        }

        return errorTerms;
    }

    private void setDataOnInputLayer(double[] data)
    {
        neuralNetwork.getInputLayer().setInputs(data);
    }
}
