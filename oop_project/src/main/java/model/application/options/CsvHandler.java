package model.application.options;

import model.map.RectangularMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvHandler {

    private RectangularMap map;

    public CsvHandler(RectangularMap map) {
        this.map = map;
    }

    public void createCsvFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Nagłówki kolumn
            writer.write("Liczba zwierzaków,Liczba pól z trawą,Liczba pustych pól, Najpopularniejszy gen, " +
                    "Średnia energia, Średnia liczba dzieci, Średni czas życia martwych zwierzaków");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void appendRowToCsv(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            StringBuilder rowBuilder = new StringBuilder();
//            rowBuilder.append(life);append(",").append(genotype).append(",").append(dead);
            rowBuilder.append(map.statsHandler.getNumberOfAnimals()).append(",");
            rowBuilder.append(map.statsHandler.getNumberOfGrass()).append(",");
            rowBuilder.append(map.statsHandler.getNumberOfFreeSpaces()).append(",");
            rowBuilder.append(map.statsHandler.getMostPopularGen()).append(",");
            rowBuilder.append(map.statsHandler.getAverageEnergy()).append(",");
            rowBuilder.append(map.statsHandler.getAverageNumberOfKids()).append(",");
            rowBuilder.append(map.statsHandler.getAverageLengthOfDeadAnimals()).append(",");
            writer.write(rowBuilder.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
