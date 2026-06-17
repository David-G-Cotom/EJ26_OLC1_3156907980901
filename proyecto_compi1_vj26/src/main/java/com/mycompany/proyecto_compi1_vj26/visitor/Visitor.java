/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.ProgramNode;
import com.mycompany.proyecto_compi1_vj26.ast.expressions.*;
import com.mycompany.proyecto_compi1_vj26.ast.statements.*;

/**
 *
 * @author david
 * @param <T>
 */
public interface Visitor<T> {

    T visit(ASTNode node);

    T visit(ProgramNode.Context ctx);

    T visit(Binary.Context ctx);

    T visit(FuncCall.Context ctx);

    T visit(Identifier.Context ctx);

    T visit(Literal.Context ctx);

    T visit(Unary.Context ctx);

    T visit(Assign.Context ctx);

    T visit(Block.Context ctx);

    T visit(Break.Context ctx);

    T visit(Continue.Context ctx);

    T visit(For.Context ctx);

    T visit(ForCond.Context ctx);

    T visit(FuncDecl.Context ctx);

    T visit(If.Context ctx);

    T visit(ImplicitAssign.Context ctx);

    T visit(ImplicitVarDecl.Context ctx);

    T visit(IncDec.Context ctx);

    T visit(Switch.Context ctx);

    T visit(VarDecl.Context ctx);

}
