package NeuralNetwork.Learning.Results;

public class ConfusionMatrix
{
    int[][] confusionMatrix;

    public ConfusionMatrix(int[][] confusionMatrix)
    {
        this.confusionMatrix = confusionMatrix;
    }

    public int getTruePositive(int index)
    {
        return confusionMatrix[index][index];
    }

    public int getFalsePositive(int index)
    {
        int sum = 0;

        for (int i = 0; i < confusionMatrix.length; i++)
        {
            if (i == index) continue;
            sum += confusionMatrix[i][index];
        }

        return sum;
    }

    public int getTrueNegative(int index)
    {
        int sum = 0;

        for (int i = 0; i < confusionMatrix[index].length; i++)
        {
            if (i == index) continue;
            sum += confusionMatrix[index][i];
        }

        return sum;
    }

    public int getFalseNegative(int index)
    {
        int sum = 0;

        for (int i = 0; i < confusionMatrix.length; i++)
        {
            for (int j = 0; j < confusionMatrix[0].length; j++)
            {
                if (i == index || j == index) continue;
                sum += confusionMatrix[i][j];
            }
        }

        return sum;
    }

    public int getPositive(int index)
    {
        return getTruePositive(index) + getFalseNegative(index);
    }

    public int getNegative(int index)
    {
        return getFalsePositive(index) + getTrueNegative(index);
    }

    public int getPositivePrediction(int index)
    {
        return getTruePositive(index) + getFalsePositive(index);
    }

    public int getNegativePrediction(int index)
    {
        return getFalseNegative(index) + getTrueNegative(index);
    }

    public int getNumberOfElements()
    {
        return getNegative(0) + getPositive(0);
    }
}
