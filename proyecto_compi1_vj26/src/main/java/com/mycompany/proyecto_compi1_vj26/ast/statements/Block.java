/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import java.util.List;

/**
 *
 * @author david
 */
public class Block extends BaseNode {
    
    private final List<ASTNode> statements;

    public Block(List<ASTNode> statements, int line, int column) {
        super(line, column);
        this.statements = statements;
    }

    public List<ASTNode> getStatements() {
        return statements;
    }
    
}
