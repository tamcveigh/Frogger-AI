package suna;

/**
 * Implementation of Link for a connection between a ControlNeuron and
 * another neuron.
 */
public class ControlLink extends Link{

    /**
     * Constructor for Control Link
     * @param source the sending ControlNeuron
     * @param destination the receiving Neuron
     */
    public ControlLink(ControlNeuron source, Neuron destination){
        super(source,destination);
    }

    /**
     * Checks if the ControlNeuron should set the destination neuron's
     * activation field or not
     */
    @Override
    public void feedForward() {
        ControlNeuron source = (ControlNeuron) super.getSource();
        Neuron destination = super.getDestination();

        if (source.isActivated() ) {
            source.activate();
            destination.setActivated(source.getOutputValue() > 0);
        }
    }
}
