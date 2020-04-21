package com.rules.rulecompiler.ruletree;

import java.util.Map;

public class VariableNode extends TreeNode {

  private String name;
  private Object contextValue = null;

  public VariableNode(String name) {
    this.name = name;
  }

  @Override
  public Object calculate() throws NodeError {
    if (contextValue == null) {
      throw new NodeError("Null value for '" + contextValue + "'");
    }
    return contextValue;
  }

  @Override
  public void setVariableParameters(Map<String, Object> contextElements) {
    Object value = contextElements.get(name);
    if (value != null) {
      contextValue = value;
    } else {
      throw new NodeError("No variable value for '" + name + "'");
    }
  }

  @Override
  public void clear() {
    contextValue = null;
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
