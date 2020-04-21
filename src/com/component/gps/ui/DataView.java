package com.component.gps.ui;

import com.component.gps.data.INMEAUpdate;
import com.component.gps.data.NMEAInfo;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.text.DecimalFormat;

public class DataView extends Pane implements INMEAUpdate {

  /// internal id to access latitude value field
  private static final String LAT_VALUE_ID = "lat-value";
  /// internal id to access longitude value field
  private static final String LON_VALUE_ID = "lon-value";
  /// internal id to access altitude value field
  private static final String ALT_VALUE_ID = "alt-value";
  /// internal id to access PDOP (precision delusion of precision) value field
  private static final String PDOP_VALUE_ID = "pdop-value";
  /// internal id to access HDOP (horizontal delusion of precision) value field
  private static final String HDOP_VALUE_ID = "hdop-value";
  /// internal id to access VDOP (vertical delusion of precision) value field
  private static final String VDOP_VALUE_ID = "vdop-value";

  /**
   * Initialize the layout of the data panel ...
   */
  public void init() {

    Font dF = Font.getDefault();
    Font lF = Font.font(dF.getFamily(), FontWeight.BOLD, dF.getSize());

    Label lat = new Label("Latitude");
    lat.setTextFill(Color.BLACK);
    lat.setFont(lF);
    lat.setPadding(new Insets(5, 5, 5, 5));

    Label lon = new Label("Longitude");
    lon.setTextFill(Color.BLACK);
    lon.setFont(lF);
    lon.setPadding(new Insets(5, 5, 5, 5));

    Label alt = new Label("Altitude");
    alt.setTextFill(Color.BLACK);
    alt.setFont(lF);
    alt.setPadding(new Insets(5, 5, 5, 5));

    GridPane grid = new GridPane();
    grid.add(lat, 0, 0);
    grid.add(lon, 0, 1);
    grid.add(alt, 0, 2);

    Label latVal = new Label("undefined");
    latVal.setTextFill(Color.BLACK);
    latVal.setId(LAT_VALUE_ID);

    Label lonVal = new Label("undefined");
    lonVal.setTextFill(Color.BLACK);
    lonVal.setId(LON_VALUE_ID);

    Label altVal = new Label("undefined");
    altVal.setTextFill(Color.BLACK);
    altVal.setId(ALT_VALUE_ID);

    grid.add(latVal, 1, 0);
    grid.add(lonVal, 1, 1);
    grid.add(altVal, 1, 2);

    Label pdop = new Label("PDOP");
    pdop.setTextFill(Color.BLACK);
    pdop.setFont(lF);
    pdop.setPadding(new Insets(5, 5, 5, 5));

    Label hdop = new Label("HDOP");
    hdop.setTextFill(Color.BLACK);
    hdop.setFont(lF);
    hdop.setPadding(new Insets(5, 5, 5, 5));

    Label vdop = new Label("VDOP");
    vdop.setTextFill(Color.BLACK);
    vdop.setFont(lF);
    vdop.setPadding(new Insets(5, 5, 5, 5));

    grid.add(pdop, 2, 0);
    grid.add(hdop, 2, 1);
    grid.add(vdop, 2, 2);

    Label pdopVal = new Label("undefined");
    pdopVal.setTextFill(Color.BLACK);
    pdopVal.setId(PDOP_VALUE_ID);

    Label hdopVal = new Label("undefined");
    hdopVal.setTextFill(Color.BLACK);
    hdopVal.setId(HDOP_VALUE_ID);

    Label vdopVal = new Label("undefined");
    vdopVal.setTextFill(Color.BLACK);
    vdopVal.setId(VDOP_VALUE_ID);
    grid.add(pdopVal, 3, 0);
    grid.add(hdopVal, 3, 1);
    grid.add(vdopVal, 3, 2);

    //		grid.setStyle("-fx-background-color:red");

    grid.prefHeightProperty().bind(this.heightProperty());
    grid.prefWidthProperty().bind(this.widthProperty());

    this.getChildren().add(grid);
  }

  @Override
  public void update(NMEAInfo _data) {
    if (_data != null) {
      Platform.runLater(
              new Runnable() {
                  @Override
                  public void run() {
                      DecimalFormat df = new DecimalFormat("#.######");
                      double lat = _data.getLatitude();
                      Label latL = (Label) DataView.this.lookup("#" + LAT_VALUE_ID);
                      latL.setText(String.valueOf(df.format(lat)));

                      double lon = _data.getLongitude();
                      Label lonL = (Label) DataView.this.lookup("#" + LON_VALUE_ID);
                      lonL.setText(String.valueOf(df.format(lon)));

                      double alt = _data.getHeight();
                      Label altL = (Label) DataView.this.lookup("#" + ALT_VALUE_ID);
                      altL.setText(String.valueOf(df.format(alt)));

                      double pdop = _data.getPDOP();
                      Label pdopL = (Label) DataView.this.lookup("#" + PDOP_VALUE_ID);
                      pdopL.setText(String.valueOf(df.format(pdop)));

                      double hdop = _data.getHDOP();
                      Label hdopL = (Label) DataView.this.lookup("#" + HDOP_VALUE_ID);
                      hdopL.setText(String.valueOf(df.format(hdop)));

                      double vdop = _data.getVDOP();
                      Label vdopL = (Label) DataView.this.lookup("#" + VDOP_VALUE_ID);
                      vdopL.setText(String.valueOf(df.format(vdop)));

                      int val = _data.getUsedSat();
                      Color c = null;
                      if (val < 3) {
                          // red background
                          c = Color.LIGHTSALMON;
                      } else if (val < 5) {
                          // yellow background
                          c = Color.LIGHTYELLOW;
                      } else {
                          // green background
                          c = Color.LIGHTSEAGREEN;
                      }
                      DataView.this.setBackground(
                              new Background(new BackgroundFill(c, CornerRadii.EMPTY, Insets.EMPTY)));
                  }
              });
    }
  }
}
