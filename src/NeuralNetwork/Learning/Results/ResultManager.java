package NeuralNetwork.Learning.Results;

import NeuralNetwork.DataStructure.NeuralNetwork;

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

    public void exportNeuralNetworkParametersFile(String fileName)
    {
        FileWriter file = createFile(fileName);
    }

    public void exportInitialWeightsFile(String fileName)
    {
        FileWriter file = createFile(fileName);
    }

    public void exportFinalWeightsFile(String fileName)
    {
        int currentIndex = 0;
        double[][] currentSynapseWeights = neuralNetwork.getSynapseWeights(currentIndex);
        double[] currentLayerBias = neuralNetwork.getSynapseDestinyLayerBias(currentIndex);

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
            currentSynapseWeights = neuralNetwork.getSynapseWeights(currentIndex);
            currentLayerBias = neuralNetwork.getSynapseDestinyLayerBias(currentIndex);
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

    public void exportOutputsFile(String fileName)
    {
        FileWriter file = createFile(fileName);

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
