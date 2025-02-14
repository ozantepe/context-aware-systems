/* RuleCompiler.java */
/* Generated By:JavaCC: Do not edit this line. RuleCompiler.java */
package com.rules.rulecompiler.generated;

import com.rules.rulecompiler.ruletree.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class RuleCompiler implements RuleCompilerConstants {
    static RuleCompiler compiler = null;

    public static TreeNode evaluate(String expr) throws ParseException {
        InputStream input = new ByteArrayInputStream(expr.getBytes());
        if (compiler == null) {
            compiler = new RuleCompiler(input);
        } else {
            compiler.ReInit(input);
        }
        return compiler.expr();
    }

    public static void main(String[] argv) {
        try {
            TreeNode tree = RuleCompiler.evaluate(argv[0]);
            System.out.println(tree);
        } catch (Exception detail) {
            detail.printStackTrace();
        }
    }

    public final TreeNode expr() throws ParseException {
        TreeNode root;
        root = stmt();
        jj_consume_token(0);
        {
            if ("" != null) return root;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode stmt() throws ParseException {
        TreeNode node = null;
        node = condition();
        {
            if ("" != null) return node;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode condition() throws ParseException {
        TreeNode conditionNode;
        switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
            case CONTEXT_IDENTIFIER_TEMP: {
                conditionNode = tempStmt();
                {
                    if ("" != null) return conditionNode;
                }
                break;
            }
            case CONTEXT_IDENTIFIER_FUEL_STATUS: {
                conditionNode = fuelStmt();
                {
                    if ("" != null) return conditionNode;
                }
                break;
            }
            case CONTEXT_IDENTIFIER_TIME: {
                conditionNode = daytimeStmt();
                {
                    if ("" != null) return conditionNode;
                }
                break;
            }
            default:
                jj_la1[0] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode tempStmt() throws ParseException {
        Token variableToken;
        TreeNode compareNode;
        variableToken = jj_consume_token(CONTEXT_IDENTIFIER_TEMP);
        compareNode = numberComparison();
        TreeNode variableNode = new VariableNode(variableToken.image);
        compareNode.setNodeA(variableNode);
        {
            if ("" != null) return compareNode;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode fuelStmt() throws ParseException {
        Token variableToken;
        TreeNode compareNode;
        variableToken = jj_consume_token(CONTEXT_IDENTIFIER_FUEL_STATUS);
        compareNode = numberComparison();
        TreeNode variableNode = new VariableNode(variableToken.image);
        compareNode.setNodeA(variableNode);
        {
            if ("" != null) return compareNode;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode daytimeStmt() throws ParseException {
        Token variableToken;
        TreeNode compareNode;
        variableToken = jj_consume_token(CONTEXT_IDENTIFIER_TIME);
        compareNode = daytimeComparison();
        TreeNode variableNode = new VariableNode(variableToken.image);
        compareNode.setNodeA(variableNode);
        {
            if ("" != null) return compareNode;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode numberComparison() throws ParseException {
        TreeNode compareNode, numberNode;
        compareNode = comparison();
        numberNode = number();
        compareNode.setNodeB(numberNode);
        {
            if ("" != null) return compareNode;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode comparison() throws ParseException {
        switch ((jj_ntk == -1) ? jj_ntk_f() : jj_ntk) {
            case GREATER: {
                jj_consume_token(GREATER);
                {
                    if ("" != null) return new CompareGreaterNode();
                }
                break;
            }
            case LESS: {
                jj_consume_token(LESS);
                {
                    if ("" != null) return new CompareLessNode();
                }
                break;
            }
            case EQUALS: {
                jj_consume_token(EQUALS);
                {
                    if ("" != null) return new CompareEqualsNode();
                }
                break;
            }
            default:
                jj_la1[1] = jj_gen;
                jj_consume_token(-1);
                throw new ParseException();
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode daytimeComparison() throws ParseException {
        Token daytimeToken;
        jj_consume_token(EQUALS);
        daytimeToken = jj_consume_token(DAYTIME);
        TreeNode constantNode = new ConstantStringNode(daytimeToken.image);
        TreeNode compareNode = new CompareEqualsNode();
        compareNode.setNodeB(constantNode);
        {
            if ("" != null) return compareNode;
        }
        throw new Error("Missing return statement in function");
    }

    public final TreeNode number() throws ParseException {
        Token numberToken;
        numberToken = jj_consume_token(INT);
        {
            if ("" != null) return new ConstantIntNode(numberToken.image);
        }
        throw new Error("Missing return statement in function");
    }

    /**
     * Generated Token Manager.
     */
    public RuleCompilerTokenManager token_source;

    SimpleCharStream jj_input_stream;
    /**
     * Current token.
     */
    public Token token;
    /**
     * Next token.
     */
    public Token jj_nt;

    private int jj_ntk;
    private int jj_gen;
    private final int[] jj_la1 = new int[2];
    private static int[] jj_la1_0;

    static {
        jj_la1_init_0();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 =
                new int[]{
                        0x1c00, 0x380,
                };
    }

    /**
     * Constructor with InputStream.
     */
    public RuleCompiler(java.io.InputStream stream) {
        this(stream, null);
    }

    /**
     * Constructor with InputStream and supplied encoding
     */
    public RuleCompiler(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source = new RuleCompilerTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 2; i++) jj_la1[i] = -1;
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.InputStream stream) {
        ReInit(stream, null);
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.InputStream stream, String encoding) {
        try {
            jj_input_stream.ReInit(stream, encoding, 1, 1);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 2; i++) jj_la1[i] = -1;
    }

    /**
     * Constructor.
     */
    public RuleCompiler(java.io.Reader stream) {
        jj_input_stream = new SimpleCharStream(stream, 1, 1);
        token_source = new RuleCompilerTokenManager(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 2; i++) jj_la1[i] = -1;
    }

    /**
     * Reinitialise.
     */
    public void ReInit(java.io.Reader stream) {
        jj_input_stream.ReInit(stream, 1, 1);
        token_source.ReInit(jj_input_stream);
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 2; i++) jj_la1[i] = -1;
    }

    /**
     * Constructor with generated Token Manager.
     */
    public RuleCompiler(RuleCompilerTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 2; i++) jj_la1[i] = -1;
    }

    /**
     * Reinitialise.
     */
    public void ReInit(RuleCompilerTokenManager tm) {
        token_source = tm;
        token = new Token();
        jj_ntk = -1;
        jj_gen = 0;
        for (int i = 0; i < 2; i++) jj_la1[i] = -1;
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken;
        if ((oldToken = token).next != null) token = token.next;
        else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        if (token.kind == kind) {
            jj_gen++;
            return token;
        }
        token = oldToken;
        jj_kind = kind;
        throw generateParseException();
    }

    /**
     * Get the next Token.
     */
    public final Token getNextToken() {
        if (token.next != null) token = token.next;
        else token = token.next = token_source.getNextToken();
        jj_ntk = -1;
        jj_gen++;
        return token;
    }

    /**
     * Get the specific Token.
     */
    public final Token getToken(int index) {
        Token t = token;
        for (int i = 0; i < index; i++) {
            if (t.next != null) t = t.next;
            else t = t.next = token_source.getNextToken();
        }
        return t;
    }

    private int jj_ntk_f() {
        if ((jj_nt = token.next) == null)
            return (jj_ntk = (token.next = token_source.getNextToken()).kind);
        else return (jj_ntk = jj_nt.kind);
    }

    private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
    private int[] jj_expentry;
    private int jj_kind = -1;

    /**
     * Generate ParseException.
     */
    public ParseException generateParseException() {
        jj_expentries.clear();
        boolean[] la1tokens = new boolean[13];
        if (jj_kind >= 0) {
            la1tokens[jj_kind] = true;
            jj_kind = -1;
        }
        for (int i = 0; i < 2; i++) {
            if (jj_la1[i] == jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i = 0; i < 13; i++) {
            if (la1tokens[i]) {
                jj_expentry = new int[1];
                jj_expentry[0] = i;
                jj_expentries.add(jj_expentry);
            }
        }
        int[][] exptokseq = new int[jj_expentries.size()][];
        for (int i = 0; i < jj_expentries.size(); i++) {
            exptokseq[i] = jj_expentries.get(i);
        }
        return new ParseException(token, exptokseq, tokenImage);
    }

    /**
     * Enable tracing.
     */
    public final void enable_tracing() {
    }

    /**
     * Disable tracing.
     */
    public final void disable_tracing() {
    }
}
