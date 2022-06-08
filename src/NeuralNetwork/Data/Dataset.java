package NeuralNetwork.Data;

public interface Dataset
{
    Data getNextTrainingData();

    Data getNextValidationData();

    boolean gotAllTrainingData();

    boolean gotAllValidationData();

    void resetTrainingDataRead();

    void resetValidationDataRead();

    void shuffleAll();

    int getDataLength();

    String dataRepresentation();
}
