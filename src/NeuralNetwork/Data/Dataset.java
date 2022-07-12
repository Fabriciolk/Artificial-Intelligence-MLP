package NeuralNetwork.Data;

public interface Dataset
{
    String classNameByIndex(int index);

    Data getNextTrainingData();

    Data getNextValidationData();

    boolean gotAllTrainingData();

    boolean gotAllValidationData();

    int getClassDataLength();

    void resetTrainingDataRead();

    void resetValidationDataRead();

    void shuffleAll();

    int getNumberOfDataForTraining();

    String dataRepresentation();
}
