package AIinterfaces;

import hyperneat.*;
import neat.Species;

import java.util.*;

public class ReusedCode {

    /**
     * Mutates the weight of this link either completely or slightly.
     */
    public static void mutateWeight(double weight) {
        if(Math.random() < 0.1) {      // Completely change the weight.
            weight = Math.random() * 2 - 1;
        } else {                // Slightly change the weight.
            Random random = new Random();
            weight += random.nextGaussian() / 50;

            if(weight > 1) {
                weight = 1;
            } else if(weight < -1) {
                weight = -1;
            }
        }
    }

    /**
     * Performs a deep copy of the links from a supplied network to this one.
     * @param network The network to copy the links from.
     */
    public static  void copyLinks(Network network, Network thisNetwork) {
        for(Link link : network.getLinks()) {
            Node input = thisNetwork.getNode(link.getInputNodeID());
            Node output = thisNetwork.getNode(link.getOutputNode().getId());
            assert input != null;
            assert output != null;
            addLink(thisNetwork, input, output, link.getWeight());
            thisNetwork.getLinks().get(thisNetwork.getLinks().size() - 1).setEnabled(link.isEnabled());
        }
    }

    /**
     * Helper function to fully connect the initial network of just input and output nodes. Also
     * attaches the bias node to each output node.
     */
    public static  void generateNetwork(Network thisNetwork, Node[] inputNodes, Node[] outputNodes, Node biasNode) {
        for(Node input : inputNodes) {
            for(Node output : outputNodes) {
                // Math.random() * 2 - 1 generates a random number between -1 and 1.
                addLink(thisNetwork, input, output, Math.random() * 2 - 1);
            }
        }
        for(Node output : outputNodes) {
            // Link the bias node and apply a random link weight. Comment out if using line below.
            addLink(thisNetwork, biasNode, output, Math.random() * 2 - 1);
            // NOTE: If you would like to control the link weight of the bias node, please use
            // the created one in the coefficients enumeration and uncomment the line below.
            // addLink(biasNode, output, Coefficients.BIAS_NODE_LINK_WEIGHT.getValue());
        }
    }

    /**
     * Adds a specified link between the two supplied nodes with a supplied weight. Does NOT
     * randomly add a link to the network. Will not create a link if the two nodes are already
     * connected.
     * @param input The input node to connect from.
     * @param output The output node to connect to.
     * @param weight The weight that should be given to the link.
     */
    public static  void addLink(Network network, Node input, Node output, double weight) {
        int inputID = input.getId();
        int outputID = output.getId();

        if(!isConnectedTo(input, output)) {
            network.getLinks().add(new Link(getInnovationNumber(network, inputID, outputID), inputID, output, weight));
            input.getOutgoingLinks().add(new Link(getInnovationNumber(network, inputID, outputID), inputID,
                    output, weight));
        }
    }

    /**
     * Returns the innovation number of the link between the specified input node ID and output
     * node ID. Will return a new innovation number if there is no pre-existing link in our
     * innovation number list.
     * @param inputID The ID of the input node.
     * @param outputID The ID of the output node.
     * @return The innovation number of a pre-existing link or a brand new innovation number.
     */
    public static  int getInnovationNumber(Network thisNetwork, int inputID, int outputID) {
        int innovationNumber = Network.getInnovationList().size();
        String innovationSearch = inputID + " " + outputID;
        boolean found = false;
        for(Map.Entry<Integer, String> innovation : Network.getInnovationList().entrySet()) {
            int key = innovation.getKey();
            String value = innovation.getValue();

            if(value.equals(innovationSearch)) {
                innovationNumber = key;
                found = true;
            }
        }
        if(!found) {    // If we did not find an existing innovation number, make a new one.
            Network.getInnovationList().put(innovationNumber, innovationSearch);
        }
        return innovationNumber;
    }

    /**
     * Returns whether or not this network is fully connected. Links that are not enabled are
     * still counted in this total.
     * @return True if this network is fully connected, false otherwise.
     */
    public static  boolean isFullyConnected(Network thisNetwork) {
        // Adding up the total links from input nodes to their outputs.
        int maxLinks = thisNetwork.getInputNodes().length * thisNetwork.getHiddenNodes().size();
        maxLinks += thisNetwork.getInputNodes().length * thisNetwork.getOutputNodes().length;

        // Adding up the total links from hidden nodes to their outputs.
        for(Node node : thisNetwork.getHiddenNodes()) {
            for (Node otherNode : thisNetwork.getHiddenNodes()) {
                if(node.getLayer() < otherNode.getLayer()) {
                    maxLinks++;
                }
            }
        }
        maxLinks += thisNetwork.getHiddenNodes().size() * thisNetwork.getOutputNodes().length;

        // Adding up the total links from bias nodes to their outputs.
        maxLinks += thisNetwork.getHiddenNodes().size() + thisNetwork.getOutputNodes().length;

        return maxLinks == thisNetwork.getLinks().size();
    }

