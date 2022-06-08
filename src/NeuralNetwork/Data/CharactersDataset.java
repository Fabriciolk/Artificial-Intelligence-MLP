package NeuralNetwork.Data;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CharactersDataset implements Dataset
{
    private final Random random = new Random();
    private List<Data> trainingDataList = new LinkedList<>();
    private List<Data> validationDataList = new LinkedList<>();
    private int numberOfDataRead = -1;
    private int numberOfValidationDataRead = -1;

    public CharactersDataset(String fileName, double validationDataPercent, int dataLength, int answerLength)
    {
        if (validationDataPercent < 0.0 || validationDataPercent > 1.0) return;

        try{
            // Cria o fluxo e lê a primeira linha do dataset
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line = bufferedReader.readLine();

            while (line != null)
            {
                String[] stringValues = line.split(",");
                double[] data = new double[dataLength];
                double[] classData = new double[answerLength];

                // Lê o dado e sua classe
                for (int i = 0; i < data.length; i++) data[i] = Double.parseDouble(removeBOMCharacterIfExists(stringValues[i]));
                for (int i = 0; i < classData.length; i++) classData[i] = Double.parseDouble(stringValues[data.length + i]);

                // Armazena o dado e sua classe e lê a próxima linha do dataset
                trainingDataList.add(new Data(data, classData));
                line = bufferedReader.readLine();
            }

            // Se necessário, coleta da lista dos dados aqueles destinados para validação
            collectValidationData(validationDataPercent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*****************************\
     **                         **
     **      Public Methods     **
     **                         **
    \*****************************/

    // Este método retorna um dado de treinamento.
    @Override
    public Data getNextTrainingData() {
        numberOfDataRead++;
        return trainingDataList.get(numberOfDataRead);
    }

    // Este método retorna um dado de validação.
    @Override
    public Data getNextValidationData() {
        numberOfValidationDataRead++;
        return validationDataList.get(numberOfValidationDataRead);
    }

    // Este método determina se todos os dados já forma lidos.
    @Override
    public boolean gotAllTrainingData() {
        return numberOfDataRead + 1 >= trainingDataList.size();
    }

    // Este método zera a quantidade de dados lidos
    // para que a lista de dados seja lida novamente
    @Override
    public void resetDataRead() {
        numberOfDataRead = 0;
    }

    // Este método determina se todos os dados já forma lidos.
    @Override
    public boolean gotAllValidationData() {
        return numberOfValidationDataRead + 1 >= validationDataList.size();
    }

    // Este método zera a quantidade de dados lidos
    // para que a lista de dados seja lida novamente
    @Override
    public void resetValidationDataRead() {
        numberOfValidationDataRead = 0;
    }

    // Este método embaralha aleatoriamente os dados de
    // treinamento e validação.
    @Override
    public void shuffleAll() {
        shuffleList(trainingDataList);
        shuffleList(validationDataList);
    }

    // Este método retorna a quantidade de dados para treinamento (não inclui os de validação)
    @Override
    public int getDataLength() {
        return trainingDataList.size();
    }

    // Este método retorna uma string representando a classificação dos dados.
    @Override
    public String dataRepresentation() {
        return "Characters A, B, C, D, E, J, K";
    }

    /******************************\
     **                          **
     **      Private Methods     **
     **                          **
    \******************************/

    // Se parte do dataset for utilizada para validação, ou seja,
    // para análise de overfitting a partir dos erros das épocas,
    // este método reserva a porcentagem fornecida do total dos dados.
    private void collectValidationData (double validationDataPercent)
    {
        for (int i = 0; i < Math.round(trainingDataList.size() * validationDataPercent); i++)
        {
            int randomIndex = random.nextInt(trainingDataList.size());
            validationDataList.add(trainingDataList.remove(randomIndex));
        }
    }

    // Este método remove o caracter Byte Order Mark e retorna a nova string
    private String removeBOMCharacterIfExists (String string)
    {
        for (int i = 0; i < string.length(); i++)
            if ((int)string.charAt(i) == 65279)
                return string.substring(0, i).concat(string.substring(i + 1));

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
