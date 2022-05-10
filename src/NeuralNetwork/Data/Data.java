package NeuralNetwork.Data;

public class Data
{
    private double[] data;
    private double[] exceptedResult;

    public Data(double[] data, double[] exceptedResult)
    {
        this.data = data;
        this.exceptedResult = exceptedResult;
    }

    public double[] getData() {
        return data;
    }

    public double[] getExceptedResult() {
        return exceptedResult;
    }
}
