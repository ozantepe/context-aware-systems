package com.component.aal;

import com.component.IComponent;
import com.component.aal.parser.ContextDomParser;
import com.component.aal.parser.ContextSaxParser;
import com.component.aal.parser.IContextParser;
import com.dto.ContextElement;
import com.dto.MessageType;
import com.mediator.IMediator;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AALComponent implements IComponent {

  private IMediator mediator;

  private Pane view;
  private final String name = "AAL";

  private IContextParser parser;

  private static final String XML_DIR = "xml/";

  public AALComponent(IMediator mediator) {
    this.mediator = mediator;
    initView();
    setParseMode(ParseMode.DOM);
  }

  private void initView() {
    BorderPane mainPane = new BorderPane();

    VBox vBox = new VBox();

    Button temperatureContextButton = new Button("Update/Send Temperature");
    temperatureContextButton.setOnAction(event -> getTemperatureContext());

    Button positionContextButton = new Button("Update/Send Position");
    positionContextButton.setOnAction(event -> getPositionContext());

    Button timeContextButton = new Button("Update/Send Time");
    timeContextButton.setOnAction(event -> getTimeContext());

    Button velocityContextButton = new Button("Update/Send Velocity");
    velocityContextButton.setOnAction(event -> getVelocityContext());

    vBox.getChildren()
            .addAll(
                    temperatureContextButton,
                    positionContextButton,
                    timeContextButton,
                    velocityContextButton);
    mainPane.setCenter(vBox);

    view = mainPane;
  }

  public void setParseMode(ParseMode _mode) {
    try {
      switch (_mode) {
        case SAX:
          parser = new ContextSaxParser();
          break;
        case DOM:
          parser = new ContextDomParser();
          break;
      }
    } catch (SAXException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void getTemperatureContext() {
    try {
      ContextElement result = parser.parseUrl(XML_DIR + "contextTemperature.xml");
      mediator.notify(this, MessageType.FROM_AAL, result);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void getPositionContext() {
    try {
      ContextElement result = parser.parseUrl(XML_DIR + "contextPosition.xml");
      mediator.notify(this, MessageType.FROM_AAL, result);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void getTimeContext() {
    try {
      ContextElement result = parser.parseUrl(XML_DIR + "contextTime.xml");
      mediator.notify(this, MessageType.FROM_AAL, result);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void getVelocityContext() {
    try {
      ContextElement result = parser.parseUrl(XML_DIR + "contextVelocity.xml");
      mediator.notify(this, MessageType.FROM_AAL, result);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
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

  public enum ParseMode {
    SAX,
    DOM
  }
}
