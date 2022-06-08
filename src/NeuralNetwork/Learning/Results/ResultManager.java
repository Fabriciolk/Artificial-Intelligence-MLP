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
    private FileWriter epochResultsCSVFile;
    private boolean CSVFileCreationEnabled = false;

    public ResultManager(NeuralNetwork neuralNetwork)
    {
        this.neuralNetwork = neuralNetwork;
    }

    public ResultManager(NeuralNetwork neuralNetwork, boolean createCSVFile)
    {
        this.neuralNetwork = neuralNetwork;
        CSVFileCreationEnabled = createCSVFile;
        if (CSVFileCreationEnabled) createCSVFile();
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

    // Este método preenche o arquivo CSV com todos registros das épocas
    // feitos até o momento em que ele é chamado.
    public void fillCSVFile()
    {
        if (!CSVFileCreationEnabled) return;

        try {
            for (int i = 0; i < Math.min(trainingEpochResultsList.size(), validationEpochResultsList.size()); i++) {
                epochResultsCSVFile.append(String.valueOf(trainingEpochResultsList.get(i).getErrorsMean())).append(", ").append(String.valueOf(validationEpochResultsList.get(i).getErrorsMean())).append("\n");
            }

            epochResultsCSVFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportNeuralNetworkParametersFile(String fileName)
    {

    }

    public void exportInitialWeightsFile(String fileName)
    {

    }

    public void exportFinalWeightsFile(String fileName)
    {

    }

    public void exportErrorsFile(String fileName)
    {

    }

    public void exportOutputsFile(String fileName)
    {

    }

    // Este método cria o arquivo CSV na pasta de results. O nome
    // do arquivo indica qual a estrutura da rede neural, ou seja,
    // quantos neurônios existem em cada camada.
    private void createCSVFile()
    {
        try {
            epochResultsCSVFile = new FileWriter( "results" + File.separator + "EpochResultForNeuralNetwork-" + neuralNetwork.getNeuronsByLayerLabel() + ".csv");
            epochResultsCSVFile.append("testError, validateError\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
