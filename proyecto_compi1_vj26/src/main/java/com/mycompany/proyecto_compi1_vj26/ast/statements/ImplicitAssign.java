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
public class ImplicitAssign extends BaseNode {
    
    private final String name;
    private final String operator;  // "+=" o "-="
    private final ASTNode value;

    public ImplicitAssign(String name, String operator, ASTNode value, int line, int column) {
        super(line, column);
        this.name = name;
        this.operator = operator;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getValue() {
        return value;
    }
    
}
