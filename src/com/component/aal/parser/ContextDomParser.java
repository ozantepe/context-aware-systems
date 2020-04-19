package com.component.aal.parser;

import com.dto.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ContextDomParser implements IContextParser {

  private DocumentBuilderFactory documentBuilderFactory;

  public ContextDomParser() {
    documentBuilderFactory = DocumentBuilderFactory.newInstance();
  }

  @Override
  public ContextElement parseUrl(String inputUrl)
          throws SAXException, ParserConfigurationException, IOException {
    return parseInternal(inputUrl);
  }

  private ContextElement parseInternal(String url)
          throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    Document document = documentBuilder.parse(url);
    document.getDocumentElement().normalize();

    Element root = document.getDocumentElement();

    ContextElement contextElement = null;

    if (root.getNodeName().equals("contextElement")) {
      String contextKeyString = root.getElementsByTagName("contextKey").item(0).getTextContent();
      ContextKey contextKey = ContextKey.valueOf(contextKeyString.toUpperCase());

      String contextIdString = root.getElementsByTagName("contextId").item(0).getTextContent();
      int contextId = Integer.parseInt(contextIdString);

      if (contextKey.equals(ContextKey.TIME)) {
        ContextTime contextTime = new ContextTime();

        NodeList contextValue = root.getElementsByTagName("contextValue").item(0).getChildNodes();

        for (int i = 0; i < contextValue.getLength(); i++) {
          if (contextValue.item(i).getNodeName().equals("unit")) {
            contextTime.setUnit(contextValue.item(i).getTextContent());
          } else if (contextValue.item(i).getNodeName().equals("value")) {
            Date date = new Date(Long.parseLong(contextValue.item(i).getTextContent()));
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("CET"));
            String[] dateStr = sdf.format(date).split(":");

            int hours = Integer.parseInt(dateStr[0]);
            int minutes = Integer.parseInt(dateStr[1]);
            int seconds = Integer.parseInt(dateStr[2]);

            contextTime.setHours(hours);
            contextTime.setMinutes(minutes);
            contextTime.setSeconds(seconds);
          }
        }

        contextElement = contextTime;
      } else if (contextKey.equals(ContextKey.POSITION)) {
        ContextPosition contextPosition = new ContextPosition();

        NodeList contextValue = root.getElementsByTagName("contextValue").item(0).getChildNodes();

        for (int i = 0; i < contextValue.getLength(); i++) {
          if (contextValue.item(i).getNodeName().equals("unit")) {
            contextPosition.setUnit(contextValue.item(i).getTextContent());
          } else if (contextValue.item(i).getNodeName().equals("data")) {

            NodeList data = contextValue.item(i).getChildNodes();

            for (int j = 0; j < data.getLength(); j++) {
              if (data.item(j).getNodeName().equals("latitude")) {
                contextPosition.setLatitude(Double.parseDouble(data.item(j).getTextContent()));
              } else if (data.item(j).getNodeName().equals("longitude")) {
                contextPosition.setLongitude(Double.parseDouble(data.item(j).getTextContent()));
              }
            }
          }
        }

        contextElement = contextPosition;
      } else if (contextKey.equals(ContextKey.TEMPERATURE)) {
        ContextTemperature contextTemperature = new ContextTemperature();

        NodeList contextValue = root.getElementsByTagName("contextValue").item(0).getChildNodes();

        for (int i = 0; i < contextValue.getLength(); i++) {
          if (contextValue.item(i).getNodeName().equals("unit")) {
            contextTemperature.setUnit(contextValue.item(i).getTextContent());
          } else if (contextValue.item(i).getNodeName().equals("value")) {
            contextTemperature.setTemperature(
                    Integer.parseInt(contextValue.item(i).getTextContent()));
          }
        }

        contextElement = contextTemperature;
      } else if (contextKey.equals(ContextKey.VELOCITY)) {
        ContextVelocity contextVelocity = new ContextVelocity();

        NodeList contextValue = root.getElementsByTagName("contextValue").item(0).getChildNodes();

        for (int i = 0; i < contextValue.getLength(); i++) {
          if (contextValue.item(i).getNodeName().equals("unit")) {
            contextVelocity.setUnit(contextValue.item(i).getTextContent());
          } else if (contextValue.item(i).getNodeName().equals("value")) {
            contextVelocity.setVelocity(Integer.parseInt(contextValue.item(i).getTextContent()));
          }
        }

        contextElement = contextVelocity;
      }

      contextElement.setContextKey(contextKey);
      contextElement.setContextId(contextId);
    }

    return contextElement;
  }
}
