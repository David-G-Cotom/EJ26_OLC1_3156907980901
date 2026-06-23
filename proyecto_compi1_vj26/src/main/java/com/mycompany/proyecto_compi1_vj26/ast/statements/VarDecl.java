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
    private final String structTypeName;    // null salvo type == VarType.STRUCT

    public VarDecl(String name, VarType type, ASTNode initValue, int line, int column) {
        this(name, type, initValue, null, line, column);
    }

    public VarDecl(String name, VarType type, ASTNode initValue, String structTypeName, int line, int column) {
        super(line, column);
        this.name = name;
        this.type = type;
        this.initValue = initValue;
        this.structTypeName = structTypeName;
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

    public String getStructTypeName() {
        return structTypeName;
    }

    public boolean hasInitValue() {
        return this.initValue != null;
    }

    public boolean isStructType() {
        return this.structTypeName != null;
    }

    public static class Context {

        public final String name;
        public final VarType type;
        public final ASTNode initValue;
        public final String structTypeName;
        public final int line;
        public final int column;

        public Context(VarDecl node) {
            this.name = node.name;
            this.type = node.type;
            this.initValue = node.initValue;
            this.structTypeName = node.structTypeName;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

        public boolean hasInitValue() {
            return this.initValue != null;
        }

        public boolean isStructType() {
            return this.structTypeName != null;
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
