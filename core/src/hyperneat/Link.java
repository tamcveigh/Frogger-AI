package hyperneat;


import AIinterfaces.LinkIF;
import AIinterfaces.NodeIF.NEATNodeIF;

/**
 * Represents a connection between two nodes in our network. Each connection is given an innovation number which assists
 * in crossover between two separate networks.
 *
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public class Link implements LinkIF {
    /** The innovation number assigned to this link. */
    private final int innovationNum;

    /** The input node this link is connected to. */
    private final int inputNodeID;

    /** The output node this link is connected to. */
    private final NEATNodeIF outputNode;

    /** The weight that is assigned to this link. Should be between -1 and 1. */
    private double weight;

    /** Represents whether this link is enabled in our network or not. */
    private boolean enabled;

    /**
     * Constructor for links. Accepts an innovation number, the identification numbers of the input and output nodes,
     * and generates a new weight between -1 and 1.
     * @param innovationNum The supplied innovation number.
     * @param inputNodeID   The supplied id number for this links input node.
     * @param outputNode    The supplied node for this links output node.
     */
    public Link(int innovationNum, int inputNodeID, NEATNodeIF outputNode, double weight) {
        this.innovationNum = innovationNum;
        this.inputNodeID = inputNodeID;
        this.outputNode = outputNode;
        this.weight = weight;
        this.enabled = true;
    }

    /**
     * Returns this links innovation number.
     * @return This links innovation number.
     */
    public int getInnovationNum() {
        return innovationNum;
    }

    /**
     * Returns this links input node ID.
     * @return This links input node ID.
     */
    public int getInputNodeID() {
        return inputNodeID;
    }

    /**
     * Returns this links output node.
     * @return This links output node.
     */
    public NEATNodeIF getOutputNode() {
        return outputNode;
    }

    /**
     * Returns this links weight.
     * @return This links weight.
     */
    public double getWeight() {
        return weight;
    }

    /**
     * Sets this links weight to the supplied weight.
     * @param weight The weight to set this link to.
     */
    public void setWeight(double weight) {
        this.weight = weight;
    }

    /**
     * Returns whether or not this link is enabled.
     * @return True if enabled, false otherwise.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether or not this link is enabled.
     * @param enabled True if this link should be enabled, false if it should be disabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Checks to see if a supplied object is the same link as this one.
     * @param obj The object to check equality against.
     * @return True if the object is a link and the innovation numbers are the same, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Link) {
            Link other = (Link) obj;
            return other.getInnovationNum() == innovationNum;
        }
        return false;
    }
}
