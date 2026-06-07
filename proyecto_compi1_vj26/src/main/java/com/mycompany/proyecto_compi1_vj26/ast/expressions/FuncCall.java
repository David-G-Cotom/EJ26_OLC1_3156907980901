/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.expressions;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import java.util.List;

/**
 *
 * @author david
 */
public class FuncCall extends BaseNode {
    
    private final String functionName;
    private final List<ASTNode> args;

    public FuncCall(String functionName, List<ASTNode> args, int line, int column) {
        super(line, column);
        this.functionName = functionName;
        this.args = args;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<ASTNode> getArgs() {
        return args;
    }
    
}
