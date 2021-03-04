package AIinterfaces.NodeIF;

import AIinterfaces.LinkIF;

import java.util.List;

/**
 * This interface includes the overarching methods for all of the Species implementations
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface NEATNodeIF {

    /**
     * Returns this nodes identification number.
     * @return This nodes identification number.
     */
    int getId();

    /**
     * Returns the layer this node resides on.
     * @return The layer this node resides on.
     */
    int getLayer();

    /**
     * Sets this nodes output value to the supplied one.
     * @param outputValue The supplied output value.
     */
    void setOutputValue(double outputValue);

    /**
     * Activates the node. Calls the activation function if this node is not on the bias or input
     * layer. Then, grabs each of the connected output nodes and send something to that output
     * nodes input.
     */
    void activate();

    /**
     * Sets this nodes input value to the supplied one.
     * @param inputValue The supplied input value.
     */
    void setInputValue(double inputValue);

    /**
     * Returns this nodes outgoing links.
     * @return This nodes outgoing links.
     */
    List<LinkIF> getOutgoingLinks();

    /** Increments this nodes layer by 1. */
    void incrementLayer();

    /**
     * Returns this nodes value before activation.
     * @return This nodes value before activation.
     */
    double getInputValue();

    /**
     * Gets the output value for this node
     * @return The output value
     */
    double getOutputValue();
}
