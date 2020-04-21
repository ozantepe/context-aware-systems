package com.component;

import com.database.server.IGeoServer;
import com.mediator.IMediator;
import com.mediator.Mediator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ComponentFactory {

  private static final String XML_ELEMENT_COMPONENTS = "components";
  private static final String XML_ELEMENT_COMPONENT = "component";
  private static final String XML_ATTRIBUTE_NAME = "name";
  private static final String XML_ATTRIBUTE_METHOD_NAME = "methodName";
  private static final String XML_ATTRIBUTE_METHOD_PARAMETER_TYPE = "methodParameterType";
  private static final String XML_ATTRIBUTE_METHOD_PARAMETER_CLASS = "methodParameterClass";

  public static List<IComponent> buildComponents(
          String componentCompositionFileName, Mediator mediator, IGeoServer geoServer) {
    List<IComponent> components = new ArrayList<>();
    try {
      DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document document = documentBuilder.parse(new File(componentCompositionFileName));
      document.getDocumentElement().normalize();
      Element root = document.getDocumentElement();

      if (root.getNodeName().equals(XML_ELEMENT_COMPONENTS)) {
        NodeList componentList = root.getElementsByTagName(XML_ELEMENT_COMPONENT);
        int componentListSize = componentList.getLength();
        for (int i = 0; i < componentListSize; i++) {
          Node componentNode = componentList.item(i);
          String componentName =
                  componentNode.getAttributes().getNamedItem(XML_ATTRIBUTE_NAME).getTextContent();
          System.out.println("ComponentFactory: loading component '" + componentName + "'");

          IComponent component;
          if (componentName.equals("com.component.gis.GISComponent")) {
            Class<?> componentClass = Class.forName(componentName);
            component =
                    (IComponent)
                            componentClass
                                    .getConstructor(IMediator.class, IGeoServer.class)
                                    .newInstance(mediator, geoServer);

            Node methodNameNode =
                    componentNode.getAttributes().getNamedItem(XML_ATTRIBUTE_METHOD_NAME);
            Node methodParameterTypeNode =
                    componentNode.getAttributes().getNamedItem(XML_ATTRIBUTE_METHOD_PARAMETER_TYPE);
            Node methodParameterClassNode =
                    componentNode.getAttributes().getNamedItem(XML_ATTRIBUTE_METHOD_PARAMETER_CLASS);

            String methodName = methodNameNode.getTextContent();
            String methodParameterType = methodParameterTypeNode.getTextContent();
            String methodParameterClass = methodParameterClassNode.getTextContent();

            System.out.println(
                    "Injecting object '" + methodParameterClass + "' into method '" + methodName + "'");

            Method method =
                    componentClass.getMethod(methodName, Class.forName(methodParameterType));

            method.invoke(
                    component,
                    Class.forName(methodParameterClass).getDeclaredConstructor().newInstance());

          } else {
            component =
                    (IComponent)
                            Class.forName(componentName)
                                    .getConstructor(IMediator.class)
                                    .newInstance(mediator);
          }
          components.add(component);
        }
      }
    } catch (Exception detail) {
      detail.printStackTrace();
    }
    return components;
  }
}
