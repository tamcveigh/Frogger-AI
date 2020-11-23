package hyperneat;

import AIinterfaces.NetworkIF.CPPNNetworkIF;
import AIinterfaces.NetworkIF.HNNetworkIF;
import AIinterfaces.NetworkIF.NetworkIF;
import AIinterfaces.PopulationIF.HNPopulationIF;
import AIinterfaces.PopulationIF.PopulationIF;
import AIinterfaces.ReusedCode;
import AIinterfaces.SpeciesIF.HNSpeciesIF;
import com.mygdx.kittener.game.Agent;
import com.mygdx.kittener.game.MainGame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Class which handles all overhead HN functionality and is connected to the game. Keeps track of the generation, the
 * list of species, and a mapping of agents to their networks.
 *
 * @author Brooke Kiser and Tyler McVeigh
 * @version 22nd November, 2020
 */
public class Population extends ReusedCode implements HNPopulationIF, PopulationIF {

    /** List of every species in the game. */
    private final List<HNSpeciesIF> species;

    /** Mapping of each game agent to their network. */
    private final Map<Integer, CPPNNetworkIF> organisms;

    /** Keeps track of the generation of organisms we're at. */
    private int generation;

    /** Identification number of the best agent. */
    private int bestAgentID;

    /** The type of AI */
    private final boolean type = false;

    /**
     * Constructors our population. Maps every agent to a newly formed network.
     * @param agents The list of agents to connect via mapping.
     * @param input  The number of inputs we're expecting.
     * @param output The number of outputs we're expecting.
     */
    public Population(List<Agent> agents, int input, int output) {
        generation = 0;
        species = new ArrayList<>();
        organisms = new HashMap<>();
        bestAgentID = 0;

        //Place each agent and corresponding CPPN in the organisms
        for (Agent agent : agents) {
            organisms.put(agent.getId(), new CPPN(input, output));
        }
    }

    /**
     * Returns the current generation we are at.
     * @return The current generation we are at.
     */
    public int getGeneration() {
        return generation;
    }

    /** Increments the generation by one. Represents moving to the next generational step. */
    public void incrementGeneration() {
        generation++;
    }

    /**
     * Returns the network output given the supplied agent ID number and it's vision.
     * @param id          The agent ID number to search for in our mapping.
     * @param agentVision The array of what the agent can see
     * @return The network output by the supplied agent ID number.
     */
    public double[] getNetworkOutput(int id, float[] agentVision) {
        return organisms.get(id).runSubstrate(agentVision);
    }

    /**
     * Passes along the score of an agent to its network so that the species class can have access to its score.
     * @param id      The identification number of the agent to be used as a key to grab the network.
     * @param fitness The score to be passed to the network.
     */
    public void assignFitness(int id, int fitness) {
        HNNetworkIF organism = organisms.get(id);
        organism.setFitness(fitness);
    }

    /**
     * Sets the supplied agent with a certain species color.
     * @param agent The agent to modify the color of.
     */
    public void assignColor(Agent agent) {

        for (HNSpeciesIF s : species) {
            if (organisms.get(agent.getId()) == null) {
                break;
            }

            if (isCompatibleTo(s.getCompatibilityNetwork(), organisms.get(agent.getId()).getCPPNetwork())) {
                agent.setColor(s.getColor());
                break;
            }
        }
    }

    /** Sets the best agent of this generation. */
    private void setBestAgentID() {
        System.err.println();
        int bestFitness = organisms.get(0).getFitness();
        for (Map.Entry<Integer, CPPNNetworkIF> organism : organisms.entrySet()) {
            //System.err.println("Best fitness: " + bestFitness + " : " + bestAgentID);
            if (organism.getValue().getFitness() > bestFitness) {
                bestFitness = organism.getValue().getFitness();
                bestAgentID = organism.getKey();
            }
        }
    }

    /**
     * The main HN algorithmic method. Calls all needed helper methods to separate our organisms into species, cull them
     * down so that we only get the high-performing ones, and reproduces.
     */
    public void naturalSelection() {
        // Set up for producing babies.
        speciate();
        setBestAgentID();
        cullSpecies();
        removeStaleSpecies(this);
        removeBadSpecies();

        double avgSum = getAvgFitnessSum();
        List<NetworkIF> babies = new ArrayList<>();
        for (HNSpeciesIF s : species) {

            // Directly clone the best network of the species.
            //System.err.println("pop ~ Natural Selection: " + s.getBestOrgID() + " : " + bestAgentID);
            CPPNNetworkIF babyS = s.getOrganisms().get(s.getBestOrgID());
            CPPNNetworkIF babyO = organisms.get(bestAgentID);
            if (babyS != null && babyO != null) {
                babies.add(babyS);
                babies.add(babyO);
            }

            // Find the correct number of babies and reproduce them.
            int numBabies = (int) Math.floor((s.getAverageFitness() / avgSum) * organisms.size()) - 1;

            for (int i = 0; i < numBabies; i++) {
                CPPNNetworkIF baby = s.reproduce();
                if (baby != null) {
                    babies.add(baby);
                }
            }
        }

        // If we don't have enough babies, produce them from random species.
        while (babies.size() < organisms.size()) {
            Random r = new Random();
            HNSpeciesIF s = species.get(r.nextInt(species.size()));
            babies.add(s.reproduce());
        }

        // Set up our agent's with their new networks.
        int i = 0;
        for (Map.Entry<Integer, CPPNNetworkIF> organism : organisms.entrySet()) {
            organism.setValue((CPPNNetworkIF) babies.get(i));
            i++;
        }
    }

