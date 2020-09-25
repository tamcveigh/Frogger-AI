package hyperneat;

import java.util.*;

/**
 * This models a substrate with input layer, output layer, or sandwich layers.
 * @author Brooke Kiser and Tyler McVeigh
 * @version 24 September 2020
 */
public class Substrate {

    /** Size of the substrate layers*/
    private final int substrateSize;

    /** A list of all input nodes in this network. No new input nodes should be added over time.*/
    private final List<Node> inputNodes;

    /** A list of all output nodes in this network. No new output nodes should be added over time.*/
    private final List<Node> outputNodes;

    /** A double array for the input layer */
    private final Node[][] inputLayer;

    /** A double array for the sandwich layer */
    private final Node[][] sandwichLayer;

    /** A double array for the output layer */
    private final Node[][] outputLayer;

    /**
     * Our network constructor. Builds an initial fully connected network of input and output nodes.
     * @param inputNum The number of input nodes to have.
     * @param outputNum The number of output nodes to have.
     */
    public Substrate(int inputNum, int outputNum, int substrateSize) {
        this.substrateSize = substrateSize;
        this.inputNodes = new ArrayList<Node>();
        this.outputNodes = new ArrayList<Node>();

        this.inputLayer = this.generateNodes(0);
        this.sandwichLayer = this.generateNodes(1);
        this.outputLayer = this.generateNodes(2);

        this.generateLinks(this.inputLayer, this.sandwichLayer);
        this.generateLinks(this.sandwichLayer, this.outputLayer);

        //Populate the input nodes from the input layer
        for(int i = 0; i < inputNum; i++){
            this.inputNodes.add(this.inputLayer[0][i]);
        }

        //Populate the output nodes from the output layer
        for(int i = 0; i < outputNum; i++){
            this.outputNodes.add(this.outputLayer[this.substrateSize - 1][i]);
        }
    }

    /**
     * Populates a layer of the substrate with nodes
     * @param layerNum The number responding to the layer
     * @return The double array containing the populated layer
     */
    private Node[][] generateNodes(int layerNum) {
        Node[][] layerNodes = new Node[this.substrateSize][this.substrateSize];
        int id = 0;
        for(int i = 0; i < this.substrateSize; i++){
            for(int j = 0; j < this.substrateSize; j++){
                layerNodes[i][j] = new Node(id, layerNum);
                id++;
            }
        }
        return layerNodes;
    }

    /**
     * Create the links between all the layers. The weight of the links will initially be set to 0. This will
     * set up the links between the input and outgoing nodes in the substrate.
     * @param inputLayer The layer with the input nodes
     * @param outgoingLayer The output layer to set to the input layer
     */
    private void generateLinks(Node[][] inputLayer, Node[][] outgoingLayer){
        int innoNum = 0;
        for(int i = 0; i < this.substrateSize; i++){
            for(int j = 0; j < this.substrateSize; j++){
                for(int k = 0; k < this.substrateSize; k++){
                    for(int l = 0; l < this.substrateSize; l++){
                        Link link = new Link(innoNum++, inputLayer[i][j].getId(),
                                outgoingLayer[k][l], 0);
                        inputLayer[i][j].addLink(link);
                    }
                }
            }
        }//end nested loops
    }

    /**
     * Connects an input node to an output node by a link of a determined link weight.
     * @param inputX The first value of the input node
     * @param inputY The second value of the input node
     * @param outX The first value of the output node
     * @param outY The second value of the output node
     * @param weight The weight of the link
     */
    public void setLinkWeight(int inputX, int inputY, int outX, int outY, double weight){
        Node inputNode = this.inputLayer[inputX][inputY];
        Node sandwichFromIn = this.sandwichLayer[outX][outY];
        Node sandwichToOut = this.sandwichLayer[inputX][inputY];
        Node outputNode = this.outputLayer[outX][outY];

        Link link;
        boolean found = false;
        List<Link> links = inputNode.getOutgoingLinks();
        for(int i = 0; i < links.size() && !found; i++){
            link = links.get(i);
            if (link.getOutputNode().equals(sandwichFromIn)){
                found = true;
                link.setWeight(weight);
            }
        }

        found = false;
        links = sandwichToOut.getOutgoingLinks();
        for(int i = 0; i < links.size() && !found; i++){
            link = links.get(i);
            if (link.getOutputNode().equals(outputNode)){
                found = true;
                link.setWeight(weight);
            }
        }
    }

    /**
     * Activates every node in the network in a certain specified order. Should traverse from
     * input -> bias -> hidden -> output.
     * @param inputValues The values to be set as our input layer nodes' output values.
     * @return The output values in our output nodes after every node has been activated.
     */
    public double[] feedForward(float[] inputValues) {
        // Set the output values of our input nodes to the supplied input values.
        for (int i = 0; i < inputNodes.size(); i++) {
            inputNodes.get(i).setOutputValue(inputValues[i]);
        }

        // Activate the nodes in order from input node -> bias -> hidden -> output.
        for (Node[] nodes : inputLayer) {
            for (Node node : nodes) {
                node.activate();
            }
        }

        for (Node[] nodes : sandwichLayer){
            for( Node node: nodes){
                node.activate();
            }
        }

        for (Node[] nodes: outputLayer){
            for( Node node : nodes){
                node.activate();
            }
        }

        // Write the output values to a double array to pass back as the decisions of this network.
        double[] outputs = new double[outputNodes.size()];
        for (int i = 0; i < outputNodes.size(); i++) {
            outputs[i] = (outputNodes.get(i).getOutputValue());
        }

        // Set all input values back to 0 for the next feed forward.
        for (Node[] nodes : inputLayer){
            for (Node node : nodes) {
                node.setInputValue(0);
            }
        }

        for (Node[] nodes : sandwichLayer){
            for (Node node : nodes) {
                node.setInputValue(0);
            }
        }

        for (Node[] nodes : outputLayer){
            for (Node node : nodes) {
                node.setInputValue(0);
            }
        }
        return outputs;
    }

}
