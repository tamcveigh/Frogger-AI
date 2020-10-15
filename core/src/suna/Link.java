package suna;

/** This class will represent the connections between any combination of neurons in the SUNA algorithm.
  * Links will feed an activation forward through the neurons
  */
public abstract class Link {

    private Neuron source;

    private Neuron destination;

    public Link(Neuron source, Neuron destination){
        this.source = source;
        this.destination = destination;
    }

    public abstract void feedForward();

    public Neuron getSource() {
        return source;
    }

    public void setSource(Neuron source) {
        this.source = source;
    }

    public Neuron getDestination() {
        return destination;
    }

    public void setDestination(Neuron destination) {
        this.destination = destination;
    }

}