    /** Writes statistics to a csv file. The statistic will include the generation, average fitness, and max fitness. */
    private void statisticsTrack() {

        List<HNNetworkIF> organisms = new ArrayList<>();
        String colorsUsed = "";
        //Adds all the CPPNs for a species to organisms
        for (HNSpeciesIF s : species) {
            organisms.addAll(s.getOrganisms().values());
            colorsUsed += s.getColor() + " : ";
        }

        int max = 0;
        //Find the max fitness of all the CPPNs for this generation
        for (HNNetworkIF o : organisms) {
            int oFit = o.getFitness();
            if (oFit > max) {
                max = oFit;
            }
        }

        int average = 0;
        //Get the average of the fitnesses for this generation
        for (HNNetworkIF o : organisms) {
            average += o.getFitness();
        }
        average = average / organisms.size();

        //Write to the log file
        try {
            FileWriter statWriter = new FileWriter(MainGame.STAT_LOG, true);
            statWriter.write("\n" + generation + "," + average + "," + max + "," + colorsUsed);
            statWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to track statistics for generation " + generation);
        }

    }

    /** Separates this populations list of organisms into separate species. */
    private void speciate() {
        // First, set up each existing species compatibility network and clear it of all
        // organisms from the last generation.
        for (HNSpeciesIF s : species) {
            s.setCompatibilityNetwork();
            s.getOrganisms().clear();
            s.setAverageFitness();
        }

        // For each organism in the population, see if it is compatible with any existing species.
        for (Map.Entry<Integer, CPPNNetworkIF> organism : organisms.entrySet()) {
            int agentID = organism.getKey();
            CPPNNetworkIF agentNetwork = organism.getValue();
            boolean speciesFound = false;
            for (int i = 0; !speciesFound && i < species.size(); i++) {
                HNSpeciesIF s = species.get(i);
                if (isCompatibleTo(agentNetwork.getCPPNetwork(), s.getCompatibilityNetwork())) {
                    s.addOrganism(agentID, agentNetwork);
                    speciesFound = true;
                }
            }

            // If it is not compatible, create a new species.
            if (!speciesFound) {
                species.add(new Species(agentID, agentNetwork));
            }
        }

        statisticsTrack();
        //Set the average fitness for a species
        for (HNSpeciesIF s : species) {
            s.setAverageFitness();
        }
    }

    /**
     * Culls each species as well as share fitness between each organism in the species and set the species average
     * fitness.
     */
    private void cullSpecies() {
        for (HNSpeciesIF s : species) {
            s.cull();
            s.setStaleness();
            s.shareFitness();
            s.setAverageFitness();
        }
    }

    /** Removes any species who would produce zero babies this generation. */
    private void removeBadSpecies() {
        double avgSum = getAvgFitnessSum();
        for (int i = 0; i < species.size(); i++) {
            if (!species.get(i).getOrganisms().containsKey(bestAgentID)) {
                if (species.get(i).getAverageFitness() / avgSum * organisms.size() < 1 || species.get(i).size() < 1) {
                    Species.takenColors.remove(species.get(i).getColor());
                    species.remove(i);
                    i--;
                }
            }

        }
    }

    /**
     * Returns the sum of the averaged fitness of each species.
     * @return The sum of the averaged fitness of each species.
     */
    private double getAvgFitnessSum() {
        double avgSum = 0.0;
        for (HNSpeciesIF s : species) {
            avgSum += s.getAverageFitness();
        }
        return avgSum;
    }

    /**
     * Gets the list of the species
     * @return The list of species
     */
    public List<HNSpeciesIF> getSpecies() {
        return species;
    }

    /**
     * The best agent of the population
     * @return The ID of the best agent
     */
    public int getBestAgentID() {
        return bestAgentID;
    }

    /**
     * Get the type of AI
     * @return False if HyperNEAT and True if NEAT
     */
    public boolean getType() {
        return type;
    }
}
