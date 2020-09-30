package hyperneat;

import AIinterfaces.LinkIF;
import AIinterfaces.NetworkIF;
import AIinterfaces.NodeIF;
import AIinterfaces.ReusedCode;

import java.util.List;
import java.util.Map;

/**
 * This class models a CPPN in the 4th dimension. This will set up the substrate and the CPPN network
 * The substrate will consist of a input and output size that is a parameter into the CPPN class. Within
 * the CPPN class the substrate link weights are also set since the substrate in a part of the CPPN itself.
 *
 * @author Brooke Kiser and Tyler McVeigh
 * @version 23 September 2020
 */
public class CPPN extends ReusedCode implements NetworkIF{

    /** Array holding the weights between any two nodes on the substrate. */
    private final Substrate substrate;

    /** The CPPN network. */
    private NetworkIF CPPNFunction;

    /** The input number of nodes for the substrate. */
    private final int inputSize;

    /** The output number of nodes for the substrate. */
    private final int outputSize;

    /** The size of the substrate . */
    private static final int SUBSTRATE_SIZE = 11;

    /** The fitness of the CPPN. */
    private int fitness;

    /**
     * Constructor of the CPPN. Creates the substrate and network for the CPPN to run with.
     * @param inputSize the size of the square matrix of the substrate
     */
    public CPPN(int inputSize, int outputSize){
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.substrate = new Substrate(inputSize, outputSize, CPPN.SUBSTRATE_SIZE);
        //2 pairs of input points, 1 output weight
        this.CPPNFunction = new Network(4,1);
        this.generateNetwork();
        this.fitness = 0;
    }

    /**
     * Creates a  copy of this CPPN
     * @return the cloned CPPN
     */
    @Override
    public CPPN clone(){
        CPPN clone = new CPPN(this.inputSize,this.outputSize);
        clone.CPPNFunction = new Network(this.CPPNFunction);
        clone.generateNetwork();
        return clone;
    }

    /**
     * Takes any two points on an array and feeds those points forward in the CPPN network
     * to get an output
     *
     * @param xOne x coordinate of the first point on the substrate
     * @param yOne y coordinate of the first point on the substrate
     * @param xTwo x coordinate of the second point on the substrate
     * @param yTwo y coordinate of the second point on the substrate
     * @return the weight of the connection between these two points
     */
    private double outputWeight( int xOne, int yOne, int xTwo, int yTwo){
        float[] inputValues = { xOne, yOne, xTwo, yTwo };
        double[] outputArray = feedForward(CPPNFunction, inputValues);
        return outputArray[0];
    }

    /**
     * Function that will take the generated weights and create a Neural Network from them. This creates
     * a 4 dimensional CPPN with 4 substrates
     */
    private void generateNetwork(){
        //Create the node weights
        for(int i = 0; i < Coefficients.SUBSTRATE_SIZE.getValue(); i++){
            for(int j = 0; j < Coefficients.SUBSTRATE_SIZE.getValue(); j++){
                for(int k = 0; k < Coefficients.SUBSTRATE_SIZE.getValue(); k++){
                    for(int l = 0; l < Coefficients.SUBSTRATE_SIZE.getValue(); l++){
                        double output  = this.outputWeight(i,j,k,l);
                        this.substrate.setLinkWeight(i,j,k,l,output);
                    }
                }
            }
        }//end nested loops

    }

    /**
     * Mutates the CPPN network
     */
    public void mutate(){
        this.CPPNFunction.mutate();
        this.generateNetwork();
    }

    /**
     * Gets the CPPN network
     * @return This CPPN network
     */
    public NetworkIF getCPPNetwork(){
        return this.CPPNFunction;
    }

    /**
     * Feeds the network to get new output values
     * @param agentVision The array of what the agent can see
     * @return The array of the output values
     */
    public double[] runSubstrate(float[] agentVision) {
        return this.substrate.feedForward(agentVision);
    }

    /**
     * Gets the fitness of this CPPN
     * @return The fitness of the CPPN
     */
    public int getFitness() {
        return this.fitness;
    }

    /**
     * Sets the fitness for the CPPN
     *
     * @param fitness The new fitness for the CPPN
     */
    public void setFitness(int fitness){
        this.fitness = fitness;
    }

    /**
     * Crossover a baby CPPN with this CPPN and another parent
     * @param otherParent The second parent to make the baby
     * @return The baby of the two parent CPPNs
     */
    public CPPN crossover(CPPN otherParent) {
        CPPN baby = this.clone();
        crossover(otherParent.CPPNFunction, baby.CPPNFunction);
        return baby;
    }

    /**
     * Returns the node with the specified ID.
     *
     * @param id The ID number to search by.
     *
     * @return The node that corresponds to the ID number or null.
     */
    @Override
    public NodeIF getNode(int id) {
        return null;
    }

    /**
     * Returns the list of links that this network holds.
     *
     * @return The list of links that this network holds.
     */
    @Override
    public List<LinkIF> getLinks() {
        return null;
    }

    /**
     * Get the innovation list
     */
    @Override
    public Map<Integer, String> getInnovationList() {
        return null;
    }

    /**
     * Get the input nodes of the network
     *
     * @return Array containing the input nodes
     */
    @Override
    public NodeIF[] getInputNodes() {
        return new NodeIF[0];
    }

    /**
     * Get the output nodes of the network
     *
     * @return Array containing the output nodes
     */
    @Override
    public NodeIF[] getOutputNodes() {
        return new NodeIF[0];
    }

    /**
     * Get the hidden nodes of the network
     *
     * @return List containing the hidden nodes
     */
    @Override
    public List<NodeIF> getHiddenNodes() {
        return null;
    }

    /**
     * Increment the number of layers
     */
    @Override
    public void incrementLayer() {

    }

    /**
     * Gets the total of number of nodes
     *
     * @return the number of nodes
     */
    @Override
    public int getNumNodes() {
        return 0;
    }

    /**
     * Returns whether or not a link can be formed between two nodes. If the nodes are already connected, it is a bad
     * link and if both nodes are from the same layer, it is a bad link.
     *
     * @param node1 One of the nodes on the link.
     * @param node2 The other node on the link.
     *
     * @return True if the future link is bad, false otherwise.
     */
    @Override
    public boolean isBadLink(NodeIF node1, NodeIF node2) {
        return false;
    }

    /**
     * Increment the total number of nodes
     */
    @Override
    public void incrementNodes() {

    }

    /**
     * Gets the bias node
     *
     * @return The bias node
     */
    @Override
    public NodeIF getBiasNode() {
        return null;
    }

    /**
     * Gets the total number of layers
     *
     * @return The number of layers
     */
    @Override
    public int getNumLayers() {
        return 0;
    }

    @Override
    public NetworkIF getCompatibilityNetwork() {
        return null;
    }

    @Override
    public Integer getBestOrgID() {
        return null;
    }

    @Override
    public void setCompatibilityNetwork() {

    }
}
