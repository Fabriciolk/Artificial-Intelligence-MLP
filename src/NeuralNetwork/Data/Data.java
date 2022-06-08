package NeuralNetwork.Data;

public class Data
{
    /*
    * Esta classe representa um dado, contendo os valores
    * de seus componentes e sua respectiva classe.
    * */

    private final double[] data;
    private final double[] classData;

    public Data(double[] data, double[] classData)
    {
        this.data = data;
        this.classData = classData;
    }

    // Este método retorna todos os valores dos componentes do dado.
    public double[] getData() {
        return data;
    }

    // Este método retorna os valores que representam a classe do dado.
    public double[] getClassData() {
        return classData;
    }
}
