/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class Identifier extends BaseNode {

    private final String name;

    public Identifier(String name, int line, int column) {
        super(line, column);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static class Context {

        public final String name;
        public final int line;
        public final int column;

        public Context(Identifier node) {
            this.name = node.name;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
