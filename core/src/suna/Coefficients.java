package suna;

public enum Coefficients {

    INITIAL_MUTATIONS(200),
    STEP_MUTATIONS(5),
    POPULATION_SIZE(100),
    MAX_NOVELTY_MAP_POP_SIZE(20),
    MUTATION_ADD_NEURON(0.01),
    MUTATION_REMOVE_NEURON(0.01),
    MUTATION_ADD_CONNECTION(0.49),
    MUTATION_REMOVE_CONNECTION(0.49),
    NEUROMODULATION_CHANCE(0.1),
    CONTROL_NEURON_CHANCE(0.2),
    EXTINCTION_THRESHOLD(0.0);


    /** The value of this coefficient. */
    private final double value;

    /**
     * Coefficient constructor. Assigns the supplied value.
     * @param value The value for the coefficient.
     */
    Coefficients(double value) {
        this.value = value;
    }

    /**
     * Returns the value of the coefficient.
     * @return The value of the coefficient.
     */
    public double getValue() {
        return value;
    }

}
