/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class IncDec extends BaseNode {

    private final String name;
    private final String operator;  // "++" o "--"

    public IncDec(String name, String operator, int line, int column) {
        super(line, column);
        this.name = name;
        this.operator = operator;
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public static class Context {

        public final String name;
        public final String operator;
        public final int line;
        public final int column;

        public Context(IncDec node) {
            this.name = node.name;
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
