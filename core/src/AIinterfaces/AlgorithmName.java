package AIinterfaces;

/**
 * Enum to allow launchers to use the same base code but select which algorithm to use
 */
public enum AlgorithmName {

    /** Value to specify the NEAT algorithm */
    NEAT(0),
    /** Value to specify the HyperNEAT algorithm*/
    HyperNEAT(1);

    /** The value to specify which algorithm is being used*/
    private final int value;

    /**
     * Constructor for this Enum
     * @param value
     */
    AlgorithmName(int value) { this.value = value;}

    /**
     *
     * @return
     */
    public int getValue(){ return this.value;}
}
