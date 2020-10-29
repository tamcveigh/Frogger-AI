package suna;

/**
 * Implentaion of Link for a Normal Neuron connected to another Neuron
 */
public class DataLink extends Link{

    /** The weight of the connection between the neurons*/
    private double weight;

    /**
     * Constructor for DataLink
     * @param source the originating NormalNeuron
     * @param destination the receiving neuron
     * @param weight the weight of the connection
     */
    public DataLink(NormalNeuron source, Neuron destination, double weight){
        super(source, destination);
        this.weight = weight;
    }

    /**
     * Passes the value from the source NormalNeuron to the destination
     * neuron.
     */
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

    /**
     * Getter method for field weight
     * @return the field weight
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Setter method for field weight
     * @param weight the new value for field weight
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
