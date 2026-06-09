/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.BinaryOperator;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class ImplicitAssign extends BaseNode {

    private final String name;
    private final BinaryOperator operator;  // "+=" o "-="
    private final ASTNode value;

    public ImplicitAssign(String name, BinaryOperator operator, ASTNode value, int line, int column) {
        super(line, column);
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public ASTNode getValue() {
        return value;
    }

    public static class Context {

        public final String name;
        public final BinaryOperator operator;
        public final ASTNode value;
        public final int line;
        public final int column;

        public Context(ImplicitAssign node) {
            this.name = node.name;
            this.operator = node.operator;
            this.value = node.value;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
