/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class Unary extends BaseNode {

    private final String operator;  // "-" o "!"
    private final ASTNode operand;

    public Unary(String operator, ASTNode operand, int line, int column) {
        super(line, column);
        this.operator = operator;
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getOperand() {
        return operand;
    }

    public static class Context {

        public final String operator;
        public final ASTNode operand;
        public final int line;
        public final int column;

        public Context(Unary node) {
            this.operator = node.operator;
            this.operand = node.operand;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
