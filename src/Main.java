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

    static NeuralNetwork neuralNetwork;
    static NeuralNetworkTraining neuralNetworkTraining;
    static TrainingDataset charactersData;
    static TrainingDataset datasetToTest;

    public static void main(String[] args)
    {
        double[] learningRates = new double[] {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        int[][] acertos = new int[100][learningRates.length];

        for (int i = 0; i < acertos.length; i++)
        {
            for (int j = 0; j < learningRates.length; j++)
            {
                neuralNetwork = new NeuralNetwork(63);
                neuralNetwork.addLayer(new Layer(i + 1, new ArcTangentFunction()));
                neuralNetwork.addLayer(new Layer(7, new ArcTangentFunction()));

                charactersData = new CharactersData(pathFileTrain, 0.3);
                charactersData.shuffleAll();

                neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, charactersData, false);
                neuralNetworkTraining.setLearningRate(learningRates[j]);
                neuralNetworkTraining.start(2000);

                datasetToTest = new CharactersData(pathFileTest, 0.0);
                datasetToTest.shuffleAll();

                acertos[i][j] = neuralNetwork.countRightAnswers(datasetToTest, 0.5);
            }

            System.out.printf("Acertos para rede neural %s: ", neuralNetwork.getNeuronsByLayerLabel());

            int bestLearningRateIndex = 0;
            for (int j = 0; j < acertos[0].length; j++)
            {
                if (acertos[i][j] > acertos[i][bestLearningRateIndex]) bestLearningRateIndex = j;
                System.out.printf("[%.1f] %d/%d\t", learningRates[j], acertos[i][j], datasetToTest.getDataLength());
            }

            System.out.printf("Melhor taxa: %.1f\n", learningRates[bestLearningRateIndex]);
        }
    }
}
