package suna;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public class Species {

    private List<Network> members;

    private int[] spectralDiversityArray;

    private Color color;

    private Network bestNetwork;

    private int bestFitness;

    private int avgFitness;



    public Species(int[] spectralDiversityArray, Color color){
        this.members = new ArrayList<Network>();
        this.spectralDiversityArray = spectralDiversityArray;
        this.color = color;
        this.bestFitness = 0;
        this.avgFitness = 0;
        this.bestNetwork = null;
    }

   public void addMember(Network network){
        this.members.add(network);
   }

    public int[] getSpectralDiversityArray() {
        return spectralDiversityArray;
    }

    public void setSpectralDiversityArray(int[] spectralDiversityArray) {
        this.spectralDiversityArray = spectralDiversityArray;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Network getBestNetwork() {
        return bestNetwork;
    }

    public void setBestNetwork(Network bestNetwork) {
        this.bestNetwork = bestNetwork;
    }

    public int getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness() {
        int bestFitness = 0;
        for(Network n: this.members){
            if (n.getFitness() > bestFitness){
                bestFitness = n.getFitness();
            }
        }
    }

    public int getAvgFitness() {
        return avgFitness;
    }

    public void setAvgFitness(int avgFitness) {
        int sum = 0;
        for(Network n: this.members){
            sum += n.getFitness();
        }
        this.avgFitness = sum / this.members.size();
    }


}
