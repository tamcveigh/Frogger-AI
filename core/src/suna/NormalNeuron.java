package suna;

public class NormalNeuron extends Neuron{

    public NormalNeuron(int ID, Activation activationType){
        super(ID);
        super.setActivated(false);
        super.setInputValue(0);
        super.setActivationSpeed(1);
        super.setInputValue(0);
        super.setOutputValue(0);
        super.setActivationType(activationType);
        super.setThresholdActivation(0);
    }

    public void activate(){
        if(super.isActivated()){
            double input = super.getInputValue();
            double output = 0;
            switch (super.getActivationType()) {

                case IDENTITY:
                case INPUT:
                case OUTPUT:
                    output = input;
                    break;
                case CONTROL:
                case THRESHOLD:
                    if(input > super.getThresholdActivation()){
                        output = 1;
                    }else{
                        output = 0;
                    }
                    break;
                case SIGMOID:
                    //output = sigmoid(input)
                    break;
            }
            super.setOutputValue(output);
        }
    }


}
