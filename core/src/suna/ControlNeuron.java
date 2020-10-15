package suna;

public class ControlNeuron extends Neuron {

    public ControlNeuron(int ID){
        super(ID);
        super.setActivated(false);
        super.setInputValue(0);
        super.setActivationSpeed(1);
        super.setInputValue(0);
        super.setOutputValue(0);
        super.setActivationType(Activation.CONTROL);
        super.setThresholdActivation(0);
    }

    public void activate(){
        if (super.getInputValue() > super.getThresholdActivation() ){
            super.setOutputValue(1);
        }else{
            super.setOutputValue(0);
        }
    }



}
