package com.component.cm;

import com.component.IComponent;
import com.component.IObserver;
import com.dto.*;
import com.mediator.IMediator;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

public class ContextManagementComponent implements IComponent, IObserver {

    private IMediator mediator;

    private Pane view;
    private final String name = "CM";

    private Label positionLabel;

    private ContextSituation contextSituation;
    private Set<Slider> sliders;

    private int frequency = 2;
    private Timer timer;

    private static final DecimalFormat df = new DecimalFormat("#.######");

    public ContextManagementComponent(IMediator mediator) {
        this.mediator = mediator;
        timer = new Timer(frequency * 1000, event -> mediator.notify(this, MessageType.FROM_CM, contextSituation));
        initView();
    }

    private void initView() {
        sliders = new HashSet<>();

        BorderPane mainPane = new BorderPane();

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> start());

        VBox containerVBox = new VBox();
        positionLabel = new Label("");
        VBox frequencyVBox = createSliderVBox(ContextKey.FREQUENCY, 1, 10, 2, 1);
        VBox temperatureVBox = createSliderVBox(ContextKey.TEMPERATURE, -40, 50, 15, 10);
        VBox velocityVBox = createSliderVBox(ContextKey.VELOCITY, 0, 200, 0, 20);
        VBox fuelLevelVBox = createSliderVBox(ContextKey.FUEL_LEVEL, 0, 100, 70, 10);

        containerVBox.getChildren().addAll(startButton, new Label("Position: "), positionLabel, frequencyVBox, temperatureVBox, velocityVBox, fuelLevelVBox);
        mainPane.setCenter(containerVBox);

        view = mainPane;
    }

    private void start() {
        if (contextSituation == null) {
            initContextSituation();
        }
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private void initContextSituation() {
        contextSituation = new ContextSituation();
        // TODO: add context elements
        ContextPosition contextPosition = new ContextPosition();
        contextPosition.setContextKey(ContextKey.POSITION);
        contextPosition.setLatitude(48.33874658333333);
        contextPosition.setLongitude(14.409790966666666);
        contextSituation.addContext(contextPosition);

        // update gui
        positionLabel.setText(df.format(contextPosition.getLatitude()) + "\n" + df.format(contextPosition.getLongitude()));
    }

    private VBox createSliderVBox(ContextKey contextKey, int min, int max, int value, int unit) {
        VBox vBox = new VBox();
        Label label = new Label(contextKey.name());
        Slider slider = new Slider();
        slider.setMin(min);
        slider.setMax(max);
        slider.setValue(value);
        slider.setMajorTickUnit(unit);
        slider.setMinorTickCount(5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setSnapToTicks(true);
        slider.setOnMouseReleased(event -> valueChanged(contextKey, slider.getValue()));
        sliders.add(slider);
        vBox.getChildren().addAll(label, slider);
        return vBox;
    }

    private void valueChanged(ContextKey contextKey, double value) {
        ContextElement contextElement = null;
        switch (contextKey) {
            case TEMPERATURE:
                ContextTemperature contextTemperature = new ContextTemperature();
                contextTemperature.setContextKey(contextKey);
                contextTemperature.setTemperature(value);
                contextElement = contextTemperature;
                break;
            case VELOCITY:
                ContextVelocity contextVelocity = new ContextVelocity();
                contextVelocity.setContextKey(contextKey);
                contextVelocity.setVelocity(value);
                contextElement = contextVelocity;
            default:
                break;
        }
        if (contextElement != null) {
            contextSituation.addContext(contextElement);
            mediator.notify(this, MessageType.FROM_CM, contextSituation);
        }
    }

    @Override
    public Pane getView() {
        return view;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(MessageType messageType, Object data) {
        if (contextSituation != null) {
            switch (messageType) {
                case FROM_GPS: {
                    updateGPS(data);
                    break;
                }
                case FROM_AAL: {
                    updateAAL(data);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void updateGPS(Object data) {
        // update context situation
        PositionPOI positionPOI = (PositionPOI) data;
        ContextPosition contextPosition = new ContextPosition();
        contextPosition.setContextKey(ContextKey.POSITION);
        contextPosition.setLatitude(positionPOI.getLatitude());
        contextPosition.setLongitude(positionPOI.getLongitude());
        contextSituation.addContext(contextPosition);

        // update gui
        Platform.runLater(() -> positionLabel.setText(df.format(contextPosition.getLatitude()) + "\n" + df.format(contextPosition.getLongitude())));

        // notify mediator
        mediator.notify(this, MessageType.FROM_CM, contextSituation);
    }

    private void updateAAL(Object data) {
        // update context situation
        ContextElement contextElement = (ContextElement) data;
        contextSituation.addContext(contextElement);

        // update gui


        // notify mediator
        mediator.notify(this, MessageType.FROM_CM, contextSituation);
    }
}
