package AIinterfaces.SpeciesIF;

import com.badlogic.gdx.graphics.Color;

public interface SpeciesIF {

    void cull();

    void setStaleness();

    void shareFitness();

    int getBestOrgID();

    void setCompatibilityNetwork();

    void setAverageFitness();

    /**
     * Returns the staleness of this species.
     * @return The staleness of this species.
     */
    public int getStaleness();

    /**
     * Returns the color assigned to this species.
     * @return The color assigned to this species.
     */
    public Color getColor();

    double getAverageFitness();
}
