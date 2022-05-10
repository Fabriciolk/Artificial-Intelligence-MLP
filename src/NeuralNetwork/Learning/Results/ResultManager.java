package NeuralNetwork.Learning.Results;

import NeuralNetwork.Learning.Results.EpochResult;

import java.util.LinkedList;

public class ResultManager
{
    private final LinkedList<EpochResult> results = new LinkedList<>();
    private boolean trainingFinished = false;

    public void addEpochResult(EpochResult epochResult)
    {
        results.add(epochResult);
    }
}
