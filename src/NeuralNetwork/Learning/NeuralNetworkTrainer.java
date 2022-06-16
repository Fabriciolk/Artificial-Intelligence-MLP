package NeuralNetwork.Learning;

import NeuralNetwork.Data.Data;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.DataStructure.Neuron;
import NeuralNetwork.Learning.Results.EpochResult;
import NeuralNetwork.Learning.Results.ResultManager;

import Exception.InvalidLearningRateException;

public class NeuralNetworkTrainer
{
    /*
    *  Esta classe é responsável por treinar uma rede neural.
    * */

    private final NeuralNetwork neuralNetwork;
    private ResultManager resultManager;
    private final Dataset dataset;
    private boolean stopOnNextEpoch = false;
    private double learningRate = 0.01;

    public NeuralNetworkTrainer(NeuralNetwork neuralNetwork, Dataset dataset)
    {
        this.neuralNetwork = neuralNetwork;
        this.dataset = dataset;
    }

    /*****************************\
     **                         **
     **      Public Methods     **
     **                         **
    \*****************************/

    // Este método inicia o treinamento da rede neural. O treinamento
    // consiste em obter todos os dados do dataset de treinamento e
    // para cada um, realizar o feedforward e o backpropagation. Para
    // análise de erro da rede, antes de utilizar o dataset de treina-
    // mento, utiliza também um outro de validação. Além disso, o método
    // exporta um arquivo CSV para cada época, com erro de teste e validação.
    public void start(int numberOfEpoch)
    {
        resultManager = new ResultManager(neuralNetwork);
        neuralNetwork.setAllWeightsAndBiasRandomly();

        for (int i = 0; i < numberOfEpoch; i++)
        {
            dataset.resetTrainingDataRead();
            dataset.resetValidationDataRead();
            dataset.shuffleAll();

            EpochResult epochToTrain = new EpochResult();
            EpochResult epochToValidate = new EpochResult();

            while (!dataset.gotAllValidationData())
            {
                Data dataToValidate = dataset.getNextValidationData();
                setDataOnInputLayer(dataToValidate.getData());
                runFeedforward();
                epochToValidate.registerOutputs(neuralNetwork.getOutputLayer().getOutputs(), dataToValidate.getClassData());
            }

            while (!dataset.gotAllTrainingData())
            {
                Data dataToTrain = dataset.getNextTrainingData();
                setDataOnInputLayer(dataToTrain.getData());
                runFeedforward();
                epochToTrain.registerOutputs(neuralNetwork.getOutputLayer().getOutputs(), dataToTrain.getClassData());
                runBackpropagation(neuralNetwork.getOutputLayer().getOutputs(), dataToTrain.getClassData());
            }

            resultManager.addTrainingEpochResult(epochToTrain);
            resultManager.addValidationEpochResult(epochToValidate);

            if (stopOnNextEpoch) break;
        }
    }

    // Este método retorna a rede neural.
    public NeuralNetwork getNeuralNetwork()
    {
        return neuralNetwork;
    }

    // Este método retorna o gerenciador de resultados.
    public ResultManager getResultManager()
    {
        return resultManager;
    }

    // Este método define uma parada de treinamento para
    // a próxima época. Utilizado caso o treinamento seja
    // feito numa thread separada da principal.
    public void stop()
    {
        stopOnNextEpoch = true;
    }

    // Este método altera a taxa de aprendizagem.
    public void setLearningRate(double rate)
    {
        if (rate <= 0 || rate > 1) new InvalidLearningRateException(rate).printStackTrace();
        learningRate = rate;
    }

    // Este método retorna a taxa de aprendizado
    public double getLearningRate()
    {
        return learningRate;
    }

    /******************************\
     **                          **
     **      Private Methods     **
     **                          **
    \******************************/

    // Este método altera para 0 todos os pesos e bias
    // de todas as sinapses.
    private void resetAllWeights()
    {
        neuralNetwork.resetAllWeightsAndBias();
    }

    // Este método executa o feedforward.
    private void runFeedforward()
    {
        neuralNetwork.makeAllSynapse();
    }

    // Este método inicia o backpropagation.
    private void runBackpropagation(double[] obtainedResults, double[] expectedResults)
    {
        neuralNetwork.makeBackpropagation(learningRate, getCorrectionErrorTerms(obtainedResults, expectedResults));
    }

    // Este método obtém os termos de correção de erros da camada de saída.
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

    // Este método insere um dado na camada de entrada.
    private void setDataOnInputLayer(double[] data)
    {
        neuralNetwork.getInputLayer().setInputs(data);
    }
}