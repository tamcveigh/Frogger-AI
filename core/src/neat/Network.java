package neat;

import AIinterfaces.*;
import AIinterfaces.NetworkIF.HNNetworkIF;
import AIinterfaces.NetworkIF.NEATNetworkIF;
import AIinterfaces.NodeIF.HNNodeIF;
import AIinterfaces.NodeIF.NEATNodeIF;

import java.util.*;

/**
 * Class which represents the "brain" of an organism. It is a connection of nodes via links in
 * which values are passed through and a certain value is chosen as the "correct" output.
 * @author Chance Simmons and Brandon Townsend
 * @version 21 January 2020
 */
public class Network extends ReusedCode implements NEATNetworkIF {
    /**
     * A static mapping of innovation numbers. These help in identifying similar links across
     * multiple networks during crossover.
     */
    private static final Map<Integer, String> innovationList = new HashMap<>();

    /** A list of all links in this network. */
    private final List<LinkIF> links;

    /** A list of all input nodes in this network. No new input nodes should be added over time.*/
    private final NEATNodeIF[] inputNodes;

    /** A list of all output nodes in this network. No new output nodes should be added over time.*/
    private final NEATNodeIF[] outputNodes;

    /** A list of all hidden nodes in this network. This part can grow over time. */
    private List<NEATNodeIF> hiddenNodes;

    /** A single bias node which should be connected to all non-input nodes. Helps with outputs. */
    private final NEATNodeIF biasNode;

    /** Counter to keep track of the number of nodes there are in our network. */
    private int numNodes;

    /** Counter to keep track of the number of current layers there are in our network. */
    private int numLayers;

    /** The fitness that the agent assigned to this network scored. */
    private int fitness;

    private boolean type = false;

    /**
     * Our network constructor. Builds an initial fully connected network of input and output nodes.
     * @param inputNum The number of input nodes to have.
     * @param outputNum The number of output nodes to have.
     */
    public Network(int inputNum, int outputNum) {
        numNodes = 0;
        numLayers = 0;
        fitness = 0;
        links = new ArrayList<>();
        inputNodes = new NEATNodeIF[inputNum];
        outputNodes = new NEATNodeIF[outputNum];
        hiddenNodes = new ArrayList<>();
        biasNode = new Node(-1, numLayers);
        biasNode.setOutputValue(1);
        numNodes++;

        for(int i = 0; i < inputNum; i++) {
            inputNodes[i] = new Node(i, numLayers);
            numNodes++;
        }

        numLayers++;

        for(int i = 0; i < outputNum; i++) {
            // Our initial output layer is 1 since it is the layer specifically behind our input.
            // If we add a node in the hidden layer, our output layer should grow.
            outputNodes[i] = new Node(inputNodes.length + i, numLayers);
            numNodes++;
        }

        // Links our input nodes to output nodes and attaches the bias node to each output node.
        generateNetwork(this, inputNodes, outputNodes, biasNode);
    }

    /**
     * Copy constructor used to deep copy a network.
     * @param network The network to copy.
     */
    public Network(NEATNetworkIF network) {
        this.numNodes = network.getNumNodes();
        this.numLayers = network.getNumLayers();
        this.fitness = network.getFitness();
        this.links = new ArrayList<>();
        this.inputNodes = new Node[network.getInputNodes().length];
        this.outputNodes = new Node[network.getOutputNodes().length];
        this.hiddenNodes = new ArrayList<>();
        this.biasNode = new Node(network.getBiasNode());
        for(int i = 0; i < network.getInputNodes().length; i++) {
            this.inputNodes[i] = new Node(network.getInputNodes()[i]);
        }
        for(NEATNodeIF node : network.getHiddenNodes()) {
            this.hiddenNodes.add(new Node(node));
        }
        for(int i = 0; i < network.getOutputNodes().length; i++) {
            this.outputNodes[i] = new Node(network.getOutputNodes()[i]);
        }
        copyLinks(network, this);
    }

    /**
     * Returns this network's fitness.
     * @return This network's fitness.
     */
    public int getFitness() {
        return fitness;
    }

    /**
     * Sets this network's fitness to the supplied value.
     * @param fitness The supplied value to overwrite this network's fitness.
     */
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    /**
     * Returns the list of links that this network holds.
     * @return The list of links that this network holds.
     */
    public List<LinkIF> getLinks() {
        return links;
    }

