package AIinterfaces;

import AIinterfaces.NodeIF.NEATNodeIF;

public interface LinkIF {

    /**
     * Returns this links input node ID.
     * @return This links input node ID.
     */
    public int getInputNodeID();

    /**
     * Returns this links output node.
     * @return This links output node.
     */
    public NEATNodeIF getOutputNode();

    /**
     * Returns whether or not this link is enabled.
     * @return True if enabled, false otherwise.
     */
    public boolean isEnabled();

    /**
     * Returns this links weight.
     * @return This links weight.
     */
    public double getWeight();

    /**
     * Returns this links innovation number.
     * @return This links innovation number.
     */
    public int getInnovationNum();

    /**
     * Sets this links weight to the supplied weight.
     * @param weight The weight to set this link to.
     */
    public void setWeight(double weight);

    /**
     * Sets whether or not this link is enabled.
     * @param enabled True if this link should be enabled, false if it should be disabled.
     */
    public void setEnabled(boolean enabled);
}
