package NeuralNetwork.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class CharactersData implements TrainingDataset
{
    Random random = new Random();
    LinkedList<double[]> dataList = new LinkedList<>();
    LinkedList<double[]> classDataList = new LinkedList<>();

    LinkedList<double[]> validationDataList = new LinkedList<>();
    LinkedList<double[]> validationClassDataList = new LinkedList<>();
    int numberOfDataRead = 0;
    int numberOfValidationDataRead = 0;

    public CharactersData(String fileName, double validationDataPercent, int dataLength, int answerLength)
    {
        if (validationDataPercent < 0.0 || validationDataPercent > 1.0) return;

        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();

            while (line != null)
            {
                String[] stringValues = line.split(",");
                for (int i = 0; i < stringValues.length; i++)
                {
                    stringValues[i] = stringValues[i].replace("\"", "");
                }
                double[] floatValues = new double[dataLength];
                double[] floatClass = new double[answerLength];

                for (int i = 0; i < floatValues.length; i++)
                {
                    String number = stringValues[i].strip();
                    boolean negative = false;

                    if (number.toCharArray().length == 3 && number.charAt(1) == '-')
                    {
                        number = String.valueOf(number.charAt(2));
                        negative = true;
                    }

                    floatValues[i] = Double.parseDouble(number);
                    if (negative) floatValues[i] *= -1;
                }

                for (int i = 0; i < floatClass.length; i++)
                {
                    floatClass[i] = Double.parseDouble(stringValues[floatValues.length + i]);
                }

                dataList.add(floatValues);
                classDataList.add(floatClass);
                line = bufferedReader.readLine();
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }

        for (int i = 0; i < Math.round(dataList.size() * validationDataPercent); i++)
        {
            int randomIndex = random.nextInt(dataList.size());
            validationDataList.add(dataList.remove(randomIndex));
            validationClassDataList.add(classDataList.remove(randomIndex));
        }
    }

    @Override
    public Data getNextTrainingData() {
        Data data = new Data(dataList.get(numberOfDataRead), classDataList.get(numberOfDataRead));
        numberOfDataRead++;
        return data;
    }

    @Override
    public Data getNextValidationData() {
        Data data = new Data(validationDataList.get(numberOfValidationDataRead), validationClassDataList.get(numberOfValidationDataRead));
        numberOfValidationDataRead++;
        return data;
    }

    @Override
    public boolean gotAllTrainingData() {
        return numberOfDataRead == dataList.size();
    }

    @Override
    public void resetDataRead() {
        numberOfDataRead = 0;
    }

    @Override
    public boolean gotAllValidationData() {
        return numberOfValidationDataRead == validationDataList.size();
    }

    @Override
    public void resetValidationDataRead() {
        numberOfValidationDataRead = 0;
    }

    @Override
    public void shuffleAll() {
        LinkedList<double[]> newDataList = new LinkedList<>();
        LinkedList<double[]> newClassDataList = new LinkedList<>();

        for (int i = 0; i < dataList.size() + i; i++)
        {
            int randomIndex = random.nextInt(dataList.size());
            newDataList.add(dataList.remove(randomIndex));
            newClassDataList.add(classDataList.remove(randomIndex));
        }

        dataList = newDataList;
        classDataList = newClassDataList;

        ///////

        LinkedList<double[]> newDataList2 = new LinkedList<>();
        LinkedList<double[]> newClassDataList2 = new LinkedList<>();

        for (int i = 0; i < validationDataList.size() + i; i++)
        {
            int randomIndex = random.nextInt(validationDataList.size());
            newDataList2.add(validationDataList.remove(randomIndex));
            newClassDataList2.add(validationClassDataList.remove(randomIndex));
        }

        validationDataList = newDataList2;
        validationClassDataList = newClassDataList2;
    }

    @Override
    public int getDataLength() {
        return dataList.size();
    }

    @Override
    public String dataRepresentation() {
        return "Caracteres A, B, C, D, E, J, K";
    }
}
