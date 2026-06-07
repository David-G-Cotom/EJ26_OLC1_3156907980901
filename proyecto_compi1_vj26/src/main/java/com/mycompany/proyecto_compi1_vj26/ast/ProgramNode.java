/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast;

import com.mycompany.proyecto_compi1_vj26.ast.statements.FuncDecl;

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
    
}
