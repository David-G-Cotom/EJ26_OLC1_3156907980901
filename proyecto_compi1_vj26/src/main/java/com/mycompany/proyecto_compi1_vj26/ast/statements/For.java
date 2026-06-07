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
public class For extends BaseNode {
    
    private final ASTNode init;
    private final ASTNode condition;
    private final ASTNode increment;
    private final Block body;

    public For(ASTNode init, ASTNode condition, ASTNode increment, Block body, int line, int column) {
        super(line, column);
        this.init = init;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    public ASTNode getInit() {
        return init;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public ASTNode getIncrement() {
        return increment;
    }

    public Block getBody() {
        return body;
    }
    
}
