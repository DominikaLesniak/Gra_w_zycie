package sample.grid;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GridTemplates {
    private final static List<String> TEMPLATES = Arrays.asList("komp. własna", "niezmienne", "glider", "oscylator", "losowy");

    public static List<String> getTemplates() {
        return TEMPLATES;
    }

    public static CurrentGrid getGridForTemplate(String template, int width, int height) throws Exception {
        switch (template) {
            case "komp. własna":
                return new CurrentGrid(width, height);
            case "niezmienne":
                return getUnchangeableGrid(width, height);
            case "glider":
                return getGliderGrid(width, height);
            case "oscylator":
                return getOscilatorGrid(width, height);
            case "losowy":
                return getRandomGrid(width, height);
            default:
                throw new Exception("Nieznany wzorzec");
        }
    }

    private static CurrentGrid getUnchangeableGrid(int width, int height) {
        if(width < 6)
            width = 6;
        if(height < 5)
            height = 5;
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        int widthHalf = width/2;
        int heightHalf = height/2-1;
        currentGrid.reverseCell(heightHalf, widthHalf);
        currentGrid.reverseCell(heightHalf, widthHalf+1);
        currentGrid.reverseCell(heightHalf+1, widthHalf-1);
        currentGrid.reverseCell(heightHalf+1, widthHalf+2);
        currentGrid.reverseCell(heightHalf+2, widthHalf);
        currentGrid.reverseCell(heightHalf+2, widthHalf+1);
        return currentGrid;
    }

    private static CurrentGrid getGliderGrid(int width, int height){
        if(width < 5)
            width = 5;
        if(height < 5)
            height = 5;
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        int widthHalf = width/2;
        int heightHalf = height/2-1;
        currentGrid.reverseCell(heightHalf, widthHalf+1);
        currentGrid.reverseCell(heightHalf, widthHalf+2);
        currentGrid.reverseCell(heightHalf+1, widthHalf);
        currentGrid.reverseCell(heightHalf+1, widthHalf+1);
        currentGrid.reverseCell(heightHalf+2, widthHalf+2);
        return currentGrid;
    }

    private static CurrentGrid getOscilatorGrid(int width, int height) {
        if(width < 3)
            width = 3;
        if(height < 5)
            height = 5;
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        int widthHalf = width/2;
        int heightHalf = height/2-1;
        currentGrid.reverseCell(heightHalf, widthHalf+1);
        currentGrid.reverseCell(heightHalf+1, widthHalf+1);
        currentGrid.reverseCell(heightHalf+2, widthHalf+1);
        return currentGrid;
    }

    private static CurrentGrid getRandomGrid(int width, int height) {
        CurrentGrid currentGrid = new CurrentGrid(width, height);
        Random random = new Random();
        int numOfRandoms = width*height / 3;
        double[] randomDoubles = random.doubles(numOfRandoms).toArray();
        for (int i = 0; i < randomDoubles.length - 1; i+=2) {
            int x = (int) (randomDoubles[i]*height);
            int y = (int) (randomDoubles[i+1] * width);
            currentGrid.reverseCell(x, y);
        }
        return currentGrid;
    }
}