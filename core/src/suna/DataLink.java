package suna;

public class DataLink extends Link{

    private double weight;

    public DataLink(NormalNeuron source, Neuron destination, double weight){
        super(source, destination);
        this.weight = weight;
    }

    public void feedForward(){

        Neuron source = super.getSource();
        Neuron destination = super.getDestination();
        double weight = this.getWeight();

        if (source.isActivated() ) {
            source.activate();
            double value = weight * source.getOutputValue();
            destination.addInputValue(value);
        }//if source isn't activated, then it won't change its linked neurons
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
