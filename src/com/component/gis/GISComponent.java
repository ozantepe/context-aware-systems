package com.component.gis;

import com.component.IComponent;
import com.component.IObserver;
import com.component.gis.warnings.IWarning;
import com.database.server.IGeoServer;
import com.dto.MessageType;
import com.mediator.IMediator;
import com.rules.RuleEvaluator;
import com.rules.RuleType;
import javafx.scene.layout.Pane;

public class GISComponent implements IComponent, IObserver {

  private IMediator mediator;

  private final String name = "GIS";

  private GISModel model;
  private GISController controller;
  private GISView view;

  public GISComponent(IMediator mediator, IGeoServer geoServer) {
    this.mediator = mediator;
    RuleEvaluator ruleEvaluator = new RuleEvaluator();
    try {
      ruleEvaluator.parse(RuleType.GISRules);
    } catch (Exception e) {
      e.printStackTrace();
    }
    initComponent(geoServer, ruleEvaluator);
  }

  private void initComponent(IGeoServer geoServer, RuleEvaluator ruleEvaluator) {
    model = new GISModel();
    controller = new GISController(model, geoServer, ruleEvaluator);
    view = new GISView(controller);
    model.addListener(view);
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
    switch (messageType) {
      case FROM_POI: {
        controller.updatePOI(data);
        break;
      }
      case FROM_GPS: {
        controller.updateGPS(data);
        break;
      }
      case FROM_CM: {
        controller.updateCM(data);
        break;
      }
      default:
        break;
    }
  }

  public void showWarning(IWarning warning) {
    controller.showWarning(warning);
  }

  public void removeWarning(IWarning warning) {
    controller.removeWarning(warning);
  }
}
