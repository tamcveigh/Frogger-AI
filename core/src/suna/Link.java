package suna;

/** This class will represent the connections between any combination of neurons in the SUNA algorithm.
  * Links will feed an activation forward through the neurons
  */
public abstract class Link {

    /** The Neuron that sends it's output out*/
    private Neuron source;

    /** The Neuron that recieves output into it's input */
    private Neuron destination;

    /**
     * Assigns the sending and receiving neurons
     * @param source the originating neuron
     * @param destination the reveiving neuron
     */
    public Link(Neuron source, Neuron destination){
        this.source = source;
        this.destination = destination;
    }

    /**
     * Abstract method to allow for specific implementation of how
     * the two neurons interact when a message is passed between them
     */
    public abstract void feedForward();

    /**
     * Getter method for field source
     * @return the field source
     */
    public Neuron getSource() {
        return source;
    }

    /**
     * Setter method for field source
     * @param source the new source value
     */
    public void setSource(Neuron source) {
        this.source = source;
    }

    /**
     * Getter method for field destination
     * @return the field destination
     */
    public Neuron getDestination() {
        return destination;
    }

    /**
     * Setter method for field destination
     * @param destination the new destination value
     */
    public void setDestination(Neuron destination) {
        this.destination = destination;
    }

}
