package NeuralNetwork.Data;

public class Data
{
    private final double[] data;
    private final double[] classData;

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
