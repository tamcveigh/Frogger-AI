package hyperneat;

//TODO Finish method stubs
/**
 * This class will
 */
public class CPPN {

    /** Array holding the weights between any two node ons the substrate*/
    private double[][][][] substrateWeights;

    private Network generatedNetwork;

    /**
     *
     * @param size the size of the square matrix of the substrate
     */
    public CPPN(int size){
        this.substrateWeights = new double[size][size][size][size];
    }

    /**
     * Creates a deep copy of this CPPN
     * @return
     */
    @Override
    public CPPN clone(){

        return null;
    }

    /**
     * Function that will output the weights of the connections between any two
     * points on the substrate
     *
     * @param xOne x coordinate of the first point on the substrate
     * @param yOne y coordinate of the first point on the substrate
     * @param xTwo x coordinate of the second point on the substrate
     * @param yTwo y coordinate of the second point on the substrate
     * @return the weight of the connection between these two points
     */
    private double outputWeight( int xOne, int yOne, int xTwo, int yTwo){

        return 0.0;
    }

    /**
     * Function that will take the generated weights and create a Neural Network from them
     */
    private void generateNetwork(){

    }

    /**
     * Getter method for the Generated neural network
     * @return the generated neural network
     */
    public Network getGeneratedNetwork(){
        return this.generatedNetwork;
    }
}
