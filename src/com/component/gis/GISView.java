package com.component.gis;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

import java.awt.image.BufferedImage;

public class GISView extends BorderPane implements IDataObserver {

    private GISController gisController;

    private BufferedImage bufferedImage;

    private Canvas canvas;

    private static final String BUTTON_LOAD_ID = "gis_load";
    private static final String BUTTON_ZOOM_TO_FIT_ID = "gis_ztf";
    private static final String BUTTON_ZOOM_IN_ID = "gis_zin";
    private static final String BUTTON_ZOOM_OUT_ID = "gis_zout";
    private static final String BUTTON_NORTH_ID = "gis_north";
    private static final String BUTTON_SOUTH_ID = "gis_south";
    private static final String BUTTON_EAST_ID = "gis_east";
    private static final String BUTTON_WEST_ID = "gis_west";

    private static final double ZOOM_SCALE = 1.3;
    public static final int DRAG_STEP = 20;

    GISView(GISController gisController) {
        this.gisController = gisController;
        initView();
    }

    private void initView() {
        FlowPane buttonPane = new FlowPane();
        buttonPane.setStyle("-fx-background-color:blue;-fx-padding:5");

        Button button = new Button("Load");
        button.setId(BUTTON_LOAD_ID);
        button.setOnAction(event -> gisController.loadData());
        buttonPane.getChildren().add(button);
        button = new Button("ZTF");
        button.setOnAction(event -> gisController.zoomToFit());
        button.setId(BUTTON_ZOOM_TO_FIT_ID);
        buttonPane.getChildren().add(button);
        button = new Button("+");
        button.setId(BUTTON_ZOOM_IN_ID);
        button.setOnAction(event -> gisController.zoom(ZOOM_SCALE));
        buttonPane.getChildren().add(button);
        button = new Button("-");
        button.setId(BUTTON_ZOOM_OUT_ID);
        button.setOnAction(event -> gisController.zoom(1.0 / ZOOM_SCALE));
        buttonPane.getChildren().add(button);
        button = new Button("N");
        button.setId(BUTTON_NORTH_ID);
        button.setOnAction(event -> gisController.drag(0, -DRAG_STEP));
        buttonPane.getChildren().add(button);
        button = new Button("S");
        button.setId(BUTTON_SOUTH_ID);
        button.setOnAction(event -> gisController.drag(0, DRAG_STEP));
        buttonPane.getChildren().add(button);
        button = new Button("E");
        button.setId(BUTTON_EAST_ID);
        button.setOnAction(event -> gisController.drag(DRAG_STEP, 0));
        buttonPane.getChildren().add(button);
        button = new Button("W");
        button.setId(BUTTON_WEST_ID);
        button.setOnAction(event -> gisController.drag(-DRAG_STEP, 0));
        buttonPane.getChildren().add(button);


        StackPane canvasPane = new StackPane();
        canvasPane.setMinSize(0, 0);

        canvas = new Canvas();

        canvasPane.getChildren().add(canvas);

        canvas.widthProperty().bind(canvasPane.widthProperty());
        canvas.heightProperty().bind(canvasPane.heightProperty());

        canvasPane.widthProperty().addListener((observable, oldValue, newValue) -> gisController.canvasPaneSizeChanged(observable));
        canvasPane.heightProperty().addListener((observable, oldValue, newValue) -> gisController.canvasPaneSizeChanged(observable));

        this.setBottom(buttonPane);
        this.setCenter(canvasPane);
        this.prefWidth(640);
        this.prefHeight(480);
    }

    @Override
    public void update(BufferedImage bufferedImage) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.restore();
        this.bufferedImage = bufferedImage;
        repaint();
    }

    private void repaint() {
        WritableImage writable = SwingFXUtils.toFXImage(bufferedImage, null);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.drawImage(writable, 0, 0);
    }
}
