package AIinterfaces.SpeciesIF;

import AIinterfaces.NetworkIF.CPPNNetworkIF;
import AIinterfaces.NetworkIF.HNNetworkIF;

import java.util.Map;

/**
 * This interface is a specific implementation of some methods for the HyperNEAT species. This interfaces includes all the
 * methods in SpeciesIF
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface HNSpeciesIF extends SpeciesIF{

    /**
     * Get the organisms for this species
     * @return A map containing all of the organisms
     */
    Map<Integer, CPPNNetworkIF> getOrganisms();

    /**
     * The number of organisms
     * @return The number of organisms
     */
    int size();

    /**
     * Get the compatible network for the species
     * @return The compatible network
     */
    HNNetworkIF getCompatibilityNetwork();

    /**
     * The organisms will reproduce if they survive the cull
     * @return The network for the baby
     */
    CPPNNetworkIF reproduce();

    /**
     * Add an organism to the species
     * @param agentID The agent id
     * @param agentNetwork The network for the cat
     */
    void addOrganism(int agentID, CPPNNetworkIF agentNetwork);

}
