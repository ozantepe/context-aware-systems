package com.rules.rulecompiler.ruletree;

public abstract class CompareNode extends TreeNode {

  public Comparable getResultA() {
    return (Comparable) getNodeA().calculate();
  }

  public Comparable getResultB() {
    return (Comparable) getNodeB().calculate();
  }
}
