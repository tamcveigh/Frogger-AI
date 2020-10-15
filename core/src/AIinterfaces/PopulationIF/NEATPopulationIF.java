package AIinterfaces.PopulationIF;

import AIinterfaces.SpeciesIF.NEATSpeciesIF;
import com.mygdx.kittener.game.Agent;

import java.util.List;

/**
 * Interface to allow the Population classes to be interchangeable
 * @author Brooke Kiser and Tyler McVeigh
 * @version 24 September 2020
 */
public interface NEATPopulationIF {

    /**
     * Returns the current generation we are at.
     * @return The current generation we are at.
     */
    int getGeneration();

    /**
     * Increments the generation by one. Represents moving to the next generational step.
     */
    void incrementGeneration();

    /**
     * Returns the network output given the supplied agent ID number and it's vision.
     * @param id The agent ID number to search for in our mapping.
     * @param agentVision The array of what the agent can see
     * @return The network output by the supplied agent ID number.
     */
    double[] getNetworkOutput(int id, float[] agentVision);

    /**
     * Passes along the score of an agent to its network so that the species class can have
     * access to its score.
     * @param id The identification number of the agent to be used as a key to grab the network.
     * @param fitness The score to be passed to the network.
     */
    void assignFitness(int id, int fitness);

    /**
     * Sets the supplied agent with a certain species color.
     * @param agent The agent to modify the color of.
     */
    void assignColor(Agent agent);

    /**
     * The main NEAT algorithmic method. Calls all needed helper methods to separate our
     * organisms into species, cull them down so that we only get the high-performing ones, and
     * reproduces.
     */
    void naturalSelection();
}
