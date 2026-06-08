/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast;

import com.mycompany.proyecto_compi1_vj26.ast.statements.FuncDecl;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;

/**
 *
 * @author david
 */
public class ProgramNode extends BaseNode {

    private final FuncDecl mainFunction;

    public ProgramNode(FuncDecl mainFunction, int line, int column) {
        super(line, column);
        this.mainFunction = mainFunction;
    }

    public FuncDecl getMainFunction() {
        return mainFunction;
    }

    public static class Context {

        public final FuncDecl mainFunction;
        public final int line;
        public final int column;

        public Context(ProgramNode node) {
            this.mainFunction = node.mainFunction;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
