package AIinterfaces.PopulationIF;

import AIinterfaces.SpeciesIF.NEATSpeciesIF;

import java.util.List;

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
