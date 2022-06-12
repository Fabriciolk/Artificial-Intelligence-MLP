import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersDataset;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTrainer;

import java.io.File;

public class Main
{
    public static String fileTrainPath = "dataset" + File.separator + "caracteres-limpo.csv";
    public static String fileTestPath = "dataset" + File.separator + "caracteres-ruido.csv";

    public static NeuralNetwork neuralNetwork;
    public static NeuralNetworkTrainer neuralNetworkTrainer;
    public static Dataset datasetToTrain;
    public static Dataset datasetToTest;

    public static void main(String[] args)
    {
        neuralNetwork = new NeuralNetwork(63);
        neuralNetwork.addLayer(new Layer(225, new SigmoidFunction()));
        neuralNetwork.addLayer(new Layer(7, new SigmoidFunction()));

        datasetToTrain = new CharactersDataset(fileTrainPath, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
        datasetToTest = new CharactersDataset(fileTestPath, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);

        neuralNetworkTrainer = new NeuralNetworkTrainer(neuralNetwork, datasetToTrain);
        neuralNetworkTrainer.setLearningRate(0.5);
        neuralNetworkTrainer.start(1700);

        neuralNetworkTrainer.getResultManager().exportNeuralNetworkParametersFile("MLPParameters.txt", neuralNetworkTrainer);
        neuralNetworkTrainer.getResultManager().exportWeightsAndBiasFile("initialWeights.csv", true);
        neuralNetworkTrainer.getResultManager().exportWeightsAndBiasFile("finalWeights.csv", false);
        neuralNetworkTrainer.getResultManager().exportErrorsFile("errorsByEpoch.csv");
        neuralNetworkTrainer.getResultManager().exportOutputsFile("outputs.txt", datasetToTest);
        neuralNetworkTrainer.getResultManager().exportConfusionMatrixFile("confusionMatrix.csv", datasetToTest);

        System.out.printf("Got %d/%d right answers\n", neuralNetwork.countRightAnswers(datasetToTest, 0.5), datasetToTest.getNumberOfDataForTraining());
    }
}
