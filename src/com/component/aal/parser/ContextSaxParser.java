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
import java.util.*;

public class ContextSaxParser extends DefaultHandler implements IContextParser {

    private static final String CONTEXT_ID = "contextId";
    private static final String CONTEXT_KEY = "contextKey";
    private static final String UNIT = "unit";
    private static final String VALUE = "value";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";

    private int mContextId = 0;
    private ContextKey mContextKey = null;
    private String mUnit = null;
    private double mLatitude = 0;
    private double mLongitude = 0;
    private String mValue = null;

    private Map<String, Boolean> mStates = new HashMap<String, Boolean>();
    private SAXParser mParser;

    // List of context temperature data.
    private List<ContextTemperature> contectTemp = new ArrayList<ContextTemperature>();

    // Stacks for storing the elements and objects.
    private Stack<String> elements = new Stack<String>();
    private Stack<ContextTemperature> objects = new Stack<ContextTemperature>();

    private ContextElement mContext;

    public ContextSaxParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        mParser = factory.newSAXParser();
    }

    @Override
    public ContextElement parseUrl(String inputUrl) throws SAXException, IOException {
        mParser.parse(inputUrl, this);
        return mContext;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elements.push(qName);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        elements.pop();

        if (qName.equals("contextElement")) {
            mContext.setContextId(mContextId);
            mContext.setContextKey(mContextKey);
            mContext.setUnit(mUnit);

            if (mContextKey.equals(ContextKey.POSITION)) {

                ((ContextPosition) mContext).setLatitude(mLatitude);
                ((ContextPosition) mContext).setLongitude(mLongitude);

            } else if (mContextKey.equals(ContextKey.TIME)) {

                Date date = new Date(Long.parseLong(mValue));
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("CET"));
                String[] dateStr = sdf.format(date).split(":");

                int hours = Integer.parseInt(dateStr[0]);
                int minutes = Integer.parseInt(dateStr[1]);
                int seconds = Integer.parseInt(dateStr[2]);

                ((ContextTime) mContext).setHours(hours);
                ((ContextTime) mContext).setMinutes(minutes);
                ((ContextTime) mContext).setSeconds(seconds);

            } else if (mContextKey.equals(ContextKey.TEMPERATURE)) {

                ((ContextTemperature) mContext).setTemperature(Integer.parseInt(mValue));

            } else if (mContextKey.equals(ContextKey.VELOCITY)) {
                ((ContextVelocity) mContext).setVelocity(Double.parseDouble(mValue));
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        String value = new String(ch, start, length);

        if (value.length() == 0) {
            return;
        }

        String content = new String(ch, start, length);

        switch (currentElement()) {

            case CONTEXT_ID:
                mContextId = Integer.parseInt(content);
                break;

            case CONTEXT_KEY:
                mContextKey = ContextKey.valueOf(content.toUpperCase());
                initContext();
                break;

            case UNIT:
                mUnit = content;
                break;

            case VALUE:
                mValue = content;
                break;

            case LATITUDE:
                mLatitude = Double.parseDouble(content);
                break;

            case LONGITUDE:
                mLongitude = Double.parseDouble(content);
                break;

            default:
                break;
        }
    }

    private String currentElement() {
        return elements.peek();
    }

    private void initContext() {

        if (mContextKey.equals(ContextKey.TIME)) {
            mContext = new ContextTime();
        } else if (mContextKey.equals(ContextKey.POSITION)) {
            mContext = new ContextPosition();
        } else if (mContextKey.equals(ContextKey.TEMPERATURE)) {
            mContext = new ContextTemperature();
        } else if (mContextKey.equals(ContextKey.VELOCITY)) {
            mContext = new ContextVelocity();
        }
    }
}
