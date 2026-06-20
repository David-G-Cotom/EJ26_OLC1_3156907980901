/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class IndexAssign extends BaseNode {

    private final ASTNode slice;
    private final ASTNode index;
    private final ASTNode value;
    private final String operator;  // "=", "+=", "-="

    public IndexAssign(ASTNode slice, ASTNode index, ASTNode value, String operator, int line, int column) {
        super(line, column);
        this.slice = slice;
        this.index = index;
        this.value = value;
        this.operator = operator;
    }

    public ASTNode getSlice() {
        return slice;
    }

    public ASTNode getIndex() {
        return index;
    }

    public ASTNode getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

    public static class Context {

        public final ASTNode slice;
        public final ASTNode index;
        public final ASTNode value;
        public final String operator;
        public final int line;
        public final int column;

        public Context(IndexAssign node) {
            this.slice = node.slice;
            this.index = node.index;
            this.value = node.value;
            this.operator = node.operator;
            this.line = node.getLine();
            this.column = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
