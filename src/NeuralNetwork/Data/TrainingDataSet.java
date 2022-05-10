package NeuralNetwork.Data;

public interface TrainingDataSet
{
    Data getNextData();

    boolean gotAllData();

    void shuffleAllData();

    int getDataLength();

    String dataRepresentation();
}
