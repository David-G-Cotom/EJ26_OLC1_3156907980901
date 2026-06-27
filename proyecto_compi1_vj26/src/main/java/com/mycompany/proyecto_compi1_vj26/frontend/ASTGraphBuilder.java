/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.frontend;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.ProgramNode;
import com.mycompany.proyecto_compi1_vj26.ast.statements.*;
import com.mycompany.proyecto_compi1_vj26.ast.expressions.*;
import java.util.List;

/**
 *
 * @author david
 */
public class ASTGraphBuilder {

    private final StringBuilder dot = new StringBuilder();
    private int counter = 0;

    public String build(ProgramNode program) {
        this.dot.setLength(0);
        counter = 0;
        this.dot.append("graph AST {\n");
        this.dot.append("  node [shape=ellipse, fontname=\"Consolas\", fontsize=11, style=filled, fillcolor=\"#F5F8FF\", color=\"#5B7FBF\"];\n");
        this.dot.append("  edge [color=\"#7A7A7A\"];\n");

        String rootId = this.newId();
        this.emit(rootId, "ProgramNode");

        if (program.getMainFunction() != null) {
            String mainId = this.visit(program.getMainFunction());
            this.link(rootId, mainId);
        }
        for (FuncDecl fd : program.getUserFunctions()) {
            if (fd != null) {
                String id = this.visit(fd);
                this.link(rootId, id);
            }
        }
        for (StructDecl sd : program.getStructDecls()) {
            if (sd != null) {
                String id = this.visit(sd);
                this.link(rootId, id);
            }
        }

        this.dot.append("}\n");
        return this.dot.toString();
    }

    private String newId() {
        return "n" + (this.counter++);
    }

    private void emit(String id, String label) {
        this.dot.append("  ").append(id)
                .append(" [label=").append(escape(label)).append("];\n");
    }

    private void emitLeaf(String id, String label) {
        this.dot.append("  ").append(id)
                .append(" [label=").append(escape(label))
                .append(", shape=ellipse, fillcolor=\"#FFF6E0\", color=\"#C9A227\"];\n");
        this.dot.append("  // leaf\n");
    }

    private void link(String parentId, String childId) {
        if (childId == null) {
            return;
        }
        this.dot.append("  ").append(parentId).append(" -- ").append(childId).append(";\n");
    }

    private String escape(String s) {
        if (s == null) {
            s = "null";
        }
        String safe = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
        return "\"" + safe + "\"";
    }

    /**
     * Crea un nodo hoja simple y devuelve su id.
     */
    private String leaf(String label) {
        String id = this.newId();
        this.emitLeaf(id, label);
        return id;
    }

    private String visit(ASTNode node) {
        if (node == null) {
            return null;
        }

        // --- Statements ---
        if (node instanceof Block n) {
            return this.visitBlock(n);
        }
        if (node instanceof VarDecl n) {
            return this.visitVarDecl(n);
        }
        if (node instanceof ImplicitVarDecl n) {
            return this.visitImplicitVarDecl(n);
        }
        if (node instanceof Assign n) {
            return this.visitAssign(n);
        }
        if (node instanceof ImplicitAssign n) {
            return this.visitImplicitAssign(n);
        }
        if (node instanceof IndexAssign n) {
            return this.visitIndexAssign(n);
        }
        if (node instanceof FieldAssign n) {
            return this.visitFieldAssign(n);
        }
        if (node instanceof If n) {
            return this.visitIf(n);
        }
        if (node instanceof Switch n) {
            return this.visitSwitch(n);
        }
        if (node instanceof For n) {
            return this.visitFor(n);
        }
        if (node instanceof ForCond n) {
            return this.visitForCond(n);
        }
        if (node instanceof ForRange n) {
            return this.visitForRange(n);
        }
        if (node instanceof IncDec n) {
            return this.visitIncDec(n);
        }
        if (node instanceof Break) {
            return this.leafNode("Break");
        }
        if (node instanceof Continue) {
            return this.leafNode("Continue");
        }
        if (node instanceof FuncDecl n) {
            return this.visitFuncDecl(n);
        }
        if (node instanceof StructDecl n) {
            return this.visitStructDecl(n);
        }

        // --- Expressions ---
        if (node instanceof Binary n) {
            return this.visitBinary(n);
        }
        if (node instanceof Unary n) {
            return this.visitUnary(n);
        }
        if (node instanceof Literal n) {
            return this.visitLiteral(n);
        }
        if (node instanceof Identifier n) {
            return this.visitIdentifier(n);
        }
        if (node instanceof FuncCall n) {
            return this.visitFuncCall(n);
        }
        if (node instanceof UserFuncCall n) {
            return this.visitUserFuncCall(n);
        }
        if (node instanceof IndexAccess n) {
            return this.visitIndexAccess(n);
        }
        if (node instanceof FieldAccess n) {
            return this.visitFieldAccess(n);
        }
        if (node instanceof SliceLiteral n) {
            return this.visitSliceLiteral(n);
        }
        if (node instanceof StructLiteral n) {
            return this.visitStructLiteral(n);
        }

        // Caso no reconocido
        return this.leafNode(node.getClass().getSimpleName());
    }

