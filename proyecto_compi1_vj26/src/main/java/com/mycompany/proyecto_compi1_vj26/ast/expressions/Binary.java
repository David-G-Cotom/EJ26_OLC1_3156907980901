/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.BinaryOperator;

/**
 *
 * @author david
 */
public class Binary extends BaseNode {
    
    private final ASTNode left;
    private final BinaryOperator operator;
    private final ASTNode right;

    public Binary(ASTNode left, BinaryOperator operator, ASTNode right, int line, int column) {
        super(line, column);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public BinaryOperator getOperator() {
        return operator;
    }

    public ASTNode getRight() {
        return right;
    }
    
}
