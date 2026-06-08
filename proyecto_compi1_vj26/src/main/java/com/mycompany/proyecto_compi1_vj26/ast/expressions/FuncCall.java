/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class FuncCall extends BaseNode {

    private final String functionName;
    private final List<ASTNode> args;

    public FuncCall(String functionName, List<ASTNode> args, int line, int column) {
        super(line, column);
        this.functionName = functionName;
        this.args = args;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<ASTNode> getArgs() {
        return args;
    }

    public static class Context {

        public final String functionName;
        public final List<ASTNode> args;
        public final int line;
        public final int column;

        public Context(FuncCall node) {
            this.functionName = node.functionName;
            this.args = node.args;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
