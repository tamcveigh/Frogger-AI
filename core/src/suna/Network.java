package suna;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Network {

    private List<Neuron> neuronsList;

    private List<Link> connections;

    private NormalNeuron[] inputNeurons;

    private NormalNeuron[] outputNeurons;

    private int neuronCount;

    private int fitness;

    public Network(int inputCount, int outputCount) {

        this.neuronCount = 0;
        this.neuronsList = new ArrayList<Neuron>();
        this.inputNeurons = new NormalNeuron[inputCount];
        for (int i = 0; i < inputCount; i++) {
            NormalNeuron inputNeuron = new NormalNeuron(this.neuronCount, Neuron.Activation.INPUT);
            this.inputNeurons[i] = inputNeuron;
            this.neuronsList.add(inputNeuron);
            this.neuronCount++;
        }

        this.outputNeurons = new NormalNeuron[outputCount];
        for (int i = 0; i < outputCount; i++) {
            NormalNeuron outputNeuron = new NormalNeuron(this.neuronCount, Neuron.Activation.OUTPUT);
            this.outputNeurons[i] = outputNeuron;
            this.neuronsList.add(outputNeuron);
            this.neuronCount++;
        }

        this.connections = new ArrayList<Link>();

        for (NormalNeuron in : this.inputNeurons) {
            for (NormalNeuron out : this.outputNeurons) {
                DataLink link = new DataLink(in, out, Math.random() * 2 - 1);
                this.connections.add(link);
            }
        }
        this.fitness = 0;
    }


    public Network(Network original) {

    }

    public void mutate() {
        double mutationChance = Math.random();
        double remainingChance = 1.0;
        remainingChance = remainingChance - Coefficients.MUTATION_ADD_NEURON.getValue();
        if (mutationChance > remainingChance) {
            addNeuron();
        } else {
            remainingChance = remainingChance - Coefficients.MUTATION_REMOVE_NEURON.getValue();
            if (mutationChance > remainingChance) {
                removeNeuron();
            } else {
                remainingChance = remainingChance - Coefficients.MUTATION_ADD_CONNECTION.getValue();
                if (mutationChance > remainingChance) {
                    addConnection();
                } else {
                    remainingChance = remainingChance - Coefficients.MUTATION_REMOVE_CONNECTION.getValue();
                    if (mutationChance > remainingChance) {
                        removeConnection();
                    }
                }
            }
        }


    }

    private void removeConnection() {
        int linkSize = this.connections.size();
        Random generator = new Random();
        int randomLink = generator.nextInt(linkSize);
        Link removedLink = this.connections.get(randomLink);
        this.connections.remove(randomLink);
        //TODO: add check if link participates in a neuromodulated link

    }

    private void addConnection() {
        //TODO: Add chance of neuromodulated or control link
        boolean badLink = true;
        NormalNeuron start = null;
        Neuron end = null;
        while(badLink){
            Random generator = new Random();
            start = (NormalNeuron) this.neuronsList.get(generator.nextInt(this.neuronCount) );
            end = this.neuronsList.get(generator.nextInt(this.neuronCount) );

            boolean isInput = false;
            for(int i = 0; i < this.inputNeurons.length && !isInput; i++){
                isInput = end.equals(this.inputNeurons[i] );
            }
            boolean isOutput = false;
            for(int i = 0; i < this.outputNeurons.length && !isOutput && !isInput; i++){
                isOutput = start.equals(this.outputNeurons[i] );
            }

            if(!isInput && !isOutput){
                badLink = false;
            }
        }

        DataLink newLink = new DataLink(start, end, Math.random() * 2 - 1);
        this.connections.add(newLink);

    }

    private void removeNeuron() {
        Neuron removedNeuron = null;
        Random generator = new Random();
        boolean validNeuron = false;

        while(!validNeuron){
            removedNeuron = this.neuronsList.get(generator.nextInt(this.neuronCount) );

            boolean isInput = false;
            for(int i = 0; i < this.inputNeurons.length && !isInput; i++){
                isInput = removedNeuron.equals(this.inputNeurons[i] );
            }
            boolean isOutput = false;
            for(int i = 0; i < this.outputNeurons.length && !isOutput && !isInput; i++){
                isOutput = removedNeuron.equals(this.outputNeurons[i] );
            }

            if(!isInput && !isOutput){
                validNeuron = true;
            }
        }

        for(Link l: this.connections){
            if(removedNeuron.equals(l.getSource() ) ){
                this.connections.remove(l);
            }
            if(removedNeuron.equals(l.getDestination() ) ){
                this.connections.remove(l);
            }
        }

    }

    private void addNeuron() {
        Random generator = new Random();
        Link mutatedLink = this.connections.get( generator.nextInt( this.connections.size() ) );
        Neuron newNeuron = new NormalNeuron(this.neuronCount, mutatedLink);
        this.neuronsList.add(newNeuron);
        this.neuronCount++;
        this.connections.remove(mutatedLink);
    }

    /**
     * The Spectral Diversity Array will determine crossover compatibility
     * @return
     */
    public int[] getSpectralAnalysis(){

        int[] spectralArray = new int[5];

        for(Neuron n: this.neuronsList){
            switch (n.getActivationType() ){
                case IDENTITY:
                    spectralArray[0] = spectralArray[0] + 1;
                    break;
                case SIGMOID:
                    spectralArray[1] = spectralArray[1] + 1;
                    break;
                case THRESHOLD:
                    spectralArray[2] = spectralArray[2] + 1;
                    break;
                case CONTROL:
                    spectralArray[3] = spectralArray[3] + 1;
                    break;
            }
            if(n.getActivationSpeed() > 1){
                spectralArray[4] = spectralArray[4] + 1;
            }
        }

        return spectralArray;

    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}