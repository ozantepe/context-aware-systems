package com.rules.rulecompiler.ruletree;

import java.util.Map;

public abstract class TreeNode {

    private TreeNode nodeA;
    private TreeNode nodeB;

    public abstract Object calculate() throws NodeError;

    public void setVariableParameters(Map<String, Object> contextElements) {
        if (nodeA != null) {
            nodeA.setVariableParameters(contextElements);
        }
        if (nodeB != null) {
            nodeB.setVariableParameters(contextElements);
        }
    }

    public void clear() {
        if (nodeA != null) {
            nodeA.clear();
        }
        if (nodeB != null) {
            nodeB.clear();
        }
    }

    public TreeNode getNodeA() {
        return nodeA;
    }

    public void setNodeA(TreeNode nodeA) {
        this.nodeA = nodeA;
    }

    public TreeNode getNodeB() {
        return nodeB;
    }

    public void setNodeB(TreeNode nodeB) {
        this.nodeB = nodeB;
    }
}
