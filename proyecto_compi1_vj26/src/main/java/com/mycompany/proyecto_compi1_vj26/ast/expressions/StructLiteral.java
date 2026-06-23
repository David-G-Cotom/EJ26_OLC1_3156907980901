/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class StructLiteral extends BaseNode {
    
    private String structName;
    private final List<Object[]> fieldValues;

    public StructLiteral(String structName, List<Object[]> fieldValues, int line, int column) {
        super(line, column);
        this.structName = structName;
        this.fieldValues = fieldValues;
    }

    public String getStructName() {
        return structName;
    }

    public void setStructName(String structName) {
        this.structName = structName;
    }

    public List<Object[]> getFieldValues() {
        return fieldValues;
    }
    
    public static class Context {
 
        public final String structName;
        public final List<Object[]> fieldValues;
        public final int line;
        public final int column;
 
        public Context(StructLiteral node) {
            this.structName  = node.structName;
            this.fieldValues = node.fieldValues;
            this.line        = node.getLine();
            this.column      = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }
    
}
