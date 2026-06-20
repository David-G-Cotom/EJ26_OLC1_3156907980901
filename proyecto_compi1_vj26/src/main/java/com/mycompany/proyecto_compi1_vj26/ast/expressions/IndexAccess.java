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
public class IndexAccess extends BaseNode {
    
    private final ASTNode slice;
    private final ASTNode index;

    public IndexAccess(ASTNode slice, ASTNode index, int line, int column) {
        super(line, column);
        this.slice = slice;
        this.index = index;
    }

    public ASTNode getSlice() {
        return slice;
    }

    public ASTNode getIndex() {
        return index;
    }
    
    public static class Context {
 
        public final ASTNode slice;
        public final ASTNode index;
        public final int line;
        public final int column;
 
        public Context(IndexAccess node) {
            this.slice = node.slice;
            this.index  = node.index;
            this.line   = node.getLine();
            this.column = node.getColumn();
        }
    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }
    
}
