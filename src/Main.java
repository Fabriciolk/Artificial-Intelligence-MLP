import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersDataset;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTraining;

import java.io.File;

public class Main
{
    public static String fileTrainPath = "dataset" + File.separator + "caracteres-limpo.csv";
    public static String fileFilePath = "dataset" + File.separator + "caracteres-ruido.csv";

    public static NeuralNetwork neuralNetwork;
    public static NeuralNetworkTraining neuralNetworkTraining;
    public static Dataset datasetToTrain;
    public static Dataset datasetToTest;

    public static void main(String[] args)
    {
        neuralNetwork = new NeuralNetwork(63);
        neuralNetwork.addLayer(new Layer(25, new SigmoidFunction()));
        neuralNetwork.addLayer(new Layer(7, new SigmoidFunction()));

        datasetToTrain = new CharactersDataset(fileTrainPath, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
        datasetToTrain.shuffleAll();

        datasetToTest = new CharactersDataset(fileFilePath, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
        datasetToTest.shuffleAll();

        neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, datasetToTrain);
        neuralNetworkTraining.setLearningRate(0.5);
        neuralNetworkTraining.start(1700);

        neuralNetworkTraining.getResultManager().exportNeuralNetworkParametersFile("MLPParameters.txt", neuralNetworkTraining);
        neuralNetworkTraining.getResultManager().exportWeightsAndBiasFile("initialWeights.csv", true);
        neuralNetworkTraining.getResultManager().exportWeightsAndBiasFile("finalWeights.csv", false);
        neuralNetworkTraining.getResultManager().exportErrorsFile("errorsByEpoch.csv");
        neuralNetworkTraining.getResultManager().exportOutputsFile("outputs.txt", datasetToTest);

        System.out.printf("Got %d/%d right answers", neuralNetwork.countRightAnswers(datasetToTest, 0.5), datasetToTest.getDataLength());
    }
}