    /**
     * Mutates this network, either with only link weights possibly being modified or by adding
     * additional structure via new links or new nodes.
     */
    public void mutate() {
        //todo add a toggle enabled mutation where the first disabled link encountered is toggled
        // back on.

        // Mutation for link weight. Each link is either mutated or not each generation.
        for(LinkIF link : links) {
            if(Math.random() < Coefficients.LINK_WEIGHT_MUT.getValue()) {
                mutateWeight(link.getWeight());
            }
        }

        // Mutation for adding a link between two random, unlinked nodes.
        if(Math.random() < Coefficients.ADD_LINK_MUT.getValue()) {
            addLinkMutation(this);
        }

        // Mutation for adding a new node where a link previously was.
        if(Math.random() < Coefficients.ADD_NODE_MUT.getValue()) {
            addNodeMutation();
        }
    }

    /**
     * Returns whether or not a link can be formed between two nodes. If the nodes are already
     * connected, it is a bad link and if both nodes are from the same layer, it is a bad link.
     * @param node1 One of the nodes on the link.
     * @param node2 The other node on the link.
     * @return True if the future link is bad, false otherwise.
     */
    public boolean isBadLink(NEATNodeIF node1, NEATNodeIF node2) {
        return isConnectedTo(node1, node2) || node1.getLayer() == node2.getLayer();
    }

    @Override
    public boolean getType() {
        return type;
    }

    /**
     * Adds a new node to our network where a random link used to be. Two new links appear
     * connecting the old input and old output to the new node. The old link gets disabled so it
     * can no longer be used to feed forward (which would bypass our hidden node, therefore
     * reducing its ability to change the network's output).
     */
    private void addNodeMutation() {
        Random random = new Random();
        LinkIF link;
        do {
            link = links.get(random.nextInt(links.size()));
        } while(link.getInputNodeID() == biasNode.getId());

        addNode(link);
    }

    /**
     * Returns the node with the specified ID.
     * @param id The ID number to search by.
     * @return The node that corresponds to the ID number or null.
     */
    public HNNodeIF getNode(int id) {
        for(NEATNodeIF node : listNodesByLayer(this)) {
            if(node.getId() == id) {
                return (HNNodeIF) node;
            }
        }
        return null;
    }

    /**
     * Get the innovation list
     */
    @Override
    public Map<Integer, String> getInnovationList() {
        return innovationList;
    }

    /**
     * Get the input nodes of the network
     *
     * @return Array containing the input nodes
     */
    @Override
    public NEATNodeIF[] getInputNodes() {
        return inputNodes;
    }

    /**
     * Get the output nodes of the network
     *
     * @return Array containing the output nodes
     */
    @Override
    public NEATNodeIF[] getOutputNodes() {
        return outputNodes;
    }

    /**
     * Get the hidden nodes of the network
     *
     * @return List containing the hidden nodes
     */
    @Override
    public List<NEATNodeIF> getHiddenNodes() {
        return hiddenNodes;
    }

    /**
     * Increment the number of layers
     */
    @Override
    public void incrementLayer() {
        numLayers++;
    }

    /**
     * Gets the total of number of nodes
     *
     * @return the number of nodes
     */
    @Override
    public int getNumNodes() {
        return numNodes;
    }

    /**
     * Increment the total number of nodes
     */
    @Override
    public void incrementNodes() {
        numNodes++;
    }

    /**
     * Gets the bias node
     *
     * @return The bias node
     */
    @Override
    public HNNodeIF getBiasNode() {
        return (HNNodeIF) biasNode;
    }

    /**
     * Gets the total number of layers
     *
     * @return The number of layers
     */
    @Override
    public int getNumLayers() {
        return numLayers;
    }

    /**
     * Adds a node where the specified link used to be.
     * @param link The location where the new node should be added.
     */
    public void addNode(LinkIF link) {
        link.setEnabled(false);
        NEATNodeIF oldInput = getNode(link.getInputNodeID());
        int layer = addNodeHelper(this, link, oldInput);
        NEATNodeIF toAdd = new neat.Node(getNumNodes(), layer);
        toAdd.activate();
        getHiddenNodes().add(toAdd);

        // Now add links to either side of the new node. The link going from the old input to the
        // new node gets a weight of 1 while the link going from the new node to the old output
        // receives the weight of the now-disabled link.
        addLink(this, oldInput, toAdd, 1);
        addLink(this, toAdd, link.getOutputNode(), link.getWeight());

        // Finally connect our bias node with a weight of 0 to minimize the bias' initial impact.
        addLink(this, getBiasNode(), toAdd, 0);
    }
}
