package hyperneat;

import java.util.List;

import static java.lang.Math.abs;

/**
 * This class will
 */
public class CPPN {

    /** Array holding the weights between any two nodes on the substrate*/
    private double[][][][] substrateWeights;

    private Network CPPNFunction;

    private Network generatedNetwork;

    private final int[][] INPUT_NODES;

    private final int[][] OUTPUT_NODES;

    /**
     *
     * @param inputSize the size of the square matrix of the substrate
     */
    public CPPN(int inputSize, int outputSize){
        int size = (int) Coefficients.SUBSTRATE_SIZE.getValue();
        this.substrateWeights = new double[size][size][size][size];
        this.CPPNFunction = new Network(4,1);//2 pairs of input points, 1 output weight
        this.generatedNetwork = new Network(inputSize, outputSize);

        int[][] tempInput = new int[inputSize][2];
        for(int i = 0; i < inputSize; i++){
            tempInput[i] = new int[]{0, i};
        }
        INPUT_NODES = tempInput;

        int[][] tempOutput = new int[outputSize][2];
        for(int i = 0; i < outputSize; i++){
            tempOutput[i] = new int[]{size -1, i};
        }
        OUTPUT_NODES = tempOutput;


        this.generateNetwork();
    }

    /**
     * Creates a  copy of this CPPN
     * @return
     */
    @Override
    public CPPN clone(){
        CPPN clone = new CPPN(this.INPUT_NODES.length,this.OUTPUT_NODES.length);
        clone.substrateWeights = this.substrateWeights.clone();
        clone.generatedNetwork = new Network(this.generatedNetwork);
        clone.CPPNFunction = new Network(this.CPPNFunction);
        return clone;
    }

    /**
     * Takes any two points on an array
     *
     * @param xOne x coordinate of the first point on the substrate
     * @param yOne y coordinate of the first point on the substrate
     * @param xTwo x coordinate of the second point on the substrate
     * @param yTwo y coordinate of the second point on the substrate
     * @return the weight of the connection between these two points
     */
    private double outputWeight( int xOne, int yOne, int xTwo, int yTwo){
        float[] inputValues = { xOne, yOne, xTwo, yTwo };
        double[] outputArray = this.CPPNFunction.feedForward(inputValues);
        return outputArray[0];
    }

    /**
     * Function that will take the generated weights and create a Neural Network from them
     */
    private void generateNetwork(){
        //Create the node weights
        for(int i = 0; i < Coefficients.SUBSTRATE_SIZE.getValue(); i++){
            for(int j = 0; j < Coefficients.SUBSTRATE_SIZE.getValue(); j++){
                for(int k = 0; k < Coefficients.SUBSTRATE_SIZE.getValue(); k++){
                    for(int l = 0; l < Coefficients.SUBSTRATE_SIZE.getValue(); l++){
                        double output  = this.outputWeight(i,j,k,l);
                        if( abs(output) > Coefficients.MIN_WEIGHT.getValue() ){
                            this.substrateWeights[i][j][k][l] = output;
                        }else{
                            this.substrateWeights[i][j][k][l] = 0;
                        }
                    }
                }
            }
        }//end nested loops

        //Get the nodes and links from the phenotype network and place weights there
        Node[] inputNodes = this.generatedNetwork.getInputNodes();
        Node[] outputNodes = this.generatedNetwork.getOutputNodes();
        List<Link> linkList = this.generatedNetwork.getLinks();
        linkList.clear();

        for(int i = 0; i < inputNodes.length; i++){
            for(int j = 0; j < outputNodes.length; j++){
                this.generatedNetwork.addLink(inputNodes[i],outputNodes[j],
                        this.substrateWeights[0][i][this.substrateWeights.length -1][j]);
            }
        }


    }

    /**
     * Getter method for the Generated neural network
     * @return the generated neural network
     */
    public Network getGeneratedNetwork(){
        return this.generatedNetwork;
    }

    public Network getCPPNetwork(){
        return this.CPPNFunction;
    }
}
