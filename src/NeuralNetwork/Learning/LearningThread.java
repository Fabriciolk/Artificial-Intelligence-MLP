package NeuralNetwork.Learning;

import NeuralNetwork.Data.Dataset;
import NeuralNetwork.DataStructure.NeuralNetwork;

import Exception.InvalidLearningRateException;

import java.util.LinkedList;

public class LearningThread implements Runnable
{
    private final LinkedList<String> performanceList = new LinkedList<>();
    private NeuralNetwork neuralNetwork;
    private NeuralNetworkTraining neuralNetworkTraining;
    private Dataset dataset;
    private Dataset testingDataset;
    private boolean writeResultsOnCSVFileEnabled;
    private int numberOfEpochs;
    private double thresholdForTest = 0.5;
    private boolean learningIsFinished = false;
    private boolean performanceHistoricPrinted_GAMBIARRA = false;

    public int acertos = 0;

    public LearningThread (NeuralNetwork neuralNetwork, Dataset dataset, int numberOfEpochs, boolean writeResultsOnCSVFileEnabled)
    {
        this.neuralNetwork = neuralNetwork;
        this.dataset = dataset;
        this.numberOfEpochs = numberOfEpochs;
        this.writeResultsOnCSVFileEnabled = writeResultsOnCSVFileEnabled;
        this.neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, dataset, writeResultsOnCSVFileEnabled);
    }

    public LearningThread (NeuralNetwork neuralNetwork, Dataset dataset, int numberOfEpochs)
    {
        this(neuralNetwork, dataset, numberOfEpochs, false);
    }

    public LearningThread (NeuralNetwork neuralNetwork, Dataset dataset)
    {
        this(neuralNetwork, dataset, 1);
    }

    public void setNumberOfEpochs (int numberOfEpochs)
    {
        this.numberOfEpochs = numberOfEpochs;
    }

    public void setWriteResultsOnCSVFileEnabled (boolean enabled)
    {
        this.writeResultsOnCSVFileEnabled = enabled;
    }

    public void setLearningRate (double rate)
    {
        this.neuralNetworkTraining.setLearningRate(rate);
    }

    public void setTestingDataset (Dataset testingDataset)
    {
        this.testingDataset = testingDataset;
    }

    public void setThresholdForTest (double thresholdForTest)
    {
        this.thresholdForTest = thresholdForTest;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public boolean isPerformanceHistoricPrinted_GAMBIARRA() {
        return performanceHistoricPrinted_GAMBIARRA;
    }

    public boolean learningIsFinished ()
    {
        return learningIsFinished;
    }

    @Override
    public void run() {
        neuralNetworkTraining.start(numberOfEpochs);
        if (testingDataset != null) registerPerformanceInHistoric();
        learningIsFinished = true;

        System.out.printf("Acertos para rede neural %s: ", getNeuralNetwork().getNeuronsByLayerLabel());
        printPerformanceHistoric();
        System.out.println();
    }

    private void registerPerformanceInHistoric()
    {
        int acerto = neuralNetwork.countRightAnswers(testingDataset, thresholdForTest);
        acertos += acerto;

        performanceList.add(String.format("[%.2f] %d/%d",
                neuralNetworkTraining.getLearningRate(),
                acerto,
                testingDataset.getDataLength()));
    }

    public void printPerformanceHistoric ()
    {
        for (String performance : performanceList) System.out.print(performance + "\t");
        performanceHistoricPrinted_GAMBIARRA = true;
    }

}
