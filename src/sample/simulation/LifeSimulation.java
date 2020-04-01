package sample.simulation;

import sample.grid.CurrentGrid;

public class LifeSimulation {
    public void generateNextGeneration(CurrentGrid currentGrid) {
        int[][] oldGrid = currentGrid.getGrid();
        int[][] grid = new int[currentGrid.getHeight()][currentGrid.getWidth()];

        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                int livingNeighbours = countLivingNeighbours(i, j, oldGrid);
                if (currentGrid.getCell(i, j) == 0) {
                    if (livingNeighbours == 3)
                        grid[i][j] = 1;
                    else
                        grid[i][j] = 0;
                } else {
                    if(livingNeighbours < 2 || livingNeighbours > 3)
                        grid[i][j] = 0;
                    else
                        grid[i][j] = 1;
                }
            }
        }
        currentGrid.setGrid(grid);
    }

    private int countLivingNeighbours(int i, int j, int[][] oldGrid) {
        int count = 0;
        for (int k = i-1; k <= i+1; k++) {
            for (int l = j-1; l <= j+1; l++) {
                int heightIndex = k < 0 ? oldGrid.length-1 : k;
                if(heightIndex > oldGrid.length-1)
                    heightIndex = 0;
                int widthIndex = l < 0 ? oldGrid[0].length-1 : l;
                if(widthIndex > oldGrid[0].length-1)
                    widthIndex = 0;
                if (oldGrid[heightIndex][widthIndex] == 1 && !(heightIndex == i && l == j))
                    count++;
            }
        }
        return count;
    }
}
