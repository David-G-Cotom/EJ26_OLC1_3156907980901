/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class FieldAccess extends BaseNode {

    private final ASTNode struct;   // expresion que produce el StructValue
    private final String fieldName;

    public FieldAccess(ASTNode struct, String fieldName, int line, int column) {
        super(line, column);
        this.struct = struct;
        this.fieldName = fieldName;
    }

    public ASTNode getStruct() {
        return struct;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static class Context {

        public final ASTNode struct;
        public final String fieldName;
        public final int line;
        public final int column;

        public Context(FieldAccess node) {
            this.struct = node.struct;
            this.fieldName = node.fieldName;
            this.line = node.getLine();
            this.column = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
