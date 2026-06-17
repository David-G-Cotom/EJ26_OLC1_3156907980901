/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class Switch extends BaseNode {

    private final ASTNode condition;
    private final List<ASTNode> caseExprs;
    private final List<List<ASTNode>> caseBodies;
    private final List<ASTNode> defaultBody;      // null si no existe default

    public Switch(ASTNode condition, List<ASTNode> caseExprs, List<List<ASTNode>> caseBodies, List<ASTNode> defaultBody, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.caseExprs = caseExprs;
        this.caseBodies = caseBodies;
        this.defaultBody = defaultBody;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public List<ASTNode> getCaseExprs() {
        return caseExprs;
    }

    public List<List<ASTNode>> getCaseBodies() {
        return caseBodies;
    }

    public List<ASTNode> getDefaultBody() {
        return defaultBody;
    }

    public boolean hasDefault() {
        return this.defaultBody != null;
    }

    public static class Context {

        public final ASTNode condition;
        public final List<ASTNode> caseExprs;
        public final List<List<ASTNode>> caseBodies;
        public final List<ASTNode> defaultBody;
        public final int line;
        public final int column;

        public Context(Switch node) {
            this.condition = node.condition;
            this.caseExprs = node.caseExprs;
            this.caseBodies = node.caseBodies;
            this.defaultBody = node.defaultBody;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

        public boolean hasDefault() {
            return this.defaultBody != null;
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
