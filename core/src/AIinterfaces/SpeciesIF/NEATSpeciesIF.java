package AIinterfaces.SpeciesIF;

import AIinterfaces.NetworkIF.NEATNetworkIF;

import java.util.Map;

/**
 * This interface is a specific implementation of some methods for the NEAT species. This interfaces includes all the
 * methods in SpeciesIF
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface NEATSpeciesIF extends SpeciesIF{

    /**
     * Returns the mapping of agent IDs and their networks.
     * @return The mapping of agent IDs and their networks.
     */
    Map<Integer, NEATNetworkIF> getOrganisms();

    /**
     * Get the compatible network for the species
     * @return The compatible network
     */
    NEATNetworkIF reproduce();

    /**
     * Add an organism to the species
     * @param agentID The agent id
     * @param agentNetwork The network for the cat
     */
    void addOrganism(int agentID, NEATNetworkIF agentNetwork);

    /**
     * Get the compatible network for the species
     * @return The compatible network
     */
    NEATNetworkIF getCompatibilityNetwork();
}