    /**
     * Activates every node in the network in a certain specified order. Should traverse from
     * input -> bias -> hidden -> output.
     * @param inputValues The values to be set as our input layer nodes' output values.
     * @return The output values in our output nodes after every node has been activated.
     */
    public static  double[] feedForward(Network thisNetwork, float[] inputValues) {
        // Set the output values of our input nodes to the supplied input values.
        for(int i = 0; i < thisNetwork.getInputNodes().length; i++) {
            thisNetwork.getInputNodes()[i].setOutputValue(inputValues[i]);
        }

        // Activate the nodes in order from input node -> bias -> hidden -> output.
        for(Node node : listNodesByLayer()) {
            node.activate();
        }

        // Write the output values to a double array to pass back as the decisions of this network.
        double[] outputs = new double[thisNetwork.getOutputNodes().length];
        for(int i = 0; i < thisNetwork.getOutputNodes().length; i++) {
            outputs[i] = (thisNetwork.getOutputNodes()[i].getOutputValue());
        }

        // Set all input values back to 0 for the next feed forward.
        for(Node node : listNodesByLayer()) {
            node.setInputValue(0);
        }

        return outputs;
    }

    /**
     * Adds a link between two randomly selected nodes.
     */
    public static  void addLinkMutation(Network thisNetwork) {
        if(!isFullyConnected(thisNetwork)) {
            Random random = new Random();
            List<Node> allNodes = listNodesByLayer();

            Node input, output;
            do {
                input = allNodes.get(random.nextInt(allNodes.size()));
                output = allNodes.get(random.nextInt(allNodes.size()));
            } while(thisNetwork.isBadLink(input, output));

            if(output.getLayer() < input.getLayer()) {
                Node temp = input;
                input = output;
                output = temp;
            }

            addLink(thisNetwork, input, output, Math.random() * 2 - 1);
        }
    }

    /**
     * Adds a node where the specified link used to be.
     * @param link The location where the new node should be added.
     */
    public static  void addNode(Network thisNetwork, Link link) {
        link.setEnabled(false);
        Node oldInput = thisNetwork.getNode(link.getInputNodeID());
        assert oldInput != null;
        int layer = (int) Math.ceil((oldInput.getLayer() + link.getOutputNode().getLayer()) / 2.0);

        // If the layer we're placing our new node was the previous output nodes layer, we
        // move all layers that are equal to or greater than the new layer down.
        if(layer == link.getOutputNode().getLayer()) {
            for (Node node : listNodesByLayer()) {
                if (node.getLayer() >= layer) {
                    node.incrementLayer();
                }
            }
            thisNetwork.incrementLayer();
        }

        // Actually add the node now so it avoids having it's own layer incremented.
        thisNetwork.incrementNodes();
        Node toAdd = new Node(thisNetwork.getNumNodes(), layer);
        toAdd.activate();
        thisNetwork.getHiddenNodes().add(toAdd);

        // Now add links to either side of the new node. The link going from the old input to the
        // new node gets a weight of 1 while the link going from the new node to the old output
        // receives the weight of the now-disabled link.
        addLink(thisNetwork, oldInput, toAdd, 1);
        addLink(thisNetwork, toAdd, link.getOutputNode(), link.getWeight());

        // Finally connect our bias node with a weight of 0 to minimize the bias' initial impact.
        addLink(thisNetwork, thisNetwork.getBiasNode(), toAdd, 0);
    }

    /**
     * Returns a list of all nodes in order by their layers.
     * @return A list of all nodes in order by their layers.
     */
    public static  List<Node> listNodesByLayer(Network thisNetwork) {
        List<Node> nodes = new ArrayList<>(Arrays.asList(thisNetwork.getInputNodes()));
        nodes.add(thisNetwork.getBiasNode());
        for(int layer = 1; layer < thisNetwork.getNumNodes(); layer++) {
            for(Node node : thisNetwork.getHiddenNodes()) {
                if(node.getLayer() == layer) {
                    nodes.add(node);
                }
            }
        }
        nodes.addAll(Arrays.asList(thisNetwork.getOutputNodes()));

        return nodes;
    }

