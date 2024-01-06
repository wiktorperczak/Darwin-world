package model;

public final class OptionsManager {
    private static OptionsManager instance;

    public static OptionsManager getInstance(){
        if (instance == null){
            instance = new OptionsManager();
        }
        return instance;
    }
    int animalLife;
    int genotypeLength;
    int minimalEnergyToBreed;
    int energyLossOnBreed;
    int gensToMutate;

    int grassEnergy;

    public int getGenotypeLength() {
        return genotypeLength;
    }

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
    public void setGensToMutate(int value){gensToMutate = value;}
    public int getGensToMutate(){ return gensToMutate;}

}
