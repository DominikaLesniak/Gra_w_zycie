package sample.simulation;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.grid.CurrentGrid;

public class CellPainter {
    private GraphicsContext gc;
    private int scale;

    public CellPainter(GraphicsContext gc, int scale) {
        this.gc = gc;
        this.scale = scale;
    }

    public void repaintCellFromClickedPixel(CurrentGrid currentGrid, int i, int j) {
        int iResidue = i % scale;
        int jResidue = j % scale;
        if(iResidue == 0 || jResidue == 0)
            return;
        int iStartPixel = i - iResidue;
        int jStartPixel = j - jResidue;
        int y = iStartPixel / currentGrid.getScale();
        int x = jStartPixel / currentGrid.getScale();
        if (x >= currentGrid.getGrid().length || y >= currentGrid.getGrid()[0].length)
            return;
        currentGrid.reverseCell(x, y);
        int value = currentGrid.getCell(x, y);
        paintCell(iStartPixel, jStartPixel, value);
    }

    public void paintCurrentGridCells(CurrentGrid currentGrid) {
        for (int i = 0; i < currentGrid.getHeight(); i++) {
            for (int j = 0; j < currentGrid.getWidth(); j++) {
                int value = currentGrid.getCell(i, j);
                int iStartPixel = i * currentGrid.getScale();
                int jStartPixel = j * currentGrid.getScale();
                paintCell(jStartPixel, iStartPixel, value);
            }
        }
    }

    private void paintCell(int i, int j, int value) {
        Color color = getColorForValue(value);
        gc.setFill(color);
        gc.fillRect(i, j,
                19, 19);
    }

    private Color getColorForValue(int value) {
        if(value == 1)
            return Color.BLACK;
        else
            return Color.WHITE;
    }
    
    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}