    private String leafNode(String label) {
        String id = this.newId();
        this.emit(id, label);
        return id;
    }

    private String visitBlock(Block n) {
        String id = this.leafNode("Block");
        for (ASTNode stmt : n.getStatements()) {
            link(id, visit(stmt));
        }
        return id;
    }

    private String visitVarDecl(VarDecl n) {
        String typeLabel = n.isStructType() ? n.getStructTypeName() : n.getType().getType();
        String id = this.leafNode("VarDecl (" + n.getName() + ": " + typeLabel + ")");
        if (n.hasInitValue()) {
            this.link(id, visit(n.getInitValue()));
        }
        return id;
    }

    private String visitImplicitVarDecl(ImplicitVarDecl n) {
        String id = this.leafNode("ImplicitVarDecl (" + n.getName() + ")");
        this.link(id, visit(n.getValue()));
        return id;
    }

    private String visitAssign(Assign n) {
        String id = this.leafNode("Assign (" + n.getName() + ")");
        this.link(id, visit(n.getValue()));
        return id;
    }

    private String visitImplicitAssign(ImplicitAssign n) {
        String id = this.leafNode("ImplicitAssign (" + n.getName() + ", " + n.getOperator() + ")");
        this.link(id, visit(n.getValue()));
        return id;
    }

    private String visitIndexAssign(IndexAssign n) {
        String id = this.leafNode("IndexAssign (" + n.getOperator() + ")");
        this.link(id, visit(n.getSlice()));
        this.link(id, visit(n.getIndex()));
        this.link(id, visit(n.getValue()));
        return id;
    }

    private String visitFieldAssign(FieldAssign n) {
        String id = this.leafNode("FieldAssign (" + n.getFieldName() + ", " + n.getOperator() + ")");
        this.link(id, visit(n.getStruct()));
        this.link(id, visit(n.getValue()));
        return id;
    }

    private String visitIf(If n) {
        String id = this.leafNode("If");
        String condId = this.leafNode("Condition");
        this.link(condId, visit(n.getCondition()));
        this.link(id, condId);
        this.link(id, visitNamedBlock("Then", n.getThenBlock()));

        List<ASTNode> elseIfConds = n.getElseIfConditions();
        List<Block> elseIfBlocks = n.getElseIfBlocks();
        for (int i = 0; i < elseIfConds.size(); i++) {
            String eiId = this.leafNode("ElseIf");
            String eiCond = this.leafNode("Condition");
            this.link(eiCond, visit(elseIfConds.get(i)));
            this.link(eiId, eiCond);
            this.link(eiId, visitNamedBlock("Body", elseIfBlocks.get(i)));
            this.link(id, eiId);
        }

        if (n.hasElse()) {
            this.link(id, visitNamedBlock("Else", n.getElseBlock()));
        }
        return id;
    }

    private String visitNamedBlock(String label, Block block) {
        String id = this.leafNode(label);
        this.link(id, visit(block));
        return id;
    }

    private String visitSwitch(Switch n) {
        String id = this.leafNode("Switch");
        String condId = this.leafNode("Condition");
        this.link(condId, visit(n.getCondition()));
        this.link(id, condId);

        List<ASTNode> exprs = n.getCaseExprs();
        List<List<ASTNode>> bodies = n.getCaseBodies();
        for (int i = 0; i < exprs.size(); i++) {
            String caseId = this.leafNode("Case");
            String exprId = this.leafNode("Expr");
            this.link(exprId, visit(exprs.get(i)));
            this.link(caseId, exprId);
            for (ASTNode stmt : bodies.get(i)) {
                this.link(caseId, visit(stmt));
            }
            this.link(id, caseId);
        }

        if (n.hasDefault()) {
            String defId = this.leafNode("Default");
            for (ASTNode stmt : n.getDefaultBody()) {
                this.link(defId, visit(stmt));
            }
            this.link(id, defId);
        }
        return id;
    }

    private String visitFor(For n) {
        String id = this.leafNode("For");
        if (n.getInit() != null) {
            this.link(id, visitNamedWrap("Init", n.getInit()));
        }
        if (n.getCondition() != null) {
            this.link(id, visitNamedWrap("Condition", n.getCondition()));
        }
        if (n.getIncrement() != null) {
            this.link(id, visitNamedWrap("Increment", n.getIncrement()));
        }
        this.link(id, visit(n.getBody()));
        return id;
    }

