import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersDataset;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.Data.TrainingDataset;
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
    public static TrainingDataset datasetToTrain;
    public static Dataset datasetToTest;

    public static void main(String[] args)
    {
        neuralNetwork = new NeuralNetwork(63);
        neuralNetwork.addLayer(new Layer(25, new SigmoidFunction()));
        neuralNetwork.addLayer(new Layer(7, new SigmoidFunction()));

        datasetToTrain = new CharactersDataset(fileTrainPath, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
        datasetToTest = new CharactersDataset(fileTestPath, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);

        neuralNetworkTrainer = new NeuralNetworkTrainer(neuralNetwork, datasetToTrain);
        neuralNetworkTrainer.setLearningRate(0.5);
        neuralNetworkTrainer.start(1700);

        neuralNetworkTrainer.getResultTrainingManager().exportNeuralNetworkParametersFile("MLPParameters.txt", neuralNetworkTrainer);
        neuralNetworkTrainer.getResultTrainingManager().exportWeightsAndBiasFile("initialWeights.csv", true);
        neuralNetworkTrainer.getResultTrainingManager().exportWeightsAndBiasFile("finalWeights.csv", false);
        neuralNetworkTrainer.getResultTrainingManager().exportErrorsFile("errorsByEpoch.csv", neuralNetworkTrainer.getResultValidationManager());
        neuralNetworkTrainer.getResultTrainingManager().exportOutputsFile("outputs.txt", datasetToTest);
        neuralNetworkTrainer.getResultTrainingManager().exportConfusionMatrixFile("confusionMatrix.csv", datasetToTest);

        System.out.printf("Got %d/%d right answers\n\n", neuralNetwork.countRightAnswers(datasetToTest), datasetToTest.getNumberOfRows());

        Avaliation avaliation = new Avaliation(new ConfusionMatrix(neuralNetworkTrainer.getResultTrainingManager().getConfusionMatrix()));

        for (int i = 0; i < neuralNetwork.getOutputLayer().getNeurons().length; i++)
        {
            System.out.println("<<< Avaliation for class " + datasetToTrain.getDataClasses()[i] + " >>>");
            avaliation.printAllMetrics(i);
            System.out.println("-------------\\\\--------------");
        }

        try {
            Process RProcess = Runtime.getRuntime().exec(args[0] + File.separator + "Rscript.exe imageGenerator.R");
            System.out.println("Executing R script...");
            RProcess.waitFor();
            System.out.println("Finished R script execution. Check 'images' folder.");
        } catch (IOException | InterruptedException e) {
            System.out.println("R.exe was not found. Cannot generate images.");
            e.printStackTrace();
        }
    }
}
