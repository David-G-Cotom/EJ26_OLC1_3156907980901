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
public class For extends BaseNode {

    private final ASTNode init;
    private final ASTNode condition;
    private final ASTNode increment;
    private final Block body;

    public For(ASTNode init, ASTNode condition, ASTNode increment, Block body, int line, int column) {
        super(line, column);
        this.init = init;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public ASTNode getInit() {
        return init;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getIncrement() {
        return increment;
    }

    public Block getBody() {
        return body;
    }

    public static class Context {

        public final ASTNode init;
        public final ASTNode condition;
        public final ASTNode increment;
        public final Block body;
        public final int line;
        public final int column;

        public Context(For node) {
            this.init = node.init;
            this.condition = node.condition;
            this.increment = node.increment;
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
