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

    public int getGenotypeLength() {
        return genotypeLength;
    }

    public void setGenotypeLength(int genotypeLength) {
        this.genotypeLength = genotypeLength;
    }

    public void setAnimalLife(int animalLife){
        this.animalLife = animalLife;
    }
    public int getAnimalLife(){
        return animalLife;
    }
}
