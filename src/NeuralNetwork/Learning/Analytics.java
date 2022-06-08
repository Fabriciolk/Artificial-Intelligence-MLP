package NeuralNetwork.Learning;

import Function.ArcTan.ArcTangentFunction;
import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersDataset;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;

import java.io.File;
import java.util.LinkedList;

public class Analytics
{
    public static void runAllCases (String pathFileTrain, String pathFileTest) {
        int[] neuronAmountInterval = {20, 30};
        int[] rightAnswers = new int[100];

        for (int i = neuronAmountInterval[0]; i <= neuronAmountInterval[1]; i++) {
            for (int epochs = 500; epochs < 2000; epochs += 100) {
                for (int k = 0; k < rightAnswers.length; k++) {
                    NeuralNetwork neuralNetwork = new NeuralNetwork(63);
                    neuralNetwork.addLayer(new Layer(i, new SigmoidFunction()));
                    neuralNetwork.addLayer(new Layer(7, new SigmoidFunction()));

                    Dataset datasetToTrain = new CharactersDataset(pathFileTrain, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
                    datasetToTrain.shuffleAll();

                    Dataset datasetToTest = new CharactersDataset(pathFileTest, 0.0, neuralNetwork.getInputLayer().getNeurons().length, neuralNetwork.getOutputLayer().getNeurons().length);
                    datasetToTest.shuffleAll();

                    NeuralNetworkTraining neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, datasetToTrain);
                    neuralNetworkTraining.setLearningRate(0.5);
                    neuralNetworkTraining.start(epochs);
                    rightAnswers[k] = neuralNetwork.countRightAnswers(datasetToTest, 0.5);

                }

                int soma = 0;

                for (int k = 0; k < rightAnswers.length; k++) soma += rightAnswers[k];

                //System.out.println("Media de rightAnswers para " + epochs + " epocas com estrutura " + neuralNetwork.getNeuronsByLayerLabel() + ": " + ((double) (soma)) / rightAnswers.length + "/" + datasetToTrain.getDataLength());
            }
        }
    }

    public static void runNumericExample ()
    {
        NeuralNetwork neuralNetwork = new NeuralNetwork(2);
        neuralNetwork.addLayer(new Layer(3, new SigmoidFunction()));
        neuralNetwork.addLayer(new Layer(2, new SigmoidFunction()));

        Dataset dataset = new CharactersDataset("dataset" + File.separator + "teste_de_mesa.csv", 0.0, 2, 2);

        NeuralNetworkTraining neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, dataset);
        neuralNetworkTraining.setLearningRate(0.5);

        LinkedList<double[][]> weightsList = new LinkedList<>();
        LinkedList<double[]> biasList = new LinkedList<>();

        double[][] weightsV = {
                {0.1, 0.1, -0.1},
                {-0.1, 0.1, -0.1}};

        double[] biasV = {-0.1, -0.1, 0.1};

        double[][] weightsW = {
                {0.1, -0.1},
                {0.0, 0.1},
                {0.1, -0.1}};

        double[] biasW = {-0.1, 0.1};

        weightsList.add(weightsV);
        weightsList.add(weightsW);
        biasList.add(biasV);
        biasList.add(biasW);

        neuralNetwork.setWeightsAndBias(weightsList, biasList);
        neuralNetworkTraining.start(1);
    }

    public static void runRates()
    {
        String pathFileTrain = "dataset" + File.separator + "caracteres-ruido e limpo-fabricio.csv";
        String pathFileTest = "dataset" + File.separator + "caracteres_ruido20.csv";

        NeuralNetwork neuralNetwork;
        NeuralNetworkTraining neuralNetworkTraining;
        Dataset charactersData;
        Dataset datasetToTest;

        int[] neuronAmountInterval = {1, 100};
        double[] learningRatesInterval = {0.1, 1.0, 0.05};
        String[][] rightAnswers = new String[neuronAmountInterval[1] - neuronAmountInterval[0] + 1][(int)((learningRatesInterval[1] - learningRatesInterval[0]) / learningRatesInterval[2])];

        for (int neuronAmount = neuronAmountInterval[0]; neuronAmount <= neuronAmountInterval[1]; neuronAmount++)
        {
            for (double learningRate = learningRatesInterval[0]; learningRate <= learningRatesInterval[1]; learningRate += learningRatesInterval[2])
            {
                int rightAnswerTotalAmount = 0;
                int howManyTimesToRun = 100;

                datasetToTest = new CharactersDataset(pathFileTest, 0.0, 63, 7);
                datasetToTest.shuffleAll();

                for (int times = 0; times < howManyTimesToRun; times++)
                {
                    neuralNetwork = new NeuralNetwork(63);
                    neuralNetwork.addLayer(new Layer(neuronAmount, new ArcTangentFunction()));
                    neuralNetwork.addLayer(new Layer(7, new ArcTangentFunction()));

                    charactersData = new CharactersDataset(pathFileTrain, 0.3, 63, 7);
                    charactersData.shuffleAll();

                    neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, charactersData, false);
                    neuralNetworkTraining.setLearningRate(learningRate);
                    neuralNetworkTraining.start(2000);

                    rightAnswerTotalAmount += neuralNetwork.countRightAnswers(datasetToTest, 0.5);
                }

                datasetToTest.resetDataRead();
                rightAnswers[neuronAmount][(int)((learningRate - learningRatesInterval[0])/learningRatesInterval[2])] = String.valueOf(((double)rightAnswerTotalAmount)/howManyTimesToRun).concat("/").concat(String.valueOf(datasetToTest.getDataLength()));
            }
        }

        for (int i = 0; i < rightAnswers.length; i++)
        {
            for (int j = 0; j < rightAnswers[0].length; j++)
            {
                System.out.printf("%s\t", rightAnswers[i][j]);
            }
            System.out.println();
        }
    }
}
