package suna;

/** This class will represent the individual neurons in the SUNA algorithm
  * There will need to be two types of neurons; Control Neurons, and regular Neurons
  * Control Neurons will alter the output of other neurons by either delaying, or disabling them
  * Regular Neurons behave like the neurons in NEAT/HyperNEAT in just feeding forward the data
  * Neurons will now have an activation time and activation order
  * That order is: Input Neurons, Control Neurons that do not have a link towards them, Control Neurons that have a link
  * to them, then any other neurons that are above the activation threshold.
  * Neurons can have different activation function, exactly the same as in HyperNEAT.
  */
public abstract class Neuron {

    /** The delay on the activation of the neuron.
     * A value of 1 is no delay
     */
    private double ActivationSpeed;

    /** Is this Neuron allowed to fire or not?*/
    private boolean isActivated;

    /** The value of the inputs to this Neuron*/
    private double inputValue;

    /** The value of the output of the Neuron after the activation function is used*/
    private double outputValue;

    /**/
    private Activation activationType;

    private double thresholdActivation;

    private final int ID;

    protected Neuron(int ID) {
        this.ID = ID;
    }

    public abstract void activate();

    public double getInputValue() {
        return inputValue;
    }

    public void setInputValue(double inputValue) {
        this.inputValue = inputValue;
    }

    public void addInputValue(double inputValue){
        this.inputValue += inputValue;
    }

    public double getActivationSpeed() {
        return ActivationSpeed;
    }

    public void setActivationSpeed(double activationSpeed) {
        ActivationSpeed = activationSpeed;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    public Activation getActivationType() {
        return activationType;
    }

    public void setActivationType(Activation activationType) {
        this.activationType = activationType;
    }

    public double getThresholdActivation() {
        return thresholdActivation;
    }

    public void setThresholdActivation(double thresholdActivation) {
        this.thresholdActivation = thresholdActivation;
    }

    enum Activation {
        CONTROL,
        IDENTITY,
        INPUT,
        OUTPUT,
        SIGMOID,
        THRESHOLD
    }

}