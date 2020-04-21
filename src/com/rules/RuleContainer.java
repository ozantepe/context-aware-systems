package com.rules;

import com.rules.rulecompiler.generated.RuleCompiler;
import com.rules.rulecompiler.ruletree.TreeNode;

import java.util.Map;

public class RuleContainer {
  private final String conditionString;
  private final TreeNode conditionTreeRoot;
  private final Action action;

  public RuleContainer(String conditionString, Action action)
          throws Error, com.rules.rulecompiler.generated.ParseException {

    this.conditionString = conditionString;
    conditionTreeRoot = RuleCompiler.evaluate(conditionString);
    this.action = action;
  }

  public void evaluateAndExecute(Map<String, Object> contextElements) {
    conditionTreeRoot.setVariableParameters(contextElements);
    boolean result = (boolean) conditionTreeRoot.calculate();
    if (result) {
      action.execute();
    }
  }

  public String getConditionString() {
    return conditionString;
  }
}
