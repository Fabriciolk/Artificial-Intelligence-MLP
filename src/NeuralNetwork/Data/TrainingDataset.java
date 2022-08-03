package NeuralNetwork.Data;

public interface TrainingDataset extends Dataset
{
    Dataset getValidationDataset(double percent);
}
