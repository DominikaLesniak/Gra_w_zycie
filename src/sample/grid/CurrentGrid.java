package sample.grid;

import java.util.function.Function;

public class CurrentGrid {
    private final Function<Integer, Integer> REVERSE_VALUE = val -> (val+1) % 2;
    private int width;
    private int height;
    private int scale;

    private int[][] grid;

    public CurrentGrid(int width, int height) {
        this.width = width;
        this.height = height;
        this.scale = 20;
        this.grid = initiateGrid(this.height, this.width);
    }

    public CurrentGrid(int width, int height, int scale) {
        this.width = width/scale;
        this.height = height/scale;
        this.scale = scale;
        this.grid = initiateGrid(this.height, this.width);
    }

    public void reverseCell(int i, int j) {
        int newValue = REVERSE_VALUE.apply(grid[i][j]);
        this.grid[i][j] = newValue;
    }

    public void resizeGrid(int newWidth, int newHeight) {
        int[][] newGrid = initiateGrid(newHeight, newWidth);

        int widthBorder = Math.min(newWidth, width);
        int heightBorder = Math.min(newHeight, height);

        for (int i = 0; i < heightBorder; i++) {
            for (int j = 0; j < widthBorder; j++) {
                newGrid[i][j] = grid[i][j];
            }
        }
        print();
        this.height = newHeight;
        this.width = newWidth;
        this.grid = newGrid;
        print();
    }

    private int[][] initiateGrid(int height, int width) {
        int[][]grid = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = 0;
            }
        }
        return grid;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getScale() {
        return scale;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getCell(int i, int j) {
        return grid[i][j];
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public void print() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}