package AIinterfaces.PopulationIF;

import AIinterfaces.SpeciesIF.HNSpeciesIF;

import java.util.List;

/**
 * This interface is a broad overview for all methods concerning HyperNEAT populations
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public interface HNPopulationIF{
    /**
     * Gets the list of the species
     * @return The list of species
     */
    List<HNSpeciesIF> getSpecies();

    /**
     * The best agent of the population
     * @return The ID of the best agent
     */
    int getBestAgentID();
}
