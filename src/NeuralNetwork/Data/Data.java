package NeuralNetwork.Data;

public class Data
{
    private double[] data;
    private double[] classData;

    public Data(double[] data, double[] classData)
    {
        this.data = data;
        this.classData = classData;
    }

    public double[] getData() {
        return data;
    }

    public double[] getClassData() {
        return classData;
    }
}
