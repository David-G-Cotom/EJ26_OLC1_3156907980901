/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class UserFuncCall extends BaseNode {

    private final String name;
    private final List<ASTNode> args;

    public UserFuncCall(String name, List<ASTNode> args, int line, int column) {
        super(line, column);
        this.name = name;
        this.args = args;
    }

    public String getName() {
        return name;
    }

    public List<ASTNode> getArgs() {
        return args;
    }

    public static class Context {

        public final String name;
        public final List<ASTNode> args;
        public final int line;
        public final int column;

        public Context(UserFuncCall node) {
            this.name = node.name;
            this.args = node.args;
            this.line = node.getLine();
            this.column = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
