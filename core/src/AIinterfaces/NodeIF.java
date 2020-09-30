package AIinterfaces;

import hyperneat.Link;

import java.util.List;

public interface NodeIF {

    /**
     * Returns this nodes identification number.
     * @return This nodes identification number.
     */
    public int getId();

    /**
     * Returns the layer this node resides on.
     * @return The layer this node resides on.
     */
    public int getLayer();

    /**
     * Sets this nodes output value to the supplied one.
     * @param outputValue The supplied output value.
     */
    public void setOutputValue(double outputValue);

    /**
     * Activates the node. Calls the activation function if this node is not on the bias or input
     * layer. Then, grabs each of the connected output nodes and send something to that output
     * nodes input.
     */
    public void activate();

    /**
     * Sets this nodes input value to the supplied one.
     * @param inputValue The supplied input value.
     */
    public void setInputValue(double inputValue);

    /**
     * Returns this nodes outgoing links.
     * @return This nodes outgoing links.
     */
    public List<LinkIF> getOutgoingLinks();

    /**
     * Increments this nodes layer by 1.
     */
    public void incrementLayer();

    /**
     * Returns this nodes value before activation.
     * @return This nodes value before activation.
     */
    public double getInputValue();


    /**
     * Gets the input bias layer
     * @return The bias node input layer
     */
    public int getInputBiasLayer();

    /**
     * The activation function for this node
     * @return The activation number
     */
    public int getRandomActive();

    /**
     * Gets the slope of the activation function
     * @return The slope of the activation function
     */
    public double getSlope();

    /**
     * Gets the output value for this node
     * @return The output value
     */
    public double getOutputValue();

    /**
     * Learns the slope value (a) of the parameterized ReLU by taking the current a and modifying it proportionally
     * to the fitness of the network
     */
    void slopeCalc();
}
