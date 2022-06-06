package NeuralNetwork.DataStructure;

import NeuralNetwork.Data.Data;
import NeuralNetwork.Data.TrainingDataset;

import java.util.ArrayList;

public class NeuralNetwork
{
    /* Esta classe é responsável pela estrutura de dados
    *  da rede neural, cujas camadas e sinapses estão or-
    *  ganizadas como o esquema abaixo. Dessa forma, a es-
    *  trutura sempre começa e termina com uma camada e
    *  todas sinapses estão entre duas camadas.
    *
    *
    *    +-------+                    +-------+
    *    |       |                    |       |
    *    |  1st  |   +------------+   |  2nd  |   +------------+
    *    |       |   |    1st     |   |       |   |    2nd     |
    *    | Layer |   |  Synaptic  |   | Layer |   |  Synaptic  |
    *    |       |   +------------+   |       |   +------------+   * * *
    *    +-------+                    +-------+
    *
    * */

    private final int MINIMUM_NEURON_BY_LAYER = 1;
    private boolean initializedWeights = false;
    private ArrayList<Layer> layerList = new ArrayList<>();
    private ArrayList<Synaptic> synapticList = new ArrayList<>();

    public NeuralNetwork(int neuronAmountOnInputLayer)
    {
        if (neuronAmountOnInputLayer < MINIMUM_NEURON_BY_LAYER) return;
        layerList.add(new Layer(neuronAmountOnInputLayer));
    }

    /*****************************\
     **                         **
     **      Public Methods     **
     **                         **
    \*****************************/

    // Fornecido um dado, este método retorna
    // as saídas da rede neural, após passar
    // por todas as sinapses.
    public double[] classifyData(double[] data)
    {
        if (!initializedWeights) return null;
        getInputLayer().setInputs(data);
        makeAllSynapse();
        return getOutputLayer().getOutputs();
    }

    // Este método retorna a quantidade de acertos que a rede neural
    // atingiu utilizando como teste todos os dados do dataset forne-
    // cido.
    public int countRightAnswers(TrainingDataset dataset, double threshold)
    {
        int count = 0;

        while (!dataset.gotAllTrainingData())
        {
            Data dataToTest = dataset.getNextTrainingData();
            double[] MPLOutput = classifyData(dataToTest.getData());

            count++;
            for (int j = 0; j < MPLOutput.length; j++)
            {
                if (MPLOutput[j] >= threshold)
                {
                    MPLOutput[j] = 1;
                }
                else
                {
                    MPLOutput[j] = 0;
                }
                if (MPLOutput[j] != dataToTest.getExceptedResult()[j])
                {
                    count--;
                    break;
                }
            }
        }

        return count;
    }

    // Este método adiciona uma camada na estrutura
    // de dados, com opção de escolher em qual posi-
    // ção inserir.
    public boolean addLayer(int index, Layer layer)
    {
        if (index < 1 || layer == null || layer.isEntryLayer()) return false;
        layerList.add(index, layer);
        updateSynapticListOnLayerInsertion(index);
        return true;
    }

    // Este método adiciona uma camada na
    // última posição da estrutura de dados.
    public boolean addLayer(Layer layer)
    {
        if (layer == null || layer.isEntryLayer()) return false;
        layerList.add(layer);
        updateSynapticListOnLayerInsertion(layerList.size() - 1);
        return true;
    }

    // Este método remove uma camada na
    // estrutura de dados em uma determi-
    // nada posição.
    public boolean removeLayer(int index)
    {
        if (index < 1) return false;
        layerList.remove(index);
        updateSynapticListOnLayerRemoving(index);
        return true;
    }

    // Este método retorna a camada de entrada
    public Layer getInputLayer()
    {
        return layerList.get(0);
    }

    // Este método retorna a camada de saída
    public Layer getOutputLayer()
    {
        return layerList.get(layerList.size() - 1);
    }

