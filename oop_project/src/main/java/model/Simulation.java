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
        System.out.println(this.map);

        this.map.updateAllElements();
        System.out.println(this.map.allElements.size());
        this.map.mapChanged("Start");
    }

    public void run(){
        map.mapChanged("Zwierzaki sie ruszyly");
        while(!map.getAnimals().isEmpty()) {
            removeDeadBodies();
            if (map.getAnimals().isEmpty()){
                break;
            }
            moveAnimals();
            System.out.println("TEST");
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
        System.out.println(map.getAnimals().size());
        List<WorldElement> animals = new ArrayList<>(map.getAnimals().keySet());
        if (animals.isEmpty()) return;
        for (WorldElement worldElement : animals) {
            Animal animal = (Animal) worldElement;
            map.move(animal);
        }

//        map.updateAllElements();
//        map.mapChanged("Wszystkie zmiany skonczone");
    }
}
