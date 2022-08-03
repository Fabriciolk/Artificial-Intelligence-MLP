package NeuralNetwork.Learning.Results;

import NeuralNetwork.Data.Data;
import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTrainer;
import Utilities.Statistic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class ResultManager
{
    /*
    * Esta classe é responsável por gerenciar os resultados das épocas
    * e gerar os arquivos de saída adequados.
    * */

    private final LinkedList<EpochResult> results = new LinkedList<>();
    private final NeuralNetwork neuralNetwork;
    private int[][] confusionMatrix;

    public ResultManager(NeuralNetwork neuralNetwork)
    {
        this.neuralNetwork = neuralNetwork;
    }

    public void addEpochResult(EpochResult epochResult)
    {
        results.add(epochResult);
    }

    // Este método gera um arquivo contendo os parâmetros da rede neural.
    public void exportNeuralNetworkParametersFile(String fileName, NeuralNetworkTrainer neuralNetworkTrainer)
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
            file.append(String.format("Number of Epochs: %d\n", results.size()));
            file.append(String.format("Learning rate: %.2f\n", neuralNetworkTrainer.getLearningRate()));

            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Este método gera um arquivo contendo os pesos e bias
    // da rede neural, tendo como opção gerar os iniciais
    // ou não.
    public void exportWeightsAndBiasFile(String fileName, boolean isInitial)
    {
        int currentIndex = 0;
        double[][] currentSynapseWeights = neuralNetwork.getSynapseWeights(currentIndex, isInitial);
        double[] currentLayerBias = neuralNetwork.getSynapseDestinyLayerBias(currentIndex, isInitial);

        while (currentSynapseWeights != null)
        {
            FileWriter file = createFile("table" + File.separator + new StringBuilder(fileName).insert(fileName.lastIndexOf('.'), String.format("_%d", currentIndex + 1)));
            assert file != null;

            try {
                String columnNames = "src, Destiny_1";
                for (int i = 1; i < currentSynapseWeights[0].length; i++) columnNames = columnNames.concat(", Destiny_" + (i + 1));
                file.append(columnNames.concat("\n"));

                String biasLine = "Bias, ".concat(String.valueOf(currentLayerBias[0]));
                for (int i = 1; i < currentLayerBias.length; i++) biasLine = biasLine.concat(", " + currentLayerBias[i]);
                file.append(biasLine.concat("\n"));

                for (int i = 0; i < currentSynapseWeights.length; i++)
                {
                    String weightsLine = "Origin_".concat(String.valueOf(i+1)).concat(", ").concat(String.valueOf(currentSynapseWeights[i][0]));

                    for (int j = 1; j < currentSynapseWeights[0].length; j++)
                    {
                        weightsLine = weightsLine.concat(", " + currentSynapseWeights[i][j]);
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

    // Este método gera um arquivo contendo os erros cometidos
    // pela rede neural a cada época.
    public void exportErrorsFile(String fileName, ResultManager validation)
    {
        FileWriter file = createFile(fileName);

        try {
            assert file != null;
            file.append("testError, validateError\n");

            for (int i = 0; i < results.size(); i++)
            {
                String validationResult;
                if (validation != null) validationResult = String.valueOf(validation.getResults().get(i).getErrorsMean());
                else validationResult = "";
                file.append(String.valueOf(results.get(i).getErrorsMean())).append(", ").append(validationResult).append("\n");
            }

            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Este método gera um arquivo contendo os dados do dataset de
    // teste e a respectiva saída da rede neural para cada um deles.
    public void exportOutputsFile(String fileName, Dataset dataset)
    {
        FileWriter file = createFile(fileName);

        try {
            assert file != null;
            int currentData = 1;
            double[] output;

            for (Iterator<Data> it = dataset.getIterator(); it.hasNext(); )
            {
                file.append("Data ").append(String.valueOf(currentData)).append("\n");
                Data data = it.next();

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

            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void exportConfusionMatrixFile(String fileName, Dataset dataset)
    {
        FileWriter file = createFile("table" + File.separator +fileName);

        int[][] confusionMatrix = new int[dataset.getDataClasses().length][dataset.getDataClasses().length];
        this.confusionMatrix = confusionMatrix;

        // fill confusion matrix based on neural network answers.
        for (Iterator<Data> it = dataset.getIterator(); it.hasNext(); )
        {
            Data data = it.next();
            neuralNetwork.getInputLayer().setInputs(data.getData());
            neuralNetwork.makeAllSynapse();
            double[] output = neuralNetwork.getOutputLayer().getOutputs();
            confusionMatrix[Statistic.getIndexMax(data.getClassData())][Statistic.getIndexMax(output)]++;
        }

        String[] classes = {"A", "B", "C", "D", "E", "J", "K"};

        try {
            assert file != null;
            String columnNamesLine = "";
            for (String aClass : classes) columnNamesLine = columnNamesLine.concat(", Got_").concat(aClass);
            file.append(columnNamesLine.concat("\n"));

            for (int i = 0; i < confusionMatrix.length; i++)
            {
                String line = "Should_Be_".concat(classes[i]).concat(", ").concat(String.valueOf(confusionMatrix[i][0]));

                for (int j = 1; j < confusionMatrix[0].length; j++)
                {
                    line = line.concat(", " + confusionMatrix[i][j]);
                }

                file.append(line.concat("\n"));
            }

            file.close();
        }
        catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    // Este método cria um arquivo com determinado nome.
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

    public int[][] getConfusionMatrix() {
        return confusionMatrix;
    }

    public LinkedList<EpochResult> getResults() {
        return results;
    }
}
