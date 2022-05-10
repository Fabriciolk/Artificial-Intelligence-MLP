package NeuralNetwork.DataStructure;

import java.util.ArrayList;

public class NeuralNetwork
{
    private final int MINIMUM_NEURON_BY_LAYER = 1;
    private boolean initInitialWeights = false;
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

    public double[] classifyData(double[] data)
    {
        if (!initInitialWeights) return null;
        makeAllSynapse();
        return getOutputLayer().getOutputs();
    }

    public boolean addLayer(int index, Layer layer)
    {
        if (index < 1 || layer == null) return false;
        layerList.add(index, layer);
        updateSynapticListOnLayerInsertion(index);
        return true;
    }

    public boolean addLayer(Layer layer)
    {
        if (layer == null) return false;
        layerList.add(layer);
        updateSynapticListOnLayerInsertion(layerList.size() - 1);
        return true;
    }

    public boolean removeLayer(int index)
    {
        if (index < 1) return false;
        layerList.remove(index);
        updateSynapticListOnLayerRemoving(index);
        return true;
    }

    public Layer getInputLayer()
    {
        return layerList.get(0);
    }

    public Layer getOutputLayer()
    {
        return layerList.get(layerList.size() - 1);
    }

    public boolean setAllWeightsAndBiasRandomly()
    {
        if (initInitialWeights) return false;
        for (Synaptic synaptic : synapticList) synaptic.setWeightsAndBiasRandomly();
        initInitialWeights = true;
        return true;
    }

    public void resetAllWeights()
    {
        for (Synaptic synaptic : synapticList) synaptic.resetWeightsAndBias();
    }

    public void makeAllSynapse()
    {
        for (Synaptic synaptic : synapticList) synaptic.makeSynapse();
    }

    public void makeBackpropagation (double learningRate, double[] correctionErrorsFromOutputLayer)
    {
        for (int i = synapticList.size() - 1; i >= 0; i--)
        {
            correctionErrorsFromOutputLayer = synapticList.get(i).adjustWeightsAndBias(learningRate, correctionErrorsFromOutputLayer);
        }
    }

    public void printLayerWeights()
    {
        for (Synaptic synaptic : synapticList) synaptic.printWeightsAndBias();
    }

    /******************************\
     **                          **
     **      Private Methods     **
     **                          **
    \******************************/

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
