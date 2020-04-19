package com.component.aal.parser;

import com.dto.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;
import java.util.TimeZone;

public class ContextSaxParser extends DefaultHandler implements IContextParser {

  private static final String CONTEXT_ID = "contextId";
  private static final String CONTEXT_KEY = "contextKey";
  private static final String UNIT = "unit";
  private static final String VALUE = "value";
  private static final String LATITUDE = "latitude";
  private static final String LONGITUDE = "longitude";

  private int contextId = 0;
  private ContextKey contextKey = null;
  private String unit = null;
  private double latitude = 0;
  private double longitude = 0;
  private String value = null;

  private SAXParser parser;

  // Stacks for storing the elements and objects.
  private Stack<String> elements = new Stack<>();

  private ContextElement context;

  public ContextSaxParser() throws ParserConfigurationException, SAXException {
    SAXParserFactory factory = SAXParserFactory.newInstance();
    parser = factory.newSAXParser();
  }

  @Override
  public ContextElement parseUrl(String inputUrl) throws SAXException, IOException {
    parser.parse(inputUrl, this);
    return context;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
          throws SAXException {
    elements.push(qName);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    elements.pop();

    if (qName.equals("contextElement")) {
      context.setContextId(contextId);
      context.setContextKey(contextKey);
      context.setUnit(unit);

      if (contextKey.equals(ContextKey.POSITION)) {

        ((ContextPosition) context).setLatitude(latitude);
        ((ContextPosition) context).setLongitude(longitude);

      } else if (contextKey.equals(ContextKey.TIME)) {

        Date date = new Date(Long.parseLong(value));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        String[] dateStr = sdf.format(date).split(":");

        int hours = Integer.parseInt(dateStr[0]);
        int minutes = Integer.parseInt(dateStr[1]);
        int seconds = Integer.parseInt(dateStr[2]);

        ((ContextTime) context).setHours(hours);
        ((ContextTime) context).setMinutes(minutes);
        ((ContextTime) context).setSeconds(seconds);

      } else if (contextKey.equals(ContextKey.TEMPERATURE)) {

        ((ContextTemperature) context).setTemperature(Integer.parseInt(value));

      } else if (contextKey.equals(ContextKey.VELOCITY)) {
        ((ContextVelocity) context).setVelocity(Double.parseDouble(value));
      }
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {

    String strVal = new String(ch, start, length);

    if (strVal.length() == 0) {
      return;
    }

    String content = new String(ch, start, length);

    switch (currentElement()) {
      case CONTEXT_ID:
        contextId = Integer.parseInt(content);
        break;

      case CONTEXT_KEY:
        contextKey = ContextKey.valueOf(content.toUpperCase());
        initContext();
        break;

      case UNIT:
        unit = content;
        break;

      case VALUE:
        value = content;
        break;

      case LATITUDE:
        latitude = Double.parseDouble(content);
        break;

      case LONGITUDE:
        longitude = Double.parseDouble(content);
        break;

      default:
        break;
    }
  }

  private String currentElement() {
    return elements.peek();
  }

  private void initContext() {

    if (contextKey.equals(ContextKey.TIME)) {
      context = new ContextTime();
    } else if (contextKey.equals(ContextKey.POSITION)) {
      context = new ContextPosition();
    } else if (contextKey.equals(ContextKey.TEMPERATURE)) {
      context = new ContextTemperature();
    } else if (contextKey.equals(ContextKey.VELOCITY)) {
      context = new ContextVelocity();
    }
  }
}
