package neat;

import AIinterfaces.LinkIF;
import AIinterfaces.NodeIF;
import AIinterfaces.ReusedCode;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The node class contains all data needed by nodes to connect networks.
 * @author Chance Simmons and Brandon Townsend
 * @version 18 January 2020
 */
public class Node extends ReusedCode implements NodeIF {
    /** The input or bias layer should always been a value of 0. */
    private final static int INPUT_BIAS_LAYER = 0;

    /** The identification number for this node. */
    private final int id;

    /** The sum of inputs before the node is activated. */
    private double inputValue;

    /** The value generated after activation. */
    private double outputValue;

    /** List of all outgoing links. */
    private final List<LinkIF> outgoingLinks;

    /** The layer this node resides in. */
    private int layer;

    /**
     * Constructor for a node. Takes an identification number and layer for this node.
     * @param id The supplied identification number.
     * @param layer The supplied layer this node should reside in.
     */
    public Node(int id, int layer) {
        this.id = id;
        this.inputValue = 0.0;
        this.outputValue = 0.0;
        this.outgoingLinks = new ArrayList<>();
        this.layer = layer;
    }

    public Node(NodeIF node) {
        this.id = node.getId();
        this.inputValue = node.getInputValue();
        this.outputValue = node.getOutputValue();
        this.outgoingLinks = new ArrayList<>();
        this.layer = node.getLayer();
    }

    /**
     * Returns this nodes identification number.
     * @return This nodes identification number.
     */
    public int getId() {
        return id;
    }

    /**
     * Returns this nodes value before activation.
     * @return This nodes value before activation.
     */
    public double getInputValue() {
        return inputValue;
    }

    @Override
    public int getInputBiasLayer() {
        return 0;
    }

    /**
     * The activation function for this node
     * @return The activation number
     */
    public int getRandomActive() {
        return 0;
    }

    /**
     * Gets the slope of the activation function
     * @return The slope of the activation function
     */
    public double getSlope() {
        return 0.0;
    }

    /**
     * Sets this nodes input value to the supplied one.
     * @param inputValue The supplied input value.
     */
    public void setInputValue(double inputValue) {
        this.inputValue = inputValue;
    }

    /**
     * Returns the output value of this node.
     * @return The output value of this node.
     */
    public double getOutputValue() {
        return outputValue;
    }

    /**Learns the slope value (a) of the parameterized ReLU by taking the current a and modifying it proportionally
     * to the fitness of the network
     */
    public void slopeCalc() {}

    /**
     * Sets this nodes output value to the supplied one.
     * @param outputValue The supplied output value.
     */
    public void setOutputValue(double outputValue) {
        this.outputValue = outputValue;
    }

    /**
     * Returns this nodes outgoing links.
     * @return This nodes outgoing links.
     */
    public List<LinkIF> getOutgoingLinks() {
        return outgoingLinks;
    }

    /**
     * Returns the layer this node resides on.
     * @return The layer this node resides on.
     */
    public int getLayer() {
        return layer;
    }

    /**
     * Increments this nodes layer by 1.
     */
    public void incrementLayer() {
        this.layer++;
    }

    /**
     * Activates the node. Calls the activation function if this node is not on the bias or input
     * layer. Then, grabs each of the connected output nodes and send something to that output
     * nodes input.
     */
    public void activate() {
        if(layer != INPUT_BIAS_LAYER) {
            outputValue = activationFunctionS(inputValue);
        }

        for(LinkIF link : outgoingLinks) {
            if(link.isEnabled()) {
                NodeIF outputNode = link.getOutputNode();
                double oldInputValue = outputNode.getInputValue();
                outputNode.setInputValue(oldInputValue + link.getWeight() * outputValue);
            }
        }
    }

    /**
     * Helper function to call the activation function. Right now, it is a sigmoid function.
     * @param value The value to call the function on.
     * @return The value after the function has finished.
     */
    private double activationFunctionS(double value) {
        return 1.0 / (1.0 + Math.pow(Math.E, (-1 * value)));
    }

    /**
     * Returns whether or not this node's id number is the same as the supplied object.
     * @param obj The supplied object.
     * @return True if the supplied objects id number is the same as this nodes.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Node) {
            Node other = (Node) obj;
            return id == other.getId();
        }
        return false;
    }
}
