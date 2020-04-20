package com.component.gis.warnings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class WinterWarning implements IWarning {

  private static final String IMAGE_PATH = "icons/winter.png";

  private Image image = null;

  @Override
  public Image getImage() {
    if (image == null) {
      try {
        image = ImageIO.read(new File(IMAGE_PATH));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return image;
  }
}
