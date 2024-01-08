package model;

public final class OptionsManager {
    int numberOfAnimals;
    int width;
    int height;
    int animalLife;
    int genotypeLength;
    int minimalEnergyToBreed;
    int energyLossOnBreed;
    int maxGensToMutate;
    int minGensToMutate;
    int grassEnergy;
    int numberOfTunnels;
    boolean useTunnels;
    boolean useReverseGenotype;
    int numberOfGrassPerDay;
    int startingGrassNumber;

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
    public int getNumberOfGrassPerDay() { return numberOfGrassPerDay; }
    public void setNumberOfGrassPerDay(int numberOfGrassPerDay) { this.numberOfGrassPerDay = numberOfGrassPerDay; }
    public int getStartingGrassNumber() { return startingGrassNumber; }
    public void setStartingGrassNumber(int startingGrassNumber) { this.startingGrassNumber = startingGrassNumber; }
}
