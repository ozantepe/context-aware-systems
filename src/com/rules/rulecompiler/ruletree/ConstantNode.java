package com.rules.rulecompiler.ruletree;

public abstract class ConstantNode extends TreeNode {

  private Object constantValue = null;

  public void setConstantValue(Object constantValue) {
    this.constantValue = constantValue;
  }

  @Override
  public Object calculate() throws NodeError {
    return constantValue;
  }

  @Override
  public void setNodeA(TreeNode nodeA) {
    super.setNodeA(null);
  }

  @Override
  public void setNodeB(TreeNode nodeB) {
    super.setNodeB(null);
  }
}
