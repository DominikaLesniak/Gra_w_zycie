package sample.view;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.grid.CurrentGrid;
import sample.grid.GridTemplates;
import sample.simulation.CellPainter;
import sample.simulation.LifeSimulation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private static int CANVAS_HEIGHT;
    private static int CANVAS_WIDTH;
    private static int SCALE;

    @FXML
    private Canvas canvas;
    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private Button resizeButton;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField iterationsTextField;

    private CellPainter painter;
    private GraphicsContext gc;
    private CurrentGrid currentGrid;
    private LifeSimulation lifeSimulation;
    private Timeline timeline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CANVAS_HEIGHT = (int) canvas.getHeight();
        CANVAS_WIDTH = (int) canvas.getWidth();
        SCALE = 20;
        WritableImage snap = canvas.snapshot(null, null);
        gc = canvas.getGraphicsContext2D();
        currentGrid = new CurrentGrid(CANVAS_WIDTH, CANVAS_HEIGHT, SCALE);
        painter = new CellPainter(gc, SCALE);
        lifeSimulation = new LifeSimulation();
        drawGridLines(CANVAS_WIDTH, CANVAS_HEIGHT);

        setButtons();
        prepareChoiceBox();
        activateCanvas();
        timeline = new Timeline();
        timeline.setOnFinished(event -> {
            System.err.println("done");
            timeline.stop();
            timeline.getKeyFrames().clear();
            /*timeline = new Timeline();*/
        });
    }

    private void setButtons() {
        startButton.setOnAction(event -> {
            boolean iterationNumberFixed = setNumberOfIterations();
            if (!iterationNumberFixed) {
                timeline.setCycleCount(Timeline.INDEFINITE);
            } else
                timeline.setCycleCount(1);
            timeline.play();

            canvas.setOnMouseClicked(event1 -> {
            });
        });
        stopButton.setOnAction(event -> {
            timeline.stop();
            timeline.getKeyFrames().clear();
            activateCanvas();
        });
        resizeButton.setOnAction(event -> {
            resizeGrid();
        });
    }

    private boolean setNumberOfIterations() {
        String input = iterationsTextField.getText();
        boolean inputEmpty = input.isEmpty();
        int iterations = inputEmpty ? 1 : Integer.parseInt(input);
        Duration delayBetweenMessages = Duration.seconds(1);
        Duration frame = delayBetweenMessages;
        timeline.setCycleCount(1);
        for (int i = 0; i < iterations; i++) {
            timeline.getKeyFrames().add(new KeyFrame(frame, e -> lifeSimulation.generateNextGeneration(currentGrid)));
            timeline.getKeyFrames().add(new KeyFrame(frame, e -> painter.paintCurrentGridCells(currentGrid)));
            if(!inputEmpty) {
                String iterationsLeft = i != iterations-1 ? String.valueOf(iterations-i-1) : "";
                timeline.getKeyFrames().add(new KeyFrame(frame, e -> iterationsTextField.setText(iterationsLeft)));
            }
            frame = frame.add(delayBetweenMessages);
        }
        return !inputEmpty;
    }

    private void prepareChoiceBox() {
        List<String> templates = GridTemplates.getTemplates();
        ObservableList list = FXCollections.observableArrayList(templates);
        choiceBox.setItems(list);
        choiceBox.setValue(list.get(0));
        choiceBox.setOnAction(event -> {
            String value = (String) choiceBox.getValue();
            try {
                currentGrid = GridTemplates.getGridForTemplate(value, currentGrid.getWidth(), currentGrid.getHeight());
                painter.paintCurrentGridCells(currentGrid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void activateCanvas() {
        canvas.setOnMouseClicked(event -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            widthTextField.setText(x + "");
            heightTextField.setText(y + "");
            painter.repaintCellFromClickedPixel(currentGrid, x, y);
        });
    }

    private void drawGridLines(double width, double height) {
        gc.setStroke(Color.BLACK);
        for (int x = 0; x <= width; x += SCALE) {
            gc.strokeLine(x, 0, x, height);
        }

        for (int y = 0; y <= height; y += SCALE) {
            gc.strokeLine(0, y, width, y);
        }
    }

    private void resizeGrid() {
        int width = getWidth();
        int height = getHeight();
        if (currentGrid.getWidth() != width / SCALE || currentGrid.getHeight() != height / SCALE)
            currentGrid.resizeGrid(width / SCALE, height / SCALE);
        drawGridLines(width, height);
        gc.setFill(Color.WHITE);
        if (width < CANVAS_WIDTH)
            gc.fillRect((width - width%SCALE), 0,
                    CANVAS_WIDTH + (width + width%SCALE),
                    CANVAS_HEIGHT);
        if (height < CANVAS_HEIGHT)
            gc.fillRect(0, (height - height%SCALE),
                    CANVAS_WIDTH,
                    CANVAS_HEIGHT + (height + height%SCALE));
        widthTextField.setText(width - width%SCALE + "");
        heightTextField.setText(height - height%SCALE + "");
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0,
                gc.getCanvas().getWidth(),
                gc.getCanvas().getHeight());
    }

    private Integer getHeight() {
        String input = heightTextField.getText();
        if (!input.isEmpty() && isNumeric(input)) {
            int height = Integer.parseInt(input);
            if (height <= 0)
                return 1;
            if (height <= CANVAS_HEIGHT)
                return height;
        }
        return CANVAS_HEIGHT;
    }

    private int getWidth() {
        String input = widthTextField.getText();
        if (!input.isEmpty() && isNumeric(input)) {
            int width = Integer.parseInt(input);
            if (width <= 0)
                return 1;
            if (width <= CANVAS_WIDTH)
                return width;
        }
        return CANVAS_WIDTH;
    }

    private boolean isNumeric(String value) {
        String number = value.replaceAll("\\s+", "");
        for (int i = 0; i < number.length(); i++) {
            if (!((int) number.charAt(i) >= 47 && (int) number.charAt(i) <= 57)) {
                return false;
            }
        }
        return true;
    }
}
