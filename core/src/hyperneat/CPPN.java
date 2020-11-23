package hyperneat;

import AIinterfaces.LinkIF;
import AIinterfaces.NetworkIF.CPPNNetworkIF;
import AIinterfaces.NetworkIF.HNNetworkIF;
import AIinterfaces.NodeIF.HNNodeIF;
import AIinterfaces.NodeIF.NEATNodeIF;
import AIinterfaces.ReusedCode;
import java.util.List;
import java.util.Map;

/**
 * This class models a CPPN in the 4th dimension. This will set up the substrate and the CPPN network The substrate will
 * consist of a input and output size that is a parameter into the CPPN class. Within the CPPN class the substrate link
 * weights are also set since the substrate in a part of the CPPN itself.
 *
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public class CPPN extends ReusedCode implements CPPNNetworkIF {

    /** The size of the substrate. */
    private static final int SUBSTRATE_SIZE = 11;

    /** Array holding the weights between any two nodes on the substrate. */
    private final Substrate substrate;

    /** The input number of nodes for the substrate. */
    private final int inputSize;

    /** The output number of nodes for the substrate. */
    private final int outputSize;

    /** The CPPN network. */
    private HNNetworkIF CPPNFunction;

    /** The fitness of the CPPN. */
    private int fitness;

    /**
     * Constructor of the CPPN. Creates the substrate and network for the CPPN to run with.
     * @param inputSize the size of the square matrix of the substrate
     */
    public CPPN(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
        this.substrate = new Substrate(inputSize, outputSize, CPPN.SUBSTRATE_SIZE);
        //2 pairs of input points, 1 output weight
        this.CPPNFunction = new Network(4, 1);
        this.generateNetwork();
        this.fitness = 0;
    }

    /**
     * Creates a  copy of this CPPN
     * @return the cloned CPPN
     */
    @Override
    public CPPN clone() {
        CPPN clone = new CPPN(this.inputSize, this.outputSize);
        clone.CPPNFunction = new Network(this.CPPNFunction);
        clone.generateNetwork();
        return clone;
    }

    /**
     * Takes any two points on an array and feeds those points forward in the CPPN network to get an output
     * @param xOne x coordinate of the first point on the substrate
     * @param yOne y coordinate of the first point on the substrate
     * @param xTwo x coordinate of the second point on the substrate
     * @param yTwo y coordinate of the second point on the substrate
     * @return the weight of the connection between these two points
     */
    private double outputWeight(int xOne, int yOne, int xTwo, int yTwo) {
        float[] inputValues = {xOne, yOne, xTwo, yTwo};
        double[] outputArray = feedForward(CPPNFunction, inputValues);
        return outputArray[0];
    }

    /**
     * Function that will take the generated weights and create a Neural Network from them. This creates a 4 dimensional
     * CPPN with 4 substrates
     */
    private void generateNetwork() {
        //Create the node weights
        for (int i = 0; i < Coefficients.SUBSTRATE_SIZE.getValue(); i++) {
            for (int j = 0; j < Coefficients.SUBSTRATE_SIZE.getValue(); j++) {
                for (int k = 0; k < Coefficients.SUBSTRATE_SIZE.getValue(); k++) {
                    for (int l = 0; l < Coefficients.SUBSTRATE_SIZE.getValue(); l++) {
                        double output = this.outputWeight(i, j, k, l);
                        this.substrate.setLinkWeight(i, j, k, l, output);
                    }
                }
            }
        }//end nested loops

    }

    /** Mutates the CPPN network */
    public void mutate() {
        this.CPPNFunction.mutate();
        this.generateNetwork();
    }

    /**
     * Gets the CPPN network
     * @return This CPPN network
     */
    public HNNetworkIF getCPPNetwork() {
        return this.CPPNFunction;
    }

    /**
     * Feeds the network to get new output values
     * @param agentVision The array of what the agent can see
     * @return The array of the output values
     */
    @Deprecated
    public double[] runSubstrate(float[] agentVision) {
        return this.substrate.feedForward(agentVision);
    }

    /**
     * Gets the links in the CPPN
     * @return The CPPN Links
     */
    @Deprecated
    @Override
    public List<LinkIF> getLinks() {
        return CPPNFunction.getLinks();
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
     * @param fitness The new fitness for the CPPN
     */
    public void setFitness(int fitness) {
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

    /** Gets the hidden nodes of the network */
    @Deprecated
    @Override
    public List<HNNodeIF> getHiddenNodes() {
        return CPPNFunction.getHiddenNodes();
    }

    /** Determines if the link is bad */
    @Deprecated
    @Override
    public boolean isBadLink(NEATNodeIF node1, NEATNodeIF node2) {
        return false;
    }

    /** Is the class neat or HyperNEAT */
    @Deprecated
    @Override
    public boolean getType() {
        return true;
    }

    /** CPPN has no layer to increment */
    @Deprecated
    @Override
    public void incrementLayer() {}

    /**
     * Get the number of nodes in the CPPN network
     * @return The number of nodes
     */
    @Override
    public int getNumNodes() {
        return CPPNFunction.getNumNodes();
    }

    /** CPPN does not increment nodes as it has no nodes */
    @Deprecated
    @Override
    public void incrementNodes() {}

    /**
     * There is no innovation list
     * @return null because it doesn't exist
     */
    @Deprecated
    @Override
    public Map<Integer, String> getInnovationList() {
        return null;
    }

    /**
     * Gets the number of layers
     * @return the number of layers
     */
    @Deprecated
    @Override
    public int getNumLayers() {
        return CPPNFunction.getNumLayers();
    }

    /**
     * There is no node list to get the ID of
     * @param id The ID number to search by.
     * @return null because no node exists
     */
    @Deprecated
    @Override
    public NEATNodeIF getNode(int id) {
        return null;
    }

    /**
     * Get the input nodes
     * @return The network input nodes
     */
    @Override
    public NEATNodeIF[] getInputNodes() {
        return CPPNFunction.getInputNodes();
    }

    /**
     * Get the output nodes
     * @return The network output nodes
     */
    @Override
    public NEATNodeIF[] getOutputNodes() {
        return CPPNFunction.getOutputNodes();
    }

    /**
     * Get the network bias node
     * @return the bias node
     */
    @Override
    public NEATNodeIF getBiasNode() {
        return CPPNFunction.getBiasNode();
    }
}
