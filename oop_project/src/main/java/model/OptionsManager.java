package model;

public final class OptionsManager {
    private int numberOfAnimals;
    private int width;
    private int height;
    private int animalLife;
    private int genotypeLength;
    private int minimalEnergyToBreed;
    private int energyLossOnBreed;
    private int maxGensToMutate;
    private int minGensToMutate;
    private int grassEnergy;
    private int numberOfTunnels;

    int numberOfAnimals;
    int width;
    int height;
    int animalLife;
    int genotypeLength;
    int minimalEnergyToBreed;
    int energyLossOnBreed;
    int gensToMutate;
    int grassEnergy;
    int numberOfTunnels;
    boolean useTunnels;
    boolean useReverseGenotype;

    public int getNumberOfAnimals() { return numberOfAnimals; }

    public void setNumberOfAnimals(int numberOfAnimals) { this.numberOfAnimals = numberOfAnimals; }
    public void setGenotypeLength(int genotypeLength) {
        this.genotypeLength = genotypeLength;
    }
    public int getGenotypeLength() { return genotypeLength;}
    public void setAnimalLife(int animalLife){
        this.animalLife = animalLife;
    }
    public int getAnimalLife(){
        return animalLife;
    }

    public int getGrassEnergy() { return grassEnergy; }
    public void setGrassEnergy(int grassEnergy) { this.grassEnergy = grassEnergy; }
    public void setMinimalEnergyToBreed(int value){minimalEnergyToBreed = value;}
    public int getMinimalEnergyToBreed(){ return minimalEnergyToBreed;}
    public void setEnergyLossOnBreed(int value){ energyLossOnBreed = value;}
    public int getEnergyLossOnBreed(){ return energyLossOnBreed;}
    public void setMaxGensToMutate(int value){maxGensToMutate = value;}
    public int getMaxGensToMutate(){ return maxGensToMutate;}
    public void setMinGensToMutate(int value){minGensToMutate = value;}
    public int getMinGensToMutate(){ return minGensToMutate;}
    public int getNumberOfTunnels() { return numberOfTunnels; }
    public void setNumberOfTunnels(int numberOfTunnels) { this.numberOfTunnels = numberOfTunnels; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public void setUseTunnels(boolean useTunnels) { this.useTunnels = useTunnels; }
    public boolean getUseTunnels() { return useTunnels; }
    public void setUseReverseGenotype(boolean useReverseGenotype) { this.useReverseGenotype = useReverseGenotype; }
    public boolean getUseReverseGenotype() { return useReverseGenotype; }
}
