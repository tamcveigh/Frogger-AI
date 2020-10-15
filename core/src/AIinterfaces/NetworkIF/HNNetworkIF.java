package AIinterfaces.NetworkIF;

import AIinterfaces.NodeIF.HNNodeIF;
import AIinterfaces.NodeIF.NEATNodeIF;

import java.util.List;

public interface HNNetworkIF extends NetworkIF{

    /**
     * Get the hidden nodes of the network
     * @return List containing the hidden nodes
     */
    public List<HNNodeIF> getHiddenNodes();

}
