package hyperneat;

import AIinterfaces.ReusedCode;

import java.util.*;

/**
 * Class which represents the "brain" of an organism. It is a connection of nodes via links in
 * which values are passed through and a certain value is chosen as the "correct" output.
 * @author Chance Simmons and Brandon Townsend
 * @additions Brooke Kiser and Tyler McVeigh
 * @version 23 September 2020
 */
public class Network extends ReusedCode {
    /**
     * A static mapping of innovation numbers. These help in identifying similar links across
     * multiple networks during crossover.
     */
    private static final Map<Integer, String> innovationList = new HashMap<>();

    /** A list of all links in this network. */
    private final List<Link> links;

    /** A list of all input nodes in this network. No new input nodes should be added over time.*/
    private final Node[] inputNodes;

    /** A list of all output nodes in this network. No new output nodes should be added over time.*/
    private final Node[] outputNodes;

    /** A list of all hidden nodes in this network. This part can grow over time. */
    private final List<Node> hiddenNodes;

    /** A single bias node which should be connected to all non-input nodes. Helps with outputs. */
    private final Node biasNode;

    /** Counter to keep track of the number of nodes there are in our network. */
    private int numNodes;

    /** Counter to keep track of the number of current layers there are in our network. */
    private int numLayers;

    /** The fitness that the agent assigned to this network scored. */
    private int fitness;

    private final int network_id = new Random().nextInt(1000000);

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
        inputNodes = new Node[inputNum];
        outputNodes = new Node[outputNum];
        hiddenNodes = new ArrayList<>();
        biasNode = new Node(-1, numLayers);
        biasNode.setOutputValue(1);
        numNodes++;

        //Creates the input layer
        for(int i = 0; i < inputNum; i++) {
            inputNodes[i] = new Node(i, numLayers);
            numNodes++;
        }

        numLayers++;

        //Creates the output layer
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
    public Network(Network network) {
        this.numNodes = network.numNodes;
        this.numLayers = network.numLayers;
        this.fitness = network.fitness;
        this.links = new ArrayList<>();
        this.inputNodes = new Node[network.inputNodes.length];
        this.outputNodes = new Node[network.outputNodes.length];
        this.hiddenNodes = new ArrayList<>();
        this.biasNode = new Node(network.biasNode);

        //Copy the input layer into the new input layer
        for(int i = 0; i < network.inputNodes.length; i++) {
            this.inputNodes[i] = new Node(network.inputNodes[i]);
        }

        //Copy the hidden nodes into the new network
        for(Node node : network.hiddenNodes) {
            this.hiddenNodes.add(new Node(node));
        }

        //Copy the output layer into the new output layer
        for(int i = 0; i < network.outputNodes.length; i++) {
            this.outputNodes[i] = new Node(network.outputNodes[i]);
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
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Mutate a node in the hidden node list. The node will have a mutated parameterized slope if that activation
     * function is in this nodes phenotype. Otherwise the node will not express the mutation.
     */
    public void mutatePR(){
        int random = new Random().nextInt(hiddenNodes.size());
        //System.err.println("**** " + network_id + " ****");
        hiddenNodes.get(random).slopeCalc();
    }


    /**
     * Mutates this network, either with only link weights possibly being modified or by adding
     * additional structure via new links or new nodes.
     */
    public void mutate() {
        //todo add a toggle enabled mutation where the first disabled link encountered is toggled
        // back on.
        //System.err.println("Network mutate reached");
        Random random = new Random();
        // Mutation for link weight. Each link is either mutated or not each generation.
        for(Link link : links) {
            if(random.nextDouble() < Coefficients.LINK_WEIGHT_MUT.getValue()) {
                mutateWeight(link.getWeight());
            }
        }

        // Mutation for adding a link between two random, unlinked nodes.
        if(random.nextDouble() < Coefficients.ADD_LINK_MUT.getValue()) {
            addLinkMutation(this);
        }

        // Mutation for adding a new node where a link previously was.
        if(random.nextDouble() < Coefficients.ADD_NODE_MUT.getValue()) {
            addNodeMutation();
        }

        // Mutation for adding a new node where a link previously was.
        if(hiddenNodes.size() != 0 && random.nextDouble() < Coefficients.NODE_PR_MUT.getValue()) {
            mutatePR();
        }
    }

    /**
     * Returns whether or not a link can be formed between two nodes. If the nodes are already
     * connected, it is a bad link and if both nodes are from the same layer, it is a bad link.
     * @param node1 One of the nodes on the link.
     * @param node2 The other node on the link.
     * @return True if the future link is bad, false otherwise.
     */
    public boolean isBadLink(Node node1, Node node2) {
        return isConnectedTo(node1, node2) || node1.getLayer() == node2.getLayer();
    }

    /**
     * Adds a new node to our network where a random link used to be. Two new links appear
     * connecting the old input and old output to the new node. The old link gets disabled so it
     * can no longer be used to feed forward (which would bypass our hidden node, therefore
     * reducing its ability to change the network's output).
     */
    private void addNodeMutation() {
        Random random = new Random();
        Link link;
        do {
            link = links.get(random.nextInt(links.size()));
        } while(link.getInputNodeID() == biasNode.getId());

        addNode(this, link);
    }

    /**
     * Returns the node with the specified ID.
     * @param id The ID number to search by.
     * @return The node that corresponds to the ID number or null.
     */
    public Node getNode(int id) {
        for(Node node : listNodesByLayer(this)) {
            if(node.getId() == id) {
                return node;
            }
        }
        return null;
    }

    /**
     * Get the innovation list
     */
    public static Map<Integer, String> getInnovationList() {
        return innovationList;
    }

    /**
     * Get the hidden nodes of the network
     * @return List containing the hidden nodes
     */
    public List<Node> getHiddenNodes() {
        return hiddenNodes;
    }

    /**
     * Get the input nodes of the network
     * @return Array containing the input nodes
     */
    public Node[] getInputNodes() {
        return inputNodes;
    }

    /**
     * Get the output nodes of the network
     * @return Array containing the output nodes
     */
    public Node[] getOutputNodes() {
        return outputNodes;
    }

    /**
     * Increment the number of layers
     */
    public void incrementLayer(){
        numLayers++;
    }

    /**
     * Increment the total number of nodes
     */
    public void incrementNodes(){
        numNodes++;
    }

    /**
     * Gets the total of number of nodes
     * @return the number of nodes
     */
    public int getNumNodes() {
        return numNodes;
    }

    /**
     * Gets the bias node
     * @return The bias node
     */
    public Node getBiasNode() {
        return biasNode;
    }
}
