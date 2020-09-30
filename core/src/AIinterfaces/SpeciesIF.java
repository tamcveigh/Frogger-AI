package AIinterfaces;

import com.badlogic.gdx.graphics.Color;
import hyperneat.CPPN;
import neat.Network;

import java.util.Map;

public interface SpeciesIF {

    /**
     * Returns the mapping of agent IDs and their networks.
     * @return The mapping of agent IDs and their networks.
     */
    public Map<Integer, NetworkIF> getOrganisms();

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

    NetworkIF reproduce();

    void setAverageFitness();

    void addOrganism(int agentID, NetworkIF agentNetwork);

    void cull();

    void setStaleness();

    void shareFitness();

    int size();

    NetworkIF getCompatibilityNetwork();

    int getBestOrgID();

    void setCompatibilityNetwork();
}
