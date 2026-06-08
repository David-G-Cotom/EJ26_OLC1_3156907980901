/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class VarDecl extends BaseNode {

    private final String name;
    private final VarType type;
    private final ASTNode initValue;    //puede ser null

    public VarDecl(String name, VarType type, ASTNode initValue, int line, int column) {
        super(line, column);
        this.name = name;
        this.type = type;
        this.initValue = initValue;
    }

    public String getName() {
        return name;
    }

    public VarType getType() {
        return type;
    }

    public ASTNode getInitValue() {
        return initValue;
    }

    public boolean hasInitValue() {
        return this.initValue != null;
    }

    public static class Context {

        public final String name;
        public final VarType type;
        public final ASTNode initValue;
        public final int line;
        public final int column;

        public Context(VarDecl node) {
            this.name = node.name;
            this.type = node.type;
            this.initValue = node.initValue;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

        public boolean hasInitValue() {
            return this.initValue != null;
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
