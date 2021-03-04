package AIinterfaces;

/**
 * Enum to allow launchers to use the same base code but select which algorithm to use
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22 November, 2020
 */
public enum AlgorithmName {

    /** Value to specify the NEAT algorithm */
    NEAT(0),
    /** Value to specify the HyperNEAT algorithm*/
    HyperNEAT(1),
    /** Value to Specifiy the SUNA algorithm */
    SUNA(2);

    /** The value to specify which algorithm is being used*/
    private final int value;

    /**
     * Constructor for this Enum
     * @param value The value of the algorithm
     */
    AlgorithmName(int value) { this.value = value;}

    /**
     * Get the value of the algorithm Enum
     * @return The value of the Enum
     */
    public int getValue(){ return this.value;}
}
