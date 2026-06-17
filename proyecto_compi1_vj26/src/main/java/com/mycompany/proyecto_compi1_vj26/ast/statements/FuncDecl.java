/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.ast.statements;

import com.mycompany.proyecto_compi1_vj26.ast.BaseNode;
import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import java.util.List;

/**
 *
 * @author david
 */
public class FuncDecl extends BaseNode {

    private final String name;
    private final List<Object[]> params;  // cada Object[] = { String nombre, VarType tipo }
    private final VarType returnType;     // null = void (sin retorno)
    private final Block body;

    public FuncDecl(String name, List<Object[]> params, VarType returnType, Block body, int line, int column) {
        super(line, column);
        this.name = name;
        this.params = params;
        this.returnType = returnType;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public List<Object[]> getParams() {
        return params;
    }

    public VarType getReturnType() {
        return returnType;
    }

    public Block getBody() {
        return body;
    }

    public boolean isVoid() {
        return returnType == null;
    }

    public static class Context {

        public final String name;
        public final List<Object[]> params;
        public final VarType returnType;
        public final Block body;
        public final int line;
        public final int column;

        public Context(FuncDecl node) {
            this.name = node.name;
            this.params = node.params;
            this.returnType = node.returnType;
            this.body = node.body;
            this.line = node.getLine();
            this.column = node.getColumn();
        }

        public boolean isVoid() {
            return returnType == null;
        }

    }

    @Override
    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(new Context(this));
    }

}
