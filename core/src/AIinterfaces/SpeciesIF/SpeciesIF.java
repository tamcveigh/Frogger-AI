package AIinterfaces.SpeciesIF;

import com.badlogic.gdx.graphics.Color;

/**
 * This interface is a broad overview for all the methods both implementations need
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface SpeciesIF {

    /**
     * Takes the top 50% of networks in this species and retains them. Culls the bottom 50% so that they will not
     * pollute the gene pool.
     */
    void cull();

    /**
     * Increments the staleness of this species if no progress has been made. Otherwise, sets the staleness back to
     * zero.
     */
    void setStaleness();

    /**
     * Shares each network's fitness so that it can be normalized and keeps species from becoming too large. This should
     * prevent any one species from taking over the entire population.
     */
    void shareFitness();

    /**
     * Returns the ID number mapped to best network this generation.
     * @return The ID number mapped to the best network.
     */
    int getBestOrgID();

    /** Sets the compatibility network to a random organism that is in this species. */
    void setCompatibilityNetwork();

    /** Calculates the average fitness for this species and sets it. */
    void setAverageFitness();

    /**
     * Returns the staleness of this species.
     * @return The staleness of this species.
     */
    int getStaleness();

    /**
     * Returns the color assigned to this species.
     * @return The color assigned to this species.
     */
    Color getColor();

    /**
     * Returns the average fitness of this species.
     * @return The average fitness of this species.
     */
    double getAverageFitness();
}
