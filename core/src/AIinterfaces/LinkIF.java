package AIinterfaces;

import AIinterfaces.NodeIF.NEATNodeIF;

/**
 * This interface allows for the link classes to be interchangeable
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface LinkIF {

    /**
     * Returns this links input node ID.
     * @return This links input node ID.
     */
    int getInputNodeID();

    /**
     * Returns this links output node.
     * @return This links output node.
     */
    NEATNodeIF getOutputNode();

    /**
     * Returns whether or not this link is enabled.
     * @return True if enabled, false otherwise.
     */
    boolean isEnabled();

    /**
     * Returns this links weight.
     * @return This links weight.
     */
    double getWeight();

    /**
     * Returns this links innovation number.
     * @return This links innovation number.
     */
    int getInnovationNum();

    /**
     * Sets this links weight to the supplied weight.
     * @param weight The weight to set this link to.
     */
    void setWeight(double weight);

    /**
     * Sets whether or not this link is enabled.
     * @param enabled True if this link should be enabled, false if it should be disabled.
     */
    void setEnabled(boolean enabled);
}
