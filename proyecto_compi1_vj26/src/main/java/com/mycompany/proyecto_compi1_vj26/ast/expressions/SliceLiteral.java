/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class SliceLiteral extends BaseNode {
    
    private final VarType sliceType;
    private final List<ASTNode> elements;

    public SliceLiteral(VarType sliceType, List<ASTNode> elements, int line, int column) {
        super(line, column);
        this.sliceType = sliceType;
        this.elements = elements;
    }

    public VarType getSliceType() {
        return sliceType;
    }

    public List<ASTNode> getElements() {
        return elements;
    }
    
    public static class Context {
 
        public final VarType sliceType;
        public final List<ASTNode> elements;
        public final int line;
        public final int column;
 
        public Context(SliceLiteral node) {
            this.sliceType = node.sliceType;
            this.elements  = node.elements;
            this.line      = node.getLine();
            this.column    = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }
    
}
