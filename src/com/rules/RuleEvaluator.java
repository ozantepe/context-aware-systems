package com.rules;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuleEvaluator {

  private final List<RuleContainer> ruleContainers = new ArrayList<>();

  private static final String gisRulefile = "xml/GISRules.xml";
  private static final String poiRulefile = "xml/POIRules.xml";

  public void loadTestRules() {
    try {
      parse(RuleType.GISRules);
      parse(RuleType.POIRules);
    } catch (SAXException | IOException | ParserConfigurationException e) {
      e.printStackTrace();
    }
  }

  public void parse(RuleType type) throws SAXException, IOException, ParserConfigurationException {
    File f = null;
    if (type == RuleType.GISRules) {
      f = new File(gisRulefile);
    } else if (type == RuleType.POIRules) {
      f = new File(poiRulefile);
    }
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    parser.parse(f, new RuleParserHandler(ruleContainers));
  }

  public void evaluateAndExecute(Map<String, Object> contextElements) {
    for (RuleContainer ruleContainer : ruleContainers) {
      try {
        ruleContainer.evaluateAndExecute(contextElements);
      } catch (Throwable e) {
        System.out.println(
                "Error: Could not evaluate rule '"
                        + ruleContainer.getConditionString()
                        + "', Error: "
                        + e.getMessage());
      }
    }
  }
}
