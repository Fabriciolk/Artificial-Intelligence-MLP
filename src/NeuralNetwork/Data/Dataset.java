package NeuralNetwork.Data;

public interface Dataset
{
    Data getNextTrainingData();

    Data getNextValidationData();

    boolean gotAllTrainingData();

    boolean gotAllValidationData();

    void resetDataRead();

    void resetValidationDataRead();

    void shuffleAll();

    int getDataLength();

    String dataRepresentation();
}
