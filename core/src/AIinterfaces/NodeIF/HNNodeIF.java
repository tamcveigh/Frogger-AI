package AIinterfaces.NodeIF;

public interface HNNodeIF extends NEATNodeIF{
    /**
     * The activation function for this node
     * @return The activation number
     */
    int getRandomActive();

    /**
     * Gets the slope of the activation function
     * @return The slope of the activation function
     */
    double getSlope();

    /**
     * Learns the slope value (a) of the parameterized ReLU by taking the current a and modifying it proportionally
     * to the fitness of the network
     */
    void slopeCalc();

    /**
     * Gets the input bias layer
     * @return The bias node input layer
     */
    public int getInputBiasLayer();
}
