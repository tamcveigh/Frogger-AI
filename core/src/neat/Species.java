package neat;

import AIinterfaces.NetworkIF.NEATNetworkIF;
import AIinterfaces.ReusedCode;
import AIinterfaces.SpeciesIF.NEATSpeciesIF;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.Array;

import java.util.*;

/**
 * Class which represents a group of similar organisms, therefore forming a species. This
 * protects new innovations in each network as organisms will compete within their species
 * instead of the total population.
 * @author Chance Simmons and Brandon Townsend
 * @version 22nd November, 2020
 * @additions Brooke Kiser and Tyler McVeigh
 */
public class Species extends ReusedCode implements NEATSpeciesIF {

    /** Static list of colors that are already being used by species. */
    public static List<Color> takenColors = new ArrayList<>();

    /**
     * The network other networks will be tested against to see if they are compatible with this
     * species.
     */
    private NEATNetworkIF compatibilityNetwork;

    /** A mapping of agent IDs to their networks. */
    private Map<Integer, NEATNetworkIF> organisms;

    /** The ID number of the best organism in this species this generation. */
    private int bestOrgID;

    /** The highest recorded fitness for this species over all generations. */
    private int bestFitness;

    /** The average fitness of this species this generation. */
    private double averageFitness;

    /** The staleness (number of generations of no improvement). */
    private int staleness;

    /** The color assigned to the agents that belong to this species. */
    private Color color;

    /**
     * Constructor for an organism's species.
     * @param agentID The ID number of the first agent to be assigned to this species.
     * @param agentNetwork The network used by the first agent to be assigned to this species.
     */
    public Species(int agentID, NEATNetworkIF agentNetwork) {
        compatibilityNetwork = new Network(agentNetwork);
        organisms = new HashMap<>();
        organisms.put(agentID, compatibilityNetwork);
        bestOrgID = agentID;
        bestFitness = 0;
        averageFitness = 0.0;
        staleness = 0;

        Array<Color> colors = Colors.getColors().values().toArray();
        do {
            // Randomly generate a color till we have a good one.
            color = colors.get(new Random().nextInt(colors.size));
        } while(takenColors.contains(color) && color != Color.CLEAR);
        takenColors.add(color);
    }

    /**
     * Returns the color assigned to this species.
     * @return The color assigned to this species.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the network used to test compatibility with this species.
     * @return The network used to test compatibility with this species.
     */
    public NEATNetworkIF getCompatibilityNetwork() {
        return compatibilityNetwork;
    }

    /** Sets the compatibility network to a random organism that is in this species. */
    public void setCompatibilityNetwork() {
        Object[] networks = organisms.values().toArray();
        compatibilityNetwork =
                new Network((NEATNetworkIF) networks[new Random().nextInt(networks.length)]);
    }

    /**
     * Returns the mapping of agent IDs and their networks.
     * @return The mapping of agent IDs and their networks.
     */
    public Map<Integer, NEATNetworkIF> getOrganisms() {
        return organisms;
    }

    /**
     * Adds an organism to the mapping of organisms in this species.
     * @param agentID The ID number of the agent.
     * @param agentNetwork The network the agent uses.
     */
    public void addOrganism(int agentID, NEATNetworkIF agentNetwork) {
        organisms.put(agentID, agentNetwork );
    }

    /**
     * Returns the ID number mapped to best network this generation.
     * @return The ID number mapped to the best network.
     */
    public int getBestOrgID() {
        return bestOrgID;
    }

    /**
     * Returns the average fitness of this species.
     * @return The average fitness of this species.
     */
    public double getAverageFitness() {
        return averageFitness;
    }

    /** Calculates the average fitness for this species and sets it. */
    public void setAverageFitness() {
        double fitnessSum = 0.0;
        for(NEATNetworkIF network : organisms.values()) {
            fitnessSum += network.getFitness();
        }
        if(organisms.isEmpty()) {
            averageFitness = 0;
        } else {
            averageFitness = fitnessSum / organisms.size();
        }
    }

    /**
     * Returns the staleness of this species.
     * @return The staleness of this species.
     */
    public int getStaleness() {
        return staleness;
    }

    /**
     * Increments the staleness of this species if no progress has been made. Otherwise, sets the
     * staleness back to zero.
     */
    public void setStaleness() {
        int generationMaxFitness = -1;
        for(Map.Entry<Integer, NEATNetworkIF> organism : organisms.entrySet()) {
            if(organism.getValue().getFitness() > generationMaxFitness) {
                generationMaxFitness = organism.getValue().getFitness();
                bestOrgID = organism.getKey();
            }
        }
        if(generationMaxFitness > bestFitness) {
            bestFitness = generationMaxFitness;
            staleness = 0;
        } else {
            staleness++;
        }
    }

    /**
     * Takes the top 50% of networks in this species and retains them. Culls the bottom 50% so
     * that they will not pollute the gene pool.
     */
    public void cull() {
        Map<Integer, NEATNetworkIF> survivors = new HashMap<>();

        for(Map.Entry<Integer, NEATNetworkIF> organism : organisms.entrySet()) {
            int maxOrganism = organism.getKey();
            int maxFitness = organism.getValue().getFitness();

            for(Map.Entry<Integer, NEATNetworkIF> other : organisms.entrySet()) {
                int otherOrganism = other.getKey();
                int otherFitness = other.getValue().getFitness();

                if(!survivors.containsKey(otherOrganism) && otherFitness > maxFitness) {
                    maxOrganism = otherOrganism;
                    maxFitness = otherFitness;
                }
            }
            survivors.put(maxOrganism, new Network(organisms.get(maxOrganism)));
            organisms.get(maxOrganism).setFitness(-1);

            if(survivors.size() >= organisms.size() * Coefficients.CULL_THRESH.getValue()) {
                break;
            }
        }
        organisms = survivors;
    }

    /**
     * Shares each network's fitness so that it can be normalized and keeps species from becoming
     * too large. This should prevent any one species from taking over the entire population.
     */
    public void shareFitness() {
        for(NEATNetworkIF network : organisms.values()) {
            network.setFitness(network.getFitness() / organisms.size());
        }
    }

    /**
     * Generates a new network either through crossover of two random parent networks or through
     * a direct clone of a random network in the species. The new network is then mutated in the
     * hopes that we find a favorable mutation.
     * @return The new network we have produced and mutated.
     */
    public NEATNetworkIF reproduce() {
        NEATNetworkIF baby;
        if(Math.random() < Coefficients.CROSSOVER_THRESH.getValue()) {
            Object[] networks = organisms.values().toArray();
            NEATNetworkIF parent1 = (NEATNetworkIF) networks[new Random().nextInt(networks.length)];
            NEATNetworkIF parent2 = (NEATNetworkIF) networks[new Random().nextInt(networks.length)];
            if(parent1.getFitness() < parent2.getFitness()) {
                baby = (NEATNetworkIF) crossover(parent1, parent2);
            } else {
                baby = (NEATNetworkIF) crossover(parent2, parent1);
            }
        } else {
            Object[] networks = organisms.values().toArray();
            baby = new Network((NEATNetworkIF) networks[new Random().nextInt(networks.length)]);
        }

        baby.mutate();
        return baby;
    }
}