    private String visitForCond(ForCond n) {
        String id = this.leafNode("ForCond");
        this.link(id, visitNamedWrap("Condition", n.getCondition()));
        this.link(id, visit(n.getBody()));
        return id;
    }

    private String visitForRange(ForRange n) {
        String id = this.leafNode("ForRange (" + n.getIndex() + ", " + n.getValue() + ")");
        this.link(id, visitNamedWrap("Iterable", n.getIterable()));
        this.link(id, visit(n.getBody()));
        return id;
    }

    private String visitNamedWrap(String label, ASTNode child) {
        String id = this.leafNode(label);
        this.link(id, visit(child));
        return id;
    }

    private String visitIncDec(IncDec n) {
        return this.leafNode("IncDec (" + n.getName() + ", " + n.getOperator() + ")");
    }

    private String visitFuncDecl(FuncDecl n) {
        String returnLabel = n.isVoid() ? "void" : n.getReturnType().getType();
        String id = this.leafNode("FuncDecl (" + n.getName() + "): " + returnLabel);
        for (Object[] param : n.getParams()) {
            String pName = (String) param[0];
            String pType = ((com.mycompany.proyecto_compi1_vj26.models.VarType) param[1]).getType();
            this.link(id, leaf("Param (" + pName + ": " + pType + ")"));
        }
        this.link(id, visit(n.getBody()));
        return id;
    }

    private String visitStructDecl(StructDecl n) {
        String id = this.leafNode("StructDecl (" + n.getName() + ")");
        for (Object[] field : n.getFields()) {
            String fName = (String) field[0];
            com.mycompany.proyecto_compi1_vj26.models.VarType fType
                    = (com.mycompany.proyecto_compi1_vj26.models.VarType) field[1];
            String fTypeLabel = fType.getType();
            this.link(id, leaf("Campo (" + fName + ": " + fTypeLabel + ")"));
        }
        return id;
    }

    private String visitBinary(Binary n) {
        String id = this.leafNode("Binary");
        this.link(id, visit(n.getLeft()));
        this.link(id, leaf(n.getOperator().toString()));
        this.link(id, visit(n.getRight()));
        return id;
    }

    private String visitUnary(Unary n) {
        String id = this.leafNode("Unary");
        this.link(id, leaf(n.getOperator()));
        this.link(id, visit(n.getOperand()));
        return id;
    }

    private String visitLiteral(Literal n) {
        String valueLabel = n.getValue() == null ? "nil" : n.getValue().toString();
        return this.leafNode("Literal (" + valueLabel + ")");
    }

    private String visitIdentifier(Identifier n) {
        return this.leafNode("Identifier (" + n.getName() + ")");
    }

    private String visitFuncCall(FuncCall n) {
        String id = this.leafNode("FuncCall (" + n.getFunctionName().getName() + ")");
        for (ASTNode arg : n.getArgs()) {
            this.link(id, visit(arg));
        }
        return id;
    }

    private String visitUserFuncCall(UserFuncCall n) {
        String id = this.leafNode("UserFuncCall (" + n.getName() + ")");
        for (ASTNode arg : n.getArgs()) {
            this.link(id, visit(arg));
        }
        return id;
    }

    private String visitIndexAccess(IndexAccess n) {
        String id = this.leafNode("IndexAccess");
        this.link(id, visit(n.getSlice()));
        this.link(id, visit(n.getIndex()));
        return id;
    }

    private String visitFieldAccess(FieldAccess n) {
        String id = this.leafNode("FieldAccess (" + n.getFieldName() + ")");
        this.link(id, visit(n.getStruct()));
        return id;
    }

    private String visitSliceLiteral(SliceLiteral n) {
        String typeLabel = n.getSliceType() == null ? "?" : n.getSliceType().getType();
        String label = "SliceLiteral (" + typeLabel + ")";
        String id = this.leafNode(label);
        for (ASTNode elem : n.getElements()) {
            this.link(id, visit(elem));
        }
        return id;
    }

    private String visitStructLiteral(StructLiteral n) {
        String id = this.leafNode("StructLiteral (" + n.getStructName() + ")");
        for (Object[] fv : n.getFieldValues()) {
            String fName = (String) fv[0];
            ASTNode valueExpr = (ASTNode) fv[1];
            String fieldId = this.leafNode("FieldInit (" + fName + ")");
            this.link(fieldId, visit(valueExpr));
            this.link(id, fieldId);
        }
        return id;
    }

}
