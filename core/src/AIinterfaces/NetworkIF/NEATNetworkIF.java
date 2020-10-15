package AIinterfaces.NetworkIF;

import AIinterfaces.LinkIF;
import AIinterfaces.NodeIF.HNNodeIF;
import AIinterfaces.NodeIF.NEATNodeIF;

import java.util.List;
import java.util.Map;

public interface NEATNetworkIF extends NetworkIF{

    /**
     * Get the hidden nodes of the network
     * @return List containing the hidden nodes
     */
    @Override
    List<NEATNodeIF> getHiddenNodes();

}
