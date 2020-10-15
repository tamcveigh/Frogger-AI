package AIinterfaces.SpeciesIF;

import AIinterfaces.NetworkIF.NEATNetworkIF;

import java.util.Map;

public interface NEATSpeciesIF extends SpeciesIF{

    /**
     * Returns the mapping of agent IDs and their networks.
     * @return The mapping of agent IDs and their networks.
     */
    public Map<Integer, NEATNetworkIF> getOrganisms();

    NEATNetworkIF reproduce();

    void addOrganism(int agentID, NEATNetworkIF agentNetwork);


    NEATNetworkIF getCompatibilityNetwork();
}
