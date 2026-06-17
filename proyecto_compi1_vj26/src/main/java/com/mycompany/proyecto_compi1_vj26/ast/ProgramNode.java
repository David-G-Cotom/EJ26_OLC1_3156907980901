/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast;

import com.mycompany.proyecto_compi1_vj26.ast.statements.FuncDecl;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class ProgramNode extends BaseNode {

    private final FuncDecl mainFunction;
    private final List<FuncDecl> userFunctions;

    public ProgramNode(FuncDecl mainFunction, List<FuncDecl> userFunctions, int line, int column) {
        super(line, column);
        this.mainFunction = mainFunction;
        this.userFunctions = userFunctions;
    }

    public FuncDecl getMainFunction() {
        return mainFunction;
    }

    public List<FuncDecl> getUserFunctions() {
        return userFunctions;
    }

    public static class Context {

        public final FuncDecl mainFunction;
        public final List<FuncDecl> userFunctions;
        public final int line;
        public final int column;

        public Context(ProgramNode node) {
            this.mainFunction = node.mainFunction;
            this.userFunctions = node.userFunctions;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
