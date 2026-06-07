/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;

/**
 *
 * @author david
 */
public class FuncDecl extends BaseNode {
    
    private final String name;
    private final Block body;

    public FuncDecl(String name, Block body, int line, int column) {
        super(line, column);
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public Block getBody() {
        return body;
    }
    
}
