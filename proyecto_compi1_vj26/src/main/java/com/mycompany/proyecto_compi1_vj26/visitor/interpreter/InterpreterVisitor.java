/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.ProgramNode;
import com.mycompany.proyecto_compi1_vj26.ast.expressions.*;
import com.mycompany.proyecto_compi1_vj26.ast.statements.*;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value.ValueWrapper;

/**
 *
 * @author david
 */
public class InterpreterVisitor implements Visitor<ValueWrapper> {

    @Override
    public ValueWrapper visit(ASTNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(ProgramNode.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Binary.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(FuncCall.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Identifier.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Literal.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Unary.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Assign.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Block.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Break.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(Continue.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(For.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(ForCond.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(FuncDecl.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(If.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(ImplicitAssign.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(ImplicitVarDecl.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(IncDec.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(VarDecl.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
