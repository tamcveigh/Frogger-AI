package AIinterfaces;

public enum AlgorithmName {

    NEAT(0),
    HyperNEAT(1);

    private final int value;

    AlgorithmName(int value) { this.value = value;}

    public int getValue(){ return this.value;}
}
