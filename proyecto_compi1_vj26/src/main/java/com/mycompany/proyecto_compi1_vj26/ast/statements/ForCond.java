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
public class ForCond extends BaseNode {

    private final ASTNode condition;
    private final Block body;

    public ForCond(ASTNode condition, Block body, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public Block getBody() {
        return body;
    }

    public static class Context {

        public final ASTNode condition;
        public final Block body;
        public final int line;
        public final int column;

        public Context(ForCond node) {
            this.condition = node.condition;
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
