package com.rules.rulecompiler.ruletree;

public class ConstantStringNode extends ConstantNode {

  public ConstantStringNode(String constantValue) {
    setConstantValue(constantValue);
  }

  public String getValue() {
    return (String) calculate();
  }
}
