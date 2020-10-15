package AIinterfaces.NetworkIF;

import AIinterfaces.LinkIF;
import AIinterfaces.NodeIF.HNNodeIF;
import AIinterfaces.NodeIF.NEATNodeIF;

import java.util.List;
import java.util.Map;

public interface NetworkIF {

    /*getFitness

    getInnovationList

    getLinks

    getNumLayers

    getNumeNode

    incrementLayer

    incrementNodes

    mutate

    setFitness*/

    /**
     * Returns the list of links that this network holds.
     * @return The list of links that this network holds.
     */
    public List<LinkIF> getLinks();

    /**
     * Returns this network's fitness.
     * @return This network's fitness.
     */
    public int getFitness();

    void mutate();

    /**
     *
     * @param fitness
     */
    void setFitness(int fitness);

    /**
     * Increment the number of layers
     */
    public void incrementLayer();

    /**
     * Gets the total of number of nodes
     * @return the number of nodes
     */
    public int getNumNodes();

    /**
     * Increment the total number of nodes
     */
    public void incrementNodes();

    /**
     * Get the innovation list
     */
    public Map<Integer, String> getInnovationList();

    /**
     * Gets the total number of layers
     * @return The number of layers
     */
    public int getNumLayers();

    /**
     * Returns the node with the specified ID.
     * @param id The ID number to search by.
     * @return The node that corresponds to the ID number or null.
     */
    public NEATNodeIF getNode(int id);

    /**
     * Get the input nodes of the network
     * @return Array containing the input nodes
     */
    public NEATNodeIF[] getInputNodes();

    /**
     * Get the output nodes of the network
     * @return Array containing the output nodes
     */
    public NEATNodeIF[] getOutputNodes();

    /**
     * Gets the bias node
     * @return The bias node
     */
    public NEATNodeIF getBiasNode();

    /**
     * Get the hidden nodes of the network
     * @return List containing the hidden nodes
     */
    public List<? extends NEATNodeIF> getHiddenNodes();


    /**
     * Returns whether or not a link can be formed between two nodes. If the nodes are already
     * connected, it is a bad link and if both nodes are from the same layer, it is a bad link.
     * @param node1 One of the nodes on the link.
     * @param node2 The other node on the link.
     * @return True if the future link is bad, false otherwise.
     */
    public boolean isBadLink(NEATNodeIF node1, NEATNodeIF node2);

}
