package suna;

public class ControlLink extends Link{

    public ControlLink(ControlNeuron source, Neuron destination, double weight){
        super(source,destination);
    }

    @Override
    public void feedForward() {
        ControlNeuron source = (ControlNeuron) super.getSource();
        Neuron destination = super.getDestination();

        if (source.isActivated() ) {
            source.activate();
            if(source.getOutputValue() > 0){
                destination.setActivated(true);
            }
        }//if source isn't activated, then it won't change its linked neurons
    }
}
