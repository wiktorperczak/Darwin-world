package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Simulation implements Runnable{
    RectangularMap map;
    public Simulation(List<Vector2d> animalsStartingPos, WorldMap map){
        this.map = (RectangularMap) map;
        for (Vector2d position : animalsStartingPos) {
            this.map.place(new Animal(position));
        }
        System.out.println(map);
    }

    public void run(){
        while(!map.getAnimals().isEmpty()) {
            removeDeadBodies();
            if (map.getAnimals().isEmpty()){
                break;
            }
            moveAnimals();
            map.mapChanged("Zwierzaki się ruszyły");
        }
        System.out.println("Wszystkie zwierzęta umarły");
    }

    void removeDeadBodies(){
        List<WorldElement> animals = new ArrayList<>(map.getAnimals().keySet());
        List<Animal> animalsToRemove = new ArrayList<>();
        if (animals.isEmpty()) return;

        for (WorldElement worldElement : animals){
            Animal animal = (Animal) worldElement;
            if (!animal.isAlive()){
                animalsToRemove.add(animal);
            }
        }
        for (Animal animal : animalsToRemove){
            map.removeAnimal(animal);
        }

        if (!animalsToRemove.isEmpty()) map.updateAllElements();
    }
    void moveAnimals(){
        List<WorldElement> animals = new ArrayList<>(map.getAnimals().keySet());
        if (animals.isEmpty()) return;
        for (WorldElement worldElement : animals) {
            Animal animal = (Animal) worldElement;
            map.move(animal);
        }

        map.updateAllElements();
    }
}
