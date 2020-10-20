package AIinterfaces.PopulationIF;

import AIinterfaces.SpeciesIF.HNSpeciesIF;
import AIinterfaces.SpeciesIF.SpeciesIF;

import java.util.List;

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
