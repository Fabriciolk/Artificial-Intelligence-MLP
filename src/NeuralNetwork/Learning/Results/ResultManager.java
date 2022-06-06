package NeuralNetwork.Learning.Results;

import NeuralNetwork.DataStructure.NeuralNetwork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class ResultManager
{
    private final LinkedList<EpochResult> listOfResultsByEpoch = new LinkedList<>();
    private final LinkedList<EpochResult> listOfResultsByEpochForValidate = new LinkedList<>();
    private final NeuralNetwork neuralNetwork;
    private FileWriter epochResultsCSVFile;
    private boolean trainingFinished = false;
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

    public void addEpochResult(EpochResult epochResult)
    {
        listOfResultsByEpoch.add(epochResult);
    }

    public void addEpochResultOnValidateList(EpochResult epochResult)
    {
        listOfResultsByEpochForValidate.add(epochResult);
    }

    public void exportCSVFile()
    {
        if (!CSVFileCreationEnabled) return;

        try {
            for (int i = 0; i < Math.min(listOfResultsByEpoch.size(), listOfResultsByEpochForValidate.size()); i++) {
                epochResultsCSVFile.append(listOfResultsByEpoch.get(i).getErrorsMean() + ", " + listOfResultsByEpochForValidate.get(i).getErrorsMean() + "\n");
            }

            epochResultsCSVFile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

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
