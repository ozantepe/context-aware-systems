package com.rules.rulecompiler.ruletree;

public class ConstantIntNode extends ConstantNode {

  public ConstantIntNode(String constantValue) {
    int value = Integer.parseInt(constantValue);
    setConstantValue(value);
  }

  public int getValue() {
    return (int) calculate();
  }
}