    /**
     * Determines if the supplied network is compatible with this network based on how closely it
     * is related to this network.
     * @param network The network to check for compatibility.
     * @return True if it is compatible, false otherwise.
     */
    public static  boolean isCompatibleTo(Network network, Network thisNetwork) {
        double compatibility = 0.0;
        int numDisjoint = getNumDisjointLinks(network);
        double avgWeighDiff = getAverageWeightDiff(network.getLinks());
        double largestGenomeSize = Math.max(thisNetwork.getLinks().size(), network.getLinks().size());

        if(largestGenomeSize < 20) {
            largestGenomeSize = 1;
        }

        compatibility += (Coefficients.DISJOINT_CO.getValue() * numDisjoint) / largestGenomeSize;
        compatibility += Coefficients.WEIGHT_CO.getValue() * avgWeighDiff;

        return compatibility <= Coefficients.COMPAT_THRESH.getValue();
    }

    /**
     * Returns the number of links which are disjoint (i.e. they exist in one network, but not
     * the other.
     * @param network The network to check against the compatibility network.
     * @return The number of disjoint links.
     */
    public static  int getNumDisjointLinks(Network network, Network thisNetwork) {
        int numDisjoint = 0;
        List<Link> otherNetworkLinks = network.getLinks();
        for(Link link : thisNetwork.getLinks()) {
            if(!otherNetworkLinks.contains(link)) {
                numDisjoint++;
            }
        }

        for(Link link : otherNetworkLinks) {
            if(!thisNetwork.getLinks().contains(link)) {
                numDisjoint++;
            }
        }
        return numDisjoint;
    }

    /**
     * Returns the average weight difference between the matching links of the compatibility
     * network and the supplied list of links.
     * @param links The list of links to check calculate differences with our compatibility network.
     * @return The average weight difference between matching links.
     */
    public static  double getAverageWeightDiff(List<Link> links, Network thisNetwork) {
        int numMatching = 0;
        double weightSum = 0.0;
        for(Link link : thisNetwork.getLinks()) {
            boolean foundMatch = false;
            for(int i = 0; !foundMatch && i < links.size(); i++) {
                if(link.getInnovationNum() == links.get(i).getInnovationNum()) {
                    weightSum += Math.abs(link.getWeight() - links.get(i).getWeight());
                    numMatching++;
                    foundMatch = true;
                }
            }
        }

        if(numMatching == 0) {
            return 100;
        }
        return weightSum / numMatching;
    }

    /**
     * Method that mimics crossover of genomes. Takes this network and lines up its links with
     * the supplied network (parent 1 and parent 2). Any matching links (matching as in
     * innovation numbers) will have their weight and enabled randomly selected from one of the
     * parents. Then, any disjoint links should be added from the parent with the most fitness.
     * If the fitness is equal, then disjoint links should be added from both.
     * @param otherParent The other network to match links from.
     * @return The crossed over network.
     */
    public static  Network crossover(Network otherParent, Network thisNetwork) {
        Network baby = new Network(thisNetwork);

        // Randomly inherit traits from one of the matching links.
        for(Link link : baby.getLinks()) {
            for(Link other : otherParent.getLinks()) {
                if(link.getInnovationNum() == other.getInnovationNum()) {
                    if(Math.random() < 0.5) {
                        link.setWeight(other.getWeight());
                        link.setEnabled(other.isEnabled());
                    }
                    break;
                }
            }
        }

        // FIXME: 1/22/2020
//        if(otherParent.getFitness() == baby.getFitness()) {
//            for(Link link : otherParent.getLinks()) {
//                if(!baby.links.contains(link)) {
//                    add the link to the baby
//                    error arises when we attempt to add a link, but the baby does not have the
//                    needed nodes.
//                }
//            }
//        }
        return baby;
    }

    /**
     * Checks to see whether this node is connected to the supplied node.
     * @param node The supplied node to check connection with.
     * @return True if the nodes are connected, false otherwise.
     */
    public static  boolean isConnectedTo(Node input, Node node) {
        if(input.getLayer() != node.getLayer()) {
            if(input.getLayer() < node.getLayer()) {
                for(Link link : input.getOutgoingLinks()) {
                    if(link.getOutputNode().equals(node)) {
                        return true;
                    }
                }
            } else {
                for(Link link : node.getOutgoingLinks()) {
                    if(link.getOutputNode().equals(input)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Removes all species that are over the staleness threshold, except the one that contains
     * the current best organism.
     */
    public static  void removeStaleSpecies(Population thisPopulation) {
        for(int i = 0; i < thisPopulation.getSpecies().size(); i++) {
            if(!thisPopulation.getSpecies().get(i).getOrganisms().containsKey(thisPopulation.getBestAgentID())) {
                if(thisPopulation.getSpecies().get(i).getStaleness() >= neat.Coefficients.STALENESS_THRESH.getValue()) {
                    Species.takenColors.remove(thisPopulation.getSpecies().get(i).getColor());
                    thisPopulation.getSpecies().remove(thisPopulation.getSpecies().get(i));
                    i--;
                }
            }
        }
    }
}
