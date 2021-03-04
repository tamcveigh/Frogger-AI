package AIinterfaces.PopulationIF;

import AIinterfaces.SpeciesIF.NEATSpeciesIF;

import java.util.List;

/**
 * This interface is a broad overview for all methods concerning NEAT populations
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface NEATPopulationIF {

    /**
     * Gets the list of the species
     * @return The list of species
     */
    List<NEATSpeciesIF> getSpecies();

    /**
     * The best agent of the population
     * @return The ID of the best agent
     */
    int getBestAgentID();
}
