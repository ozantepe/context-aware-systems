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
import java.util.HashMap;
import java.util.Map;

public class ContextManagementComponent implements IComponent, IObserver {

  private IMediator mediator;

  private Pane view;
  private final String name = "CM";

  private ContextSituation contextSituation;
  private Map<ContextKey, Slider> sliders;
  private Map<ContextKey, Label> labels;

  private int frequency;
  private Timer timer;

  private static final DecimalFormat df = new DecimalFormat("#.######");
  private static final double DEFAULT_LATITUDE = 48.33874658333333;
  private static final double DEFAULT_LONGITUDE = 14.409790966666666;

  public ContextManagementComponent(IMediator mediator) {
    this.mediator = mediator;
    timer =
            new Timer(
                    frequency * 1000,
                    event -> mediator.notify(this, MessageType.FROM_CM, contextSituation));
    initView();
  }

  private void initView() {
    sliders = new HashMap<>();
    labels = new HashMap<>();

    BorderPane mainPane = new BorderPane();
    VBox containerVBox = new VBox();

    Button startButton = new Button("Start");
    startButton.setOnAction(event -> start());

    // Position
    VBox positionVBox = new VBox();
    Button positionButton = new Button("Reset position");
    positionButton.setOnAction(event -> resetPosition());
    Label positionLabel = new Label(ContextKey.POSITION.getText());
    labels.put(ContextKey.POSITION, positionLabel);
    positionVBox.getChildren().addAll(positionButton, positionLabel);

    // Time
    VBox timeVBox = new VBox();
    Button timeButton = new Button("Get current time");
    timeButton.setOnAction(event -> getCurrentTime());
    Label timeLabel = new Label(ContextKey.TIME.getText());
    labels.put(ContextKey.TIME, timeLabel);
    timeVBox.getChildren().addAll(timeButton, timeLabel);

    // Sliders
    VBox frequencyVBox = createSliderVBox(ContextKey.FREQUENCY, 1, 10, 2, 1);
    VBox temperatureVBox = createSliderVBox(ContextKey.TEMPERATURE, -40, 50, 15, 10);
    VBox velocityVBox = createSliderVBox(ContextKey.VELOCITY, 0, 200, 0, 20);
    //        VBox fuelLevelVBox = createSliderVBox(ContextKey.FUEL_LEVEL, 0, 100, 70, 10);

    containerVBox
            .getChildren()
            .addAll(startButton, temperatureVBox, velocityVBox, frequencyVBox, positionVBox, timeVBox);
    containerVBox.setSpacing(20);
    mainPane.setCenter(containerVBox);

    view = mainPane;
  }

  private void start() {
    if (contextSituation == null) {
      initContextSituation();
    }
    if (!timer.isRunning()) {
      sliders.forEach((contextKey, slider) -> slider.setDisable(false));
      timer.start();
    }
  }

  private void initContextSituation() {
    contextSituation = new ContextSituation();

    // Position
    ContextPosition contextPosition = new ContextPosition();
    contextPosition.setContextKey(ContextKey.POSITION);
    contextPosition.setLatitude(DEFAULT_LATITUDE);
    contextPosition.setLongitude(DEFAULT_LONGITUDE);
    contextSituation.addContext(contextPosition);
    updatePositionGUI(contextPosition);

    // Time
    ContextTime contextTime = new ContextTime();
    contextTime.setContextKey(ContextKey.TIME);
    contextTime.initCurrentTime();
    contextSituation.addContext(contextTime);
    updateTimeGUI(contextTime);

    // Temperature
    ContextTemperature contextTemperature = new ContextTemperature();
    contextTemperature.setContextKey(ContextKey.TEMPERATURE);
    contextTemperature.setTemperature(sliders.get(ContextKey.TEMPERATURE).getValue());
    contextSituation.addContext(contextTemperature);
    updateSlidersGUI(contextTemperature);

    // Velocity
    ContextVelocity contextVelocity = new ContextVelocity();
    contextVelocity.setContextKey(ContextKey.VELOCITY);
    contextVelocity.setVelocity(sliders.get(ContextKey.VELOCITY).getValue());
    contextSituation.addContext(contextVelocity);
    updateSlidersGUI(contextVelocity);

    // Frequency
    frequency = 2;
    labels.get(ContextKey.FREQUENCY).setText(ContextKey.FREQUENCY.getText() + ": " + frequency);
  }

