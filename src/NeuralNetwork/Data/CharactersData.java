package NeuralNetwork.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;

public class CharactersData implements TrainingDataSet
{
    LinkedList<double[]> dataList = new LinkedList<>();
    LinkedList<double[]> classDataList = new LinkedList<>();
    int numberOfDataRead = 0;

    public CharactersData(String fileName)
    {
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();

            while (line != null)
            {
                String[] stringValues = line.split(",");
                double[] floatValues = new double[stringValues.length - 7];
                double[] floatClass = new double[7];

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
    }

    @Override
    public Data getNextData() {
        Data data = new Data(dataList.get(numberOfDataRead), classDataList.get(numberOfDataRead));
        numberOfDataRead++;
        return data;
    }

    @Override
    public boolean gotAllData() {
        boolean gotAll = numberOfDataRead == dataList.size();
        if (gotAll) numberOfDataRead = 0;
        return gotAll;
    }

    @Override
    public void shuffleAllData() {
        Random random = new Random();
        LinkedList<double[]> newDataList = new LinkedList<>();
        LinkedList<double[]> newClassDataList = new LinkedList<>();

        for (int i = 0; i < dataList.size(); i++)
        {
            int randomIndex = random.nextInt(dataList.size() - i);
            newDataList.add(dataList.remove(randomIndex));
            newClassDataList.add(classDataList.remove(randomIndex));
        }

        dataList = newDataList;
        classDataList = newClassDataList;
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
