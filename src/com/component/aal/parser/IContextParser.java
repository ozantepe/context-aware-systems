package com.component.aal.parser;

import com.dto.ContextElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface IContextParser {
    ContextElement parseUrl(String inputUrl) throws SAXException, IOException, ParserConfigurationException;
}