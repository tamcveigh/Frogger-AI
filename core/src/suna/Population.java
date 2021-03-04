package suna;

import AIinterfaces.PopulationIF.PopulationIF;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.Array;
import com.mygdx.kittener.game.Agent;
import com.mygdx.kittener.game.MainGame;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This will be very similar to population in NEAT/HyperNEAT
 */
public class Population implements PopulationIF {

    private Map<Integer,Network> organisms;

    private List<Species> speciesList;

    private int generation;

    private int bestOrganismID;

    /**
     * Constructors our population. Maps every agent to a newly formed network.
     *
     * @param agents The list of agents to connect via mapping.
     * @param input  The number of inputs we're expecting.
     * @param output The number of outputs we're expecting.
     */
    public Population(List<Agent> agents, int input, int output) {
        generation = 0;
        speciesList = new ArrayList<>();
        organisms = new HashMap<>();
        bestOrganismID = 0;

        //Place each agent and corresponding Network agent in the organisms
        for (Agent agent : agents) {
            organisms.put(agent.getId(), new Network(input, output));
        }
        speciate();
    }

    /**
     * Returns the current generation we are at.
     *
     * @return The current generation we are at.
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * Increments the generation by one. Represents moving to the next generational step.
     */
    public void incrementGeneration() {
        generation++;
    }

    /**
     * Returns the network output given the supplied agent ID number and it's vision.
     *
     * @param id          The agent ID number to search for in our mapping.
     * @param agentVision The array of what the agent can see
     *
     * @return The network output by the supplied agent ID number.
     */
    public double[] getNetworkOutput(int id, float[] agentVision) {
        return organisms.get(id).feedForward(agentVision);
    }

    /**
     * Passes along the score of an agent to its network so that the species class can have access to its score.
     *
     * @param id      The identification number of the agent to be used as a key to grab the network.
     * @param fitness The score to be passed to the network.
     */
    public void assignFitness(int id, int fitness) {
        Network organism = organisms.get(id);
        organism.setFitness(fitness);
    }

    /**
     * Sets the supplied agent with a certain species color.
     *
     * @param agent The agent to modify the color of.
     */
    public void assignColor(Agent agent) {

        Network organism = organisms.get(agent.getId());

        for (Species s : speciesList) {
            List<Network> members = s.getMembers();
            if(members.contains(organism) ){
                agent.setColor(s.getColor() );
                break; //If found the species that the agent belongs to, set the agent's color and break the loop
            }
        }
    }

    /**
     * Sets the best agent of this generation.
     */
    private void setBestAgentID() {
        //System.err.println();
        int bestFitness = organisms.get(0).getFitness();
        for (Map.Entry<Integer, Network> organism : organisms.entrySet()) {
            //System.err.println("Best fitness: " + bestFitness + " : " + bestAgentID);
            if (organism.getValue().getFitness() > bestFitness) {
                bestFitness = organism.getValue().getFitness();
                bestOrganismID = organism.getKey();
            }
        }
    }

    private void speciate(){
        /*  Novelty Map algorithm. P is the population size, Nmetric() is a
            given novelty metric, NM is the set of cells in the Novelty Map, Maxn is
            the maximum size pf NM and dist() is the Euclidean distance.
        Require: |P| > Maxn > 0 and Nmetric() != ∅
            1: NM ← ∅ {All cells are empty}
            2: loop
                3: if An input I is presented to the Novelty Map then
                    4: if |NM| < Maxn then
                        5: NM ← NM ∪ {I}
                    6: else
                        7: {A ∈ NM | ∀K ∈ NM ∧ Nmetric(A) < Nmetric(K)}
                        8: if Nmetric(I) > Nmetric(A) then
                            9: NM ← NM ∪ {I}
                            10: NM ← NM \ {A}
                        11: end if
                    12: end if
                    13: return H : {H ∈ NM | ∀K ∈ NM ∧dist(I, H) < dist(I, K)}
                    {Return cell which is closest to input I}
                14: end if
            15: end loop
        */
        for(Network organism: organisms.values() ){
            Species closestMatch = null;
            double minDist = Double.MAX_VALUE;
            int[] orgSpec =  organism.getSpectralAnalysis();
            for (Species s : this.speciesList){
                int[] sSpec = s.getSpectralDiversityArray();
                double[] diff =  new double[5];
                for(int i = 0; i < orgSpec.length; i++){
                    diff[i] = orgSpec[i] - sSpec[i];
                    diff[i] = Math.pow(diff[i], 2);
                }
                double sum = 0;
                for(double d : diff){
                    sum += d;
                }
                double sDist = Math.sqrt(sum);

                if(sDist < minDist){
                    minDist = sDist;
                    closestMatch = s;
                }

            }

            if(closestMatch == null){
                Species s = new Species(organism.getSpectralAnalysis(), null);
                s.addMember(organism);
                Array<Color> colors = Colors.getColors().values().toArray();
                Color color;
                do {
                    // Randomly generate a color till we have a good one.
                    color = colors.get(new Random().nextInt(colors.size));
                    System.err.println("Color: " + color);
                } while (Species.takenColors.contains(color) || color == Color.CLEAR);
                this.speciesList.add(s);
            }

        }

    }

    public void naturalSelection(){

        for(Species s : speciesList){
            s.setBestFitness();
        }

        statisticsTrack();

        cull();

        reproduce();

        speciate();

    }

    @Override
    public int getBestAgentID() {
        return this.bestOrganismID;
    }

    private void cull(){

        List<Species> newList = new ArrayList<>();
        HashMap<Integer,Network> newPop = new HashMap<Integer, Network>();
        int i = 0;
        for (Species species: this.speciesList){
            Network best = species.getBestNetwork();
            Species nextGen = new Species(best.getSpectralAnalysis(),species.getColor());
            nextGen.addMember(best);
            newList.add(nextGen);
            nextGen.setBestNetwork(best);
            newPop.put(i++, best);
        }
        this.speciesList = newList;
        this.organisms = newPop;
    }

    private void reproduce(){
        while(this.organisms.values().size() < 30){
            Random r = new Random();
            int nextSpecies = r.nextInt(this.speciesList.size());
            Species species = this.speciesList.get(nextSpecies);
            Network child = new Network( species.getBestNetwork() );
            child.mutate();
            species.addMember(child);
        }
    }

    /**
     * Writes statistics to a csv file. The statistic will include the generation, average fitness, and max fitness.
     */
    private void statisticsTrack() {

        List<Network> organisms = new ArrayList<>();
        //Adds all the CPPNs for a species to organisms
        for (Species s : this.speciesList) {
            organisms.addAll(s.getMembers());
        }


        //Find the max fitness of all the CPPNs for this generation
        this.setBestAgentID();
        int max = this.organisms.get(this.bestOrganismID).getFitness();

        int average = 0;
        //Get the average of the fitnesses for this generation
        for (Network o : organisms) {
            average += o.getFitness();
        }
        average = average / organisms.size();

        //Write to the log file
        try {
            FileWriter statWriter = new FileWriter(MainGame.STAT_LOG, true);
            statWriter.write("\n" + generation + "," + average + "," + max);
            statWriter.close();
        } catch (IOException e) {
            System.err.println("ERROR: Unable to track statistics for generation " + generation);
        }

    }

}
