import Function.ArcTan.ArcTangentFunction;
import Function.Sigmoid.SigmoidFunction;
import NeuralNetwork.Data.CharactersData;
import NeuralNetwork.Data.TrainingDataSet;
import NeuralNetwork.DataStructure.Layer;
import NeuralNetwork.DataStructure.NeuralNetwork;
import NeuralNetwork.Learning.NeuralNetworkTraining;

public class Main
{
    public static void main(String[] args)
    {
        NeuralNetwork neuralNetwork = new NeuralNetwork(63);
        neuralNetwork.addLayer(new Layer(4, new SigmoidFunction()));
        neuralNetwork.addLayer(new Layer(7, new SigmoidFunction()));

        TrainingDataSet charactersData = new CharactersData("C:\\Users\\fabri\\Documents\\USP\\Trabalhos_e_Atividades\\2022\\IA\\EP\\Data\\caracteres-limpo.csv");

        NeuralNetworkTraining neuralNetworkTraining = new NeuralNetworkTraining(neuralNetwork, charactersData);
        neuralNetworkTraining.start(1);

        //double[] outputs = neuralNetwork.classifyData(new double[] {-1,-1});

        System.out.println();
        //for (int i = 0; i < outputs.length; i++) System.out.printf("%.1f \t", outputs[i]);
    }
}
