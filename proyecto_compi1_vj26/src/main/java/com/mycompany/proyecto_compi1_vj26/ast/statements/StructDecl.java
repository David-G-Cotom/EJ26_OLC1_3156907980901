/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class StructDecl extends BaseNode {

    private final String name;
    private final List<Object[]> fields;

    public StructDecl(String name, List<Object[]> fields, int line, int column) {
        super(line, column);
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public List<Object[]> getFields() {
        return fields;
    }

    public static class Context {

        public final String name;
        public final List<Object[]> fields;
        public final int line;
        public final int column;

        public Context(StructDecl node) {
            this.name = node.name;
            this.fields = node.fields;
            this.line = node.getLine();
            this.column = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
