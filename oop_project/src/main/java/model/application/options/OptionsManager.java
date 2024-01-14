package model.application.options;

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
    private boolean useTunnels;
    private boolean useReverseGenotype;
    private int numberOfGrassPerDay;
    private int startingGrassNumber;
    private int simulationSpeed;

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
    public void setSimulationSpeed(int simulationSpeed){ this.simulationSpeed = simulationSpeed; }
    public int getSimulationSpeed(){ return simulationSpeed; }

    public void generateBasicValues() {
        setNumberOfAnimals(10);
        setWidth(5);
        setHeight(5);
        setAnimalLife(10);
        setGenotypeLength(5);
        setMinimalEnergyToBreed(5);
        setEnergyLossOnBreed(2);
        setMaxGensToMutate(5);
        setMinGensToMutate(1);
        setGrassEnergy(5);
        setNumberOfTunnels(1);
        setUseTunnels(false);
        setUseReverseGenotype(true);
        setNumberOfGrassPerDay(10);
        setStartingGrassNumber(10);
        setSimulationSpeed(5);
    }
}
