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
public class ForRange extends BaseNode {

    private final String index;
    private final String value;
    private final ASTNode iterable; // expresion que produce el slice
    private final Block body;

    public ForRange(String index, String value, ASTNode iterable, Block body, int line, int column) {
        super(line, column);
        this.index = index;
        this.value = value;
        this.iterable = iterable;
        this.body = body;
    }

    public String getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public ASTNode getIterable() {
        return iterable;
    }

    public Block getBody() {
        return body;
    }

    public static class Context {

        public final String index;
        public final String value;
        public final ASTNode iterable;
        public final Block body;
        public final int line;
        public final int column;

        public Context(ForRange node) {
            this.index = node.index;
            this.value = node.value;
            this.iterable = node.iterable;
            this.body = node.body;
            this.line = node.getLine();
            this.column = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
