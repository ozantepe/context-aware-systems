package com.rules;

import com.rules.rulecompiler.generated.ParseException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;
import java.util.Stack;

public class RuleParserHandler extends DefaultHandler {

    private static final String CONDITION = "condition";
    private static final String METHOD_NAME = "methodName";
    private static final String PARAMETER_TYPE = "parameterType";
    private static final String PARAMETER_VALUE = "parameterValue";
    private static final String CLASS = "class";
    private static final String ACTION = "action";

    private static final String rule = "rule";

    private String condition = "";
    private Action action;

    private final Stack<String> elements = new Stack<>();
    private RuleContainer ruleContainer;

    private final List<RuleContainer> ruleContainers;

    public RuleParserHandler(List<RuleContainer> ruleContainers) {
        this.ruleContainers = ruleContainers;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        elements.push(qName);

        if (qName.equals(CONDITION)) {
            condition = "";
        } else if (qName.equals(ACTION)) {
            action = new Action();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elements.pop();

        if (qName.equals(rule)) {
            try {
                ruleContainer = new RuleContainer(condition, action);
            } catch (Error | ParseException e) {
                e.printStackTrace();
            }
            ruleContainers.add(ruleContainer);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        String content = new String(ch, start, length);

        if (content.length() == 0) {
            return;
        }

        switch (currentElement()) {
            case CONDITION:
                condition += content;
                break;

            case METHOD_NAME:
                action.setMethodName(content);
                break;

            case CLASS:
                action.setClassName(content);
                break;

            case PARAMETER_TYPE:
                action.setParameterType(content);
                break;

            case PARAMETER_VALUE:
                action.setParameterValue(content);
                break;

            default:
                break;
        }
    }

    private String currentElement() {
        return elements.peek();
    }
}
