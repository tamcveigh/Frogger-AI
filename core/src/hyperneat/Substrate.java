package hyperneat;

import java.util.*;

public class Substrate {

    /** Size of the substrate layers*/
    private int substrateSize;

    /** A list of all input nodes in this network. No new input nodes should be added over time.*/
    private List<Node> inputNodes;

    /** A list of all output nodes in this network. No new output nodes should be added over time.*/
    private List<Node> outputNodes;

    private Node[][] inputLayer;

    private Node[][] sandwichLayer;

    private Node[][] outputLayer;

    /** Counter to keep track of the number of nodes there are in our network. */
    private int numNodes;

    /** Constant to keep track of the number of current layers there are in our network. */
    private final int numLayers = 3;

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

        for(int i = 0; i < inputNum; i++){
            this.inputNodes.add(this.inputLayer[0][i]);
        }

        for(int i = 0; i < outputNum; i++){
            this.outputNodes.add(this.outputLayer[this.substrateSize - 1][i]);
        }


    }

    private Node[][] generateNodes(int layerNum) {
        Node[][] layerNodes = new Node[this.substrateSize][this.substrateSize];
        int id = 0;
        for(int i = 0; i < this.substrateSize; i++){
            for(int j = 0; j < this.substrateSize; j++){
                layerNodes[i][j] = new Node(id, layerNum);
                id++;
                this.numNodes++;
            }
        }
        return layerNodes;
    }

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


    protected Node[] getInputNodes() {return (Node[])this.inputNodes.toArray();}

    protected Node[] getOutputNodes() {return (Node[])this.outputNodes.toArray();}


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
        //System.err.println("--" + network_id + "--");///////////////////////////////////////////////////////////////
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
        }for (Node[] nodes : outputLayer){
            for (Node node : nodes) {
                node.setInputValue(0);
            }
        }


        return outputs;
    }

}