  private void resetPosition() {
    ContextPosition contextPosition = new ContextPosition();
    contextPosition.setContextKey(ContextKey.POSITION);
    contextPosition.setLatitude(DEFAULT_LATITUDE);
    contextPosition.setLongitude(DEFAULT_LONGITUDE);
    contextSituation.addContext(contextPosition);
    updatePositionGUI(contextPosition);
  }

  private void getCurrentTime() {
    ContextTime contextTime = new ContextTime();
    contextTime.setContextKey(ContextKey.TIME);
    contextTime.initCurrentTime();
    contextSituation.addContext(contextTime);
    updateTimeGUI(contextTime);
  }

  private VBox createSliderVBox(ContextKey contextKey, int min, int max, int value, int unit) {
    VBox vBox = new VBox();
    Label label = new Label(contextKey.getText());
    labels.put(contextKey, label);
    Slider slider = new Slider();
    slider.setMin(min);
    slider.setMax(max);
    slider.setValue(value);
    slider.setMajorTickUnit(unit);
    slider.setMinorTickCount(5);
    slider.setShowTickLabels(true);
    slider.setShowTickMarks(true);
    slider.setSnapToTicks(true);
    slider.setDisable(true);
    slider.setOnMouseReleased(event -> valueChanged(contextKey, slider.getValue()));
    sliders.put(contextKey, slider);
    vBox.getChildren().addAll(label, slider);
    return vBox;
  }

  /* Slider value change handler */
  private void valueChanged(ContextKey contextKey, double value) {
    ContextElement contextElement = null;
    switch (contextKey) {
      case TEMPERATURE:
        ContextTemperature contextTemperature = new ContextTemperature();
        contextTemperature.setContextKey(contextKey);
        contextTemperature.setTemperature(value);
        contextElement = contextTemperature;
        labels
                .get(contextKey)
                .setText(contextKey.getText() + ": " + contextElement.getValueAsInt());
        break;
      case VELOCITY:
        ContextVelocity contextVelocity = new ContextVelocity();
        contextVelocity.setContextKey(contextKey);
        contextVelocity.setVelocity(value);
        contextElement = contextVelocity;
        labels
                .get(contextKey)
                .setText(contextKey.getText() + ": " + contextElement.getValueAsInt());
        break;
      case FREQUENCY:
        frequency = (int) value;
        timer.setDelay(frequency * 1000);
        labels.get(contextKey).setText(contextKey.getText() + ": " + (int) value);
        break;
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
    updatePositionGUI(contextPosition);

    // notify mediator
    mediator.notify(this, MessageType.FROM_CM, contextSituation);
  }

  private void updateAAL(Object data) {
    ContextElement contextElement = (ContextElement) data;
    contextSituation.addContext(contextElement);
    ContextKey contextKey = contextElement.getContextKey();
    switch (contextKey) {
      case POSITION:
        updatePositionGUI((ContextPosition) contextElement);
        break;
      case TEMPERATURE:
        updateSlidersGUI(contextElement);
        break;
      case TIME:
        updateTimeGUI((ContextTime) contextElement);
        break;
      case VELOCITY:
        updateSlidersGUI(contextElement);
        break;
      default:
        break;
    }

    // notify mediator
    mediator.notify(this, MessageType.FROM_CM, contextSituation);
  }

  private void updatePositionGUI(ContextPosition contextPosition) {
    Platform.runLater(
            () ->
                    labels
                            .get(contextPosition.getContextKey())
                            .setText(
                                    df.format(contextPosition.getLatitude())
                                            + "\n"
                                            + df.format(contextPosition.getLongitude())));
  }

  private void updateTimeGUI(ContextTime contextTime) {
    Platform.runLater(
            () ->
                    labels
                            .get(contextTime.getContextKey())
                            .setText(
                                    String.format(
                                            "Time: %sh %sm %ss",
                                            contextTime.getHours(),
                                            contextTime.getMinutes(),
                                            contextTime.getSeconds())));
  }

  private void updateSlidersGUI(ContextElement contextElement) {
    ContextKey contextKey = contextElement.getContextKey();
    int value = contextElement.getValueAsInt();

    Slider slider = sliders.get(contextKey);
    slider.setValue(contextElement.getValueAsInt());

    Label label = labels.get(contextKey);
    label.setText(contextKey.getText() + ": " + value);
  }
}
