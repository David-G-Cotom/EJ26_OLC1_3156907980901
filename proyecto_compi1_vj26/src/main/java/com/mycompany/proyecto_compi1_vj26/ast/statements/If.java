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
public class If extends BaseNode {

    private final ASTNode condition;
    private final Block thenBlock;
    private final List<ASTNode> elseIfConditions;
    private final List<Block> elseIfBlocks;
    private final Block elseBlock;  // null si no existe

    public If(ASTNode condition, Block thenBlock, List<ASTNode> elseIfConditions, List<Block> elseIfBlocks, Block elseBlock, int line, int column) {
        super(line, column);
        this.condition = condition;
        this.thenBlock = thenBlock;
        this.elseIfConditions = elseIfConditions;
        this.elseIfBlocks = elseIfBlocks;
        this.elseBlock = elseBlock;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public Block getThenBlock() {
        return thenBlock;
    }

    public List<ASTNode> getElseIfConditions() {
        return elseIfConditions;
    }

    public List<Block> getElseIfBlocks() {
        return elseIfBlocks;
    }

    public Block getElseBlock() {
        return elseBlock;
    }

    public boolean hasElse() {
        return this.elseBlock != null;
    }

}
