import Function.ArcTan.ArcTangentFunction;
import NeuralNetwork.Data.CharactersData;
import NeuralNetwork.Data.TrainingDataset;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTraining;

import java.io.File;

public class Main
{
    private static String pathFileTrain = "dataset" + File.separator + "caracteres-ruido e limpo-fabricio.csv";
    private static String pathFileTest = "dataset" + File.separator + "caracteres_ruido20.csv";

    static TrainingDataset charactersData;
    static TrainingDataset datasetToTest;

    public static void main(String[] args)
    {
        int[] acertos = new int[100];

        for (int i = 0; i < acertos.length; i++)
        {
            NeuralNetwork neuralNetwork = new NeuralNetwork(63);
            neuralNetwork.addLayer(new Layer(i + 1, new ArcTangentFunction()));
            neuralNetwork.addLayer(new Layer(7, new ArcTangentFunction()));

            charactersData = new CharactersData(pathFileTrain, 0.3);
            charactersData.shuffleAll();

            NeuralNetworkTraining neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, charactersData);
            neuralNetworkTraining.setLearningRate(5);
            neuralNetworkTraining.start(2000);

            datasetToTest = new CharactersData(pathFileTest, 0.0);
            datasetToTest.shuffleAll();

            acertos[i] = neuralNetwork.countRightAnswers(datasetToTest, 0.5);
            System.out.printf("Acertos para rede neural %s: %d/%d\n", neuralNetwork.getNeuronsByLayerLabel(), acertos[i], datasetToTest.getDataLength());
        }
    }
}
