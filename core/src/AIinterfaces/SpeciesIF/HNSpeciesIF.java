package AIinterfaces.SpeciesIF;

import AIinterfaces.NetworkIF.CPPNNetworkIF;
import AIinterfaces.NetworkIF.HNNetworkIF;

import java.util.Map;

public interface HNSpeciesIF extends SpeciesIF{

    Map<Integer, CPPNNetworkIF> getOrganisms();

    int size();

    CPPNNetworkIF getCompatibilityNetwork();

    CPPNNetworkIF reproduce();

    void addOrganism(int agentID, CPPNNetworkIF agentNetwork);

}
