package com.rules;

import com.component.IComponent;
import com.mediator.Mediator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Action {

  public String className;
  public String methodName;
  public String parameterType;
  public String parameterValue;

  public void setClassName(String className) {
    this.className = className;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public void setParameterType(String parameterType) {
    this.parameterType = parameterType;
  }

  public void setParameterValue(String parameterValue) {
    this.parameterValue = parameterValue;
  }

  public void execute() {
    try {
      Class<?> componentClass = Class.forName(className);

      IComponent component = Mediator.getComponent(componentClass);

      if (component == null) {
        System.out.println("component of type '" + className + "' not found in Mediator!");
        return;
      }

      if (parameterType != null && parameterValue != null) {
        Class<?> parameterTypeClass = Class.forName(parameterType);
        Class<?> parameterValueClass = Class.forName(parameterValue);
        Method method = componentClass.getMethod(methodName, parameterTypeClass);
        method.invoke(component, parameterValueClass.getDeclaredConstructor().newInstance());
      } else {
        Method method = componentClass.getMethod(methodName);
        method.invoke(component);
      }

    } catch (ClassNotFoundException
            | NoSuchMethodException
            | IllegalAccessException
            | InstantiationException
            | InvocationTargetException e) {
        e.printStackTrace();
    }
  }

  @Override
  public String toString() {
    return "Action{"
            + "methodName='"
            + methodName
            + '\''
            + ", parameterType='"
            + parameterType
            + '\''
            + ", parameterValue='"
            + parameterValue
            + '\''
            + ", className='"
            + className
            + '\''
            + '}';
  }
}
