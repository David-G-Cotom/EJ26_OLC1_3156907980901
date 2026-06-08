/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.ValType;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class Literal extends BaseNode {

    private final Object value;
    private final ValType type;

    public Literal(Object value, ValType type, int line, int column) {
        super(line, column);
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public ValType getType() {
        return type;
    }

    public static class Context {

        public final Object value;
        public final ValType type;
        public final int line;
        public final int column;

        public Context(Literal nodo) {
            this.value = nodo.value;
            this.type = nodo.type;
            this.line = nodo.getLine();
            this.column = nodo.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
