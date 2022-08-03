package NeuralNetwork.Data;

import java.util.Iterator;

public interface Dataset
{
    String[] getDataClasses();

    Iterator<Data> getIterator();

    int getNumberOfRows();

    int getNumberOfColumns();

    void shuffle();

    String getTitle();
}
