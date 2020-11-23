package AIinterfaces.NetworkIF;

import AIinterfaces.NodeIF.NEATNodeIF;
import java.util.List;

/**
 * This interface is a specific implementation of some methods for the NEAT species. This interfaces includes all the
 * methods in NetworkIF
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface NEATNetworkIF extends NetworkIF{

    /**
     * Get the hidden nodes of the network
     * @return List containing the hidden nodes
     */
    @Override
    List<NEATNodeIF> getHiddenNodes();

}
