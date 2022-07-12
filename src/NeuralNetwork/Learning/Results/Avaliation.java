package NeuralNetwork.Learning.Results;

public class Avaliation
{
    ConfusionMatrix confusionMatrix;

    public Avaliation(ConfusionMatrix confusionMatrix)
    {
        this.confusionMatrix = confusionMatrix;
    }

    public double getAccuracy(int index)
    {
        return ((confusionMatrix.getTruePositive(index) + confusionMatrix.getTrueNegative(index)) * 1.0) / (confusionMatrix.getPositive(index) + confusionMatrix.getNegative(index));
    }

    public double getError(int index)
    {
        return ((confusionMatrix.getFalsePositive(index) + confusionMatrix.getFalseNegative(index)) * 1.0) / (confusionMatrix.getPositive(index) + confusionMatrix.getNegative(index));
    }

    public double getSensibility(int index)
    {
        return (confusionMatrix.getTruePositive(index) * 1.0) / (confusionMatrix.getTruePositive(index) + confusionMatrix.getFalseNegative(index));
    }

    public double getFalsePositiveRate(int index)
    {
        return (confusionMatrix.getFalsePositive(index) * 1.0) / (confusionMatrix.getTrueNegative(index) + confusionMatrix.getFalsePositive(index));
    }

    public double getSpecificity(int index)
    {
        return (confusionMatrix.getTrueNegative(index) * 1.0) / (confusionMatrix.getFalsePositive(index) + confusionMatrix.getTrueNegative(index));
    }

    public double getPrecision(int index)
    {
        return (confusionMatrix.getTruePositive(index) * 1.0) / (confusionMatrix.getTruePositive(index) + confusionMatrix.getFalsePositive(index));
    }

    public double getNegativePredictability(int index)
    {
        return (confusionMatrix.getTrueNegative(index) * 1.0) / (confusionMatrix.getTrueNegative(index) + confusionMatrix.getFalseNegative(index));
    }

    public double getFalseFindRate(int index)
    {
        return (confusionMatrix.getFalsePositive(index) * 1.0) / (confusionMatrix.getTruePositive(index) + confusionMatrix.getFalsePositive(index));
    }

    public void printAllMetrics(int index)
    {
        System.out.println("Error: " + getAccuracy(index));
        System.out.println("Accuracy: " + getError(index));
        System.out.println("Sensibility: " + getSensibility(index));
        System.out.println("False positive rate: " + getFalsePositiveRate(index));
        System.out.println("Specificity: " + getSpecificity(index));
        System.out.println("Precision: " + getPrecision(index));
        System.out.println("Negative Predictability: " + getNegativePredictability(index));
        System.out.println("False Find Rate: " + getFalseFindRate(index));
    }
}
