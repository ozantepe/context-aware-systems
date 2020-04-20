package com.rules.rulecompiler.ruletree;

public class CompareEqualsNode extends CompareNode {

  @Override
  public Object calculate() throws NodeError {
    return getResultA().compareTo(getResultB()) == 0;
  }
}
