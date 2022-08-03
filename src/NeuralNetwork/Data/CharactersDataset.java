package NeuralNetwork.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CharactersDataset implements TrainingDataset
{
    /*
    * Esta classe é responsável por fornecer os dados
    * para a rede neural ser treinada e validada.
    * */

    private final Random random = new Random();
    private String fileName;
    private final String[] classesRepresentation = {"A", "B", "C", "D", "E", "J", "K"};
    private List<Data> dataList = new LinkedList<>();
    private int classDimension;
    private int dataDimension;

    public CharactersDataset(String fileName, int dataDimension, int classDimension)
    {
        this.classDimension = classDimension;
        this.dataDimension = dataDimension;
        this.fileName = fileName;

        readFile(fileName);
    }

    public CharactersDataset(List<Data> dataList)
    {
        this.dataList = dataList;
    }

    /*****************************\
     **                         **
     **      Public Methods     **
     **                         **
    \*****************************/

    @Override
    public String[] getDataClasses() {
        return classesRepresentation;
    }

    @Override
    public Iterator<Data> getIterator() {
        return dataList.iterator();
    }

    @Override
    public int getNumberOfRows() {
        return dataList.size();
    }

    @Override
    public int getNumberOfColumns() {
        return dataDimension + classDimension;
    }

    @Override
    public void shuffle() {
        shuffleList(dataList);
    }

    @Override
    public String getTitle() {
        return "Characters A, B, C, D, E, J, K";
    }

    @Override
    public Dataset getValidationDataset(double percent) {
        return new CharactersDataset(collectValidationData(percent));
    }

    /******************************\
     **                          **
     **      Private Methods     **
     **                          **
    \******************************/

    private void readFile(String fileName)
    {
        try{
            // Cria o fluxo e lê a primeira linha do dataset
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();

            while (line != null)
            {
                String[] stringValues = line.split(",");
                double[] data = new double[dataDimension];
                double[] classData = new double[classDimension];

                // Lê o dado e sua classe
                for (int i = 0; i < data.length; i++) data[i] = Double.parseDouble(removeBOMCharacterIfExists(stringValues[i]));
                for (int i = 0; i < classData.length; i++) classData[i] = Double.parseDouble(stringValues[data.length + i]);

                // Armazena o dado e sua classe e lê a próxima linha do dataset
                dataList.add(new Data(data, classData));
                line = bufferedReader.readLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Se parte do dataset for utilizada para validação, ou seja,
    // para análise de overfitting a partir dos erros das épocas,
    // este método reserva a porcentagem fornecida do total dos dados.
    private List<Data> collectValidationData (double validationDataPercent)
    {
        List<Data> validationList = new LinkedList<>();

        for (int i = 0; i < Math.round(dataList.size() * validationDataPercent); i++)
        {
            int randomIndex = random.nextInt(dataList.size());
            validationList.add(dataList.remove(randomIndex));
        }

        return validationList;
    }

    // Este método remove o caracter Byte Order Mark e retorna a nova string
    private String removeBOMCharacterIfExists (String string)
    {
        string = string.replaceAll("[^-0-1,]", "");
        return string;
    }

    // Este método embaralha aleatoriamente uma lista
    private <T> void shuffleList(List<T> listToShuffle)
    {
        List<T> copyList = new LinkedList<>(listToShuffle);
        listToShuffle.clear();

        for (int i = 0; i < copyList.size() + i; i++)
        {
            int randomIndex = random.nextInt(copyList.size());
            listToShuffle.add(copyList.remove(randomIndex));
        }
    }
}
