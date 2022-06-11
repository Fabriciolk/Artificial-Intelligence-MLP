package NeuralNetwork.Learning.Results;

import NeuralNetwork.Data.Data;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTraining;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class ResultManager
{
    /*
    * Esta classe é responsável por gerenciar os resultados das épocas
    * e gerar os arquivos de saída adequados.
    * */

    private final LinkedList<EpochResult> trainingEpochResultsList = new LinkedList<>();
    private final LinkedList<EpochResult> validationEpochResultsList = new LinkedList<>();
    private final NeuralNetwork neuralNetwork;

    public ResultManager(NeuralNetwork neuralNetwork)
    {
        this.neuralNetwork = neuralNetwork;
    }

    // Este método adiciona o resultado da época (saída obtida e esperada)
    // gerado a partir de um dado de treinamento.
    public void addTrainingEpochResult(EpochResult epochResult)
    {
        trainingEpochResultsList.add(epochResult);
    }

    // Este método adiciona o resultado da época (saída obtida e esperada)
    // gerado a partir de um dado de validação.
    public void addValidationEpochResult(EpochResult epochResult)
    {
        validationEpochResultsList.add(epochResult);
    }

    public void exportNeuralNetworkParametersFile(String fileName, NeuralNetworkTraining neuralNetworkTraining)
    {
        FileWriter file = createFile(fileName);
        assert file != null;
        String[] neuronsByLayer = neuralNetwork.getNeuronsByLayerLabel().split("-");

        try
        {
            for (int i = 0; i < neuronsByLayer.length; i++)
            {
                file.append(String.format("Layer %d: %s neurons; Activation function: %s\n", (i + 1), neuronsByLayer[i], neuralNetwork.getLayerActivationFunction(i)));
            }
            file.append("\n");
            file.append(String.format("Number of Epochs: %d\n", trainingEpochResultsList.size()));
            file.append(String.format("Learning rate: %.2f\n", neuralNetworkTraining.getLearningRate()));

            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void exportWeightsAndBiasFile(String fileName, boolean isInitial)
    {
        int currentIndex = 0;
        double[][] currentSynapseWeights = neuralNetwork.getSynapseWeights(currentIndex, isInitial);
        double[] currentLayerBias = neuralNetwork.getSynapseDestinyLayerBias(currentIndex, isInitial);

        while (currentSynapseWeights != null)
        {
            FileWriter file = createFile(new StringBuilder(fileName).insert(fileName.lastIndexOf('.'), currentIndex + 1).toString());
            assert file != null;

            try {
                String columnNames = "destiny1";
                for (int i = 1; i < currentSynapseWeights[0].length; i++) columnNames = columnNames.concat(", destiny" + (i + 1));
                file.append(columnNames.concat("\n"));

                String biasLine = String.valueOf(currentLayerBias[0]);
                for (int i = 1; i < currentLayerBias.length; i++) biasLine = biasLine.concat(", " + currentLayerBias[i]);
                file.append(biasLine.concat("\n"));

                for (double[] currentSynapseWeight : currentSynapseWeights)
                {
                    String weightsLine = String.valueOf(currentSynapseWeight[0]);

                    for (int j = 1; j < currentSynapseWeights[0].length; j++)
                    {
                        weightsLine = weightsLine.concat(", " + currentSynapseWeight[j]);
                    }

                    file.append(weightsLine.concat("\n"));
                }

                file.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            currentIndex++;
            currentSynapseWeights = neuralNetwork.getSynapseWeights(currentIndex, isInitial);
            currentLayerBias = neuralNetwork.getSynapseDestinyLayerBias(currentIndex, isInitial);
        }
    }

    public void exportErrorsFile(String fileName)
    {
        FileWriter file = createFile(fileName);

        try {
            assert file != null;
            file.append("testError, validateError\n");

            for (int i = 0; i < trainingEpochResultsList.size(); i++)
            {
                file.append(String.valueOf(trainingEpochResultsList.get(i).getErrorsMean())).append(", ").append(String.valueOf(validationEpochResultsList.get(i).getErrorsMean())).append("\n");
            }

            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void exportOutputsFile(String fileName, Dataset dataset)
    {
        FileWriter file = createFile(fileName);
        dataset.resetTrainingDataRead();

        try {
            assert file != null;
            int currentData = 1;
            double[] output;

            while (!dataset.gotAllTrainingData())
            {
                file.append("Data ").append(String.valueOf(currentData)).append("\n");
                Data data = dataset.getNextTrainingData();

                // Escreve os valores do dado no arquivo
                for (int i = 0; i < data.getData().length; i++) file.append(String.valueOf(data.getData()[i])).append("\t");
                file.append("\n");
                file.append("Output\n");

                // Utiliza a rede neural para obter os outputs
                neuralNetwork.getInputLayer().setInputs(data.getData());
                neuralNetwork.makeAllSynapse();
                output = neuralNetwork.getOutputLayer().getOutputs();

                // Escreve os valores do output noa arquivo
                for (double v : output) file.append(String.format("%.5f", v)).append("\t");
                file.append("\n\n");

                currentData++;
            }

            dataset.resetTrainingDataRead();
            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    private FileWriter createFile(String fileName)
    {
        try {
            return new FileWriter("results" + File.separator + fileName);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
