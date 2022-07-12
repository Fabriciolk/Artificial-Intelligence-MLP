import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersDataset;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTrainer;
import NeuralNetwork.Learning.Results.Avaliation;
import NeuralNetwork.Learning.Results.ConfusionMatrix;

import java.io.File;
import java.io.IOException;

public class Main
{
    public static String fileTrainPath = "dataset" + File.separator + "caracteres-limpo.csv";
    public static String fileTestPath = "dataset" + File.separator + "caracteres_ruido20.csv";

    public static NeuralNetwork neuralNetwork;
    public static NeuralNetworkTrainer neuralNetworkTrainer;
    public static Dataset datasetToTrain;
    public static Dataset datasetToTest;

    public static void main(String[] args)
    {
        neuralNetwork = new NeuralNetwork(63);
        neuralNetwork.addLayer(new Layer(25, new SigmoidFunction()));
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

        System.out.printf("\nGot %d/%d right answers\n\n", neuralNetwork.countRightAnswers(datasetToTest), datasetToTest.getNumberOfDataForTraining());

        Avaliation avaliation = new Avaliation(new ConfusionMatrix(neuralNetworkTrainer.getResultManager().getConfusionMatrix()));
        for (int i = 0; i < neuralNetwork.getOutputLayer().getNeurons().length; i++)
        {
            System.out.println("<<< Avaliation for class " + datasetToTrain.classNameByIndex(i) + " >>>");
            avaliation.printAllMetrics(i);
            System.out.println("-------------\\\\--------------");
        }

        System.out.println("");
        try {
            Process RProcess = Runtime.getRuntime().exec("R" + File.separator + "R-4.1.1" + File.separator + "bin" + File.separator + "Rscript.exe imageGenerator.R");
            System.out.println("Executing R script...");
            RProcess.waitFor();
            System.out.println("Finished R script execution. Check 'images' folder.");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
