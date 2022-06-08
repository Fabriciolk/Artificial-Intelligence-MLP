import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersData;
import NeuralNetwork.Data.TrainingDataset;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTraining;

import java.io.File;

public class Main
{
    private static String fileTrainPath = "dataset" + File.separator + "caracteres-limpo.csv";
    private static String fileFilePath = "dataset" + File.separator + "caracteres-ruido.csv";

    public static NeuralNetwork neuralNetwork;
    public static NeuralNetworkTraining neuralNetworkTraining;
    public static TrainingDataset datasetToTrain;
    public static TrainingDataset datasetToTest;

    public static void main(String[] args)
    {
        neuralNetwork = new NeuralNetwork(63);
        neuralNetwork.addLayer(new Layer(25, new SigmoidFunction()));
        neuralNetwork.addLayer(new Layer(7, new SigmoidFunction()));

        datasetToTrain = new CharactersData(fileTrainPath, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
        datasetToTrain.shuffleAll();

        datasetToTest = new CharactersData(fileFilePath, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
        datasetToTest.shuffleAll();

        neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, datasetToTrain);
        neuralNetworkTraining.setLearningRate(0.5);
        neuralNetworkTraining.start(1700);

        System.out.printf("Got %d/%d right answers", neuralNetwork.countRightAnswers(datasetToTest, 0.5), datasetToTest.getDataLength());
    }
}
