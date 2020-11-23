package AIinterfaces.NetworkIF;

/**
 * This interface is a specific implementation of some methods for the CPPN networks. This interfaces includes all the
 * methods in HNNetworkIF
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface CPPNNetworkIF extends HNNetworkIF{

    /**
     * Feedforward the vision through the network
     * @param agentVision the cats vision
     * @return Array of output values
     */
    double[] runSubstrate(float[] agentVision);

    /**
     * Get the network for this CPPN
     * @return The network
     */
    HNNetworkIF getCPPNetwork();

    /**
     * Perform a deep clone of the CPPN network
     * @return the cloned network
     */
    CPPNNetworkIF clone();

    /**
     * Set the fitness of the CPPN network
     * @param fitness The new fitness
     */
    void setFitness(int fitness);
}
