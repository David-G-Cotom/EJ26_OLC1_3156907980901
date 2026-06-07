/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;

/**
 *
 * @author david
 */
public class ForCond extends BaseNode {
    
    private final ASTNode condition;
    private final Block body;

    public ForCond(ASTNode condition, Block body, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public Block getBody() {
        return body;
    }
    
}
