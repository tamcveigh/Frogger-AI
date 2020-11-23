package AIinterfaces.NetworkIF;

import AIinterfaces.LinkIF;
import AIinterfaces.NodeIF.NEATNodeIF;
import java.util.List;
import java.util.Map;

/**
 * This interface is a broad overview for all methods the implementing interfaces also need.
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface NetworkIF {

    /**
     * Returns the list of links that this network holds.
     * @return The list of links that this network holds.
     */
    List<LinkIF> getLinks();

    /**
     * Returns this network's fitness.
     * @return This network's fitness.
     */
    int getFitness();

    /**
     * Mutates this network, either with only link weights possibly being modified or by adding additional structure via
     * new links or new nodes.
     */
    void mutate();

    /**
     * Set the new fitness
     * @param fitness the new fitness
     */
    void setFitness(int fitness);

    /** Increment the number of layers */
    void incrementLayer();

    /**
     * Gets the total of number of nodes
     * @return the number of nodes
     */
    int getNumNodes();

    /** Increment the total number of nodes */
    void incrementNodes();

    /** Get the innovation list */
    Map<Integer, String> getInnovationList();

    /**
     * Gets the total number of layers
     * @return The number of layers
     */
    int getNumLayers();

    /**
     * Returns the node with the specified ID.
     * @param id The ID number to search by.
     * @return The node that corresponds to the ID number or null.
     */
    NEATNodeIF getNode(int id);

    /**
     * Get the input nodes of the network
     * @return Array containing the input nodes
     */
    NEATNodeIF[] getInputNodes();

    /**
     * Get the output nodes of the network
     * @return Array containing the output nodes
     */
    NEATNodeIF[] getOutputNodes();

    /**
     * Gets the bias node
     * @return The bias node
     */
    NEATNodeIF getBiasNode();

    /**
     * Get the hidden nodes of the network
     * @return List containing the hidden nodes
     */
    List<? extends NEATNodeIF> getHiddenNodes();


    /**
     * Returns whether or not a link can be formed between two nodes. If the nodes are already
     * connected, it is a bad link and if both nodes are from the same layer, it is a bad link.
     * @param node1 One of the nodes on the link.
     * @param node2 The other node on the link.
     * @return True if the future link is bad, false otherwise.
     */
    boolean isBadLink(NEATNodeIF node1, NEATNodeIF node2);

    /**
     * Get which type of AI is being used
     * @return True if HyperNEAT and false if NEAT
     */
    boolean getType();
}