    // Este método gera pesos e bias aleatórios
    // para todas as sinapses.
    public boolean setAllWeightsAndBiasRandomly()
    {
        if (initializedWeights) return false;
        for (Synaptic synaptic : synapticList) synaptic.setWeightsAndBiasRandomly();
        initializedWeights = true;
        return true;
    }

    // Este método altera todos os pesos e bias para 0.
    public void resetAllWeightsAndBias()
    {
        for (Synaptic synaptic : synapticList) synaptic.resetWeightsAndBias();
    }

    // Este método executa o feedfoward, realizando todas as
    // sinapses. Em cada sinapse, cada neurônio da camada da
    // direita (visualizando o diagrama da estrutura de dados)
    // recebe a soma ponderada, incluindo seu bias, com pesos
    // e entrada da camada da esquerda.
    public void makeAllSynapse()
    {
        for (Synaptic synaptic : synapticList) synaptic.makeSynapse();
    }

    // Este método executa o backpropagation fornecendo os erros de correção
    // para cada sinapse. Ele é responsável por realizar o "delivery" de erros
    // de correção, onde cada sinapse gera seus erros resultantes para a
    // sinapse anterior a ela e este método realiza a entrega.
    public void makeBackpropagation (double learningRate, double[] correctionErrorsFromOutputLayer)
    {
        for (int i = synapticList.size() - 1; i >= 0; i--)
        {
            correctionErrorsFromOutputLayer = synapticList.get(i).adjustWeightsAndBias(learningRate, correctionErrorsFromOutputLayer);
        }
    }

    // Este método imprime todos os pesos e baias de todas as sinapses.
    public void printAllLayerWeights()
    {
        for (Synaptic synaptic : synapticList) synaptic.printWeightsAndBias();
    }

    // Retorna a quantidade de neurônios em cada camada na forma w-x-y-z..., onde
    // w é a quantidade na primeira camada (de entrada), x a quantidade na segunda
    // camada, e assim sucessivamente.
    public String getNeuronsByLayerLabel()
    {
        String label = String.valueOf(getInputLayer().getNeurons().length);

        for (int i = 1; i < layerList.size(); i++) label = label.concat("-").concat(String.valueOf(layerList.get(i).getNeurons().length));

        return label;
    }

    /******************************\
     **                          **
     **      Private Methods     **
     **                          **
    \******************************/

    // Este método organiza a estrutura de dados da rede neural
    // após uma inserção de uma camada.
    private void updateSynapticListOnLayerInsertion(int indexInserted)
    {
        // Se a inserção foi feita na última posição da lista.
        if (indexInserted == layerList.size() - 1)
        {
            Synaptic synaptic = new Synaptic(layerList.get(layerList.size() - 2), layerList.get(layerList.size() - 1));
            synapticList.add(synaptic);
        }
        else
        {
            Synaptic backwardSynaptic = new Synaptic(layerList.get(indexInserted - 1), layerList.get(indexInserted));
            Synaptic forwardSynaptic = new Synaptic(layerList.get(indexInserted), layerList.get(indexInserted + 1));
            synapticList.remove(indexInserted - 1);
            synapticList.remove(indexInserted);
            synapticList.add(indexInserted - 1, backwardSynaptic);
            synapticList.add(indexInserted, forwardSynaptic);
        }
    }

    // Este método organiza a estrutura de dados da rede neural
    // após uma remoção de uma camada.
    private void updateSynapticListOnLayerRemoving(int indexRemoved)
    {
        // Se a remoção foi feita na última posição da lista.
        if (indexRemoved == layerList.size())
        {
            synapticList.remove(synapticList.size() - 1);
        }
        else
        {
            synapticList.remove(indexRemoved - 1);
            synapticList.remove(indexRemoved);
            Synaptic synaptic = new Synaptic(layerList.get(indexRemoved - 1), layerList.get(indexRemoved + 1));
            synapticList.add(indexRemoved - 1, synaptic);
        }
    }
}
