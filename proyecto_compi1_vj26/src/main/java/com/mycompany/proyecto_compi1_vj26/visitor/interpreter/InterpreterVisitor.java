/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.ProgramNode;
import com.mycompany.proyecto_compi1_vj26.ast.expressions.*;
import com.mycompany.proyecto_compi1_vj26.ast.statements.*;
import com.mycompany.proyecto_compi1_vj26.models.ErrorReport;
import com.mycompany.proyecto_compi1_vj26.models.ErrorType;
import com.mycompany.proyecto_compi1_vj26.models.ValType;
import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.symbols.Symbol;
import com.mycompany.proyecto_compi1_vj26.symbols.SymbolTable;
import com.mycompany.proyecto_compi1_vj26.visitor.Visitor;
import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public class InterpreterVisitor implements Visitor<ValueWrapper> {

    private final StringBuilder output;
    private final ValueWrapper defaultVoid = new VoidValue(-1, -1);
    private final SymbolTable symbolTable;
    private final List<ErrorReport> semanticErrors;
    private int errorCounter;
    private int loopDepth = 0;

    public InterpreterVisitor() {
        this.symbolTable = new SymbolTable();
        this.semanticErrors = new ArrayList<>();
        this.output = new StringBuilder();
        this.errorCounter = 0;
    }

    public String getOutput() {
        return output.toString();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<ErrorReport> getSemanticErrors() {
        List<ErrorReport> all = new ArrayList<>(semanticErrors);
        all.addAll(symbolTable.getSemanticErrors());
        return all;
    }

    public int getErrorCounter() {
        return errorCounter;
    }

    public int getLoopDepth() {
        return loopDepth;
    }

    @Override
    public ValueWrapper visit(ASTNode node) {
        return node.accept(this);
    }

    @Override
    public ValueWrapper visit(ProgramNode.Context ctx) {
        this.symbolTable.reset();
        this.output.setLength(0);
        this.semanticErrors.clear();
        this.errorCounter = 0;
        this.loopDepth = 0;

        visit(ctx.mainFunction);
        return this.defaultVoid;
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
        Symbol sym = this.symbolTable.lookUp(ctx.name, ctx.line, ctx.column);
        if (sym == null) {
            return this.defaultVoid;
        }
        ValueWrapper newValue = visit(ctx.value);
        newValue = this.coerce(newValue, sym.getType(), ctx.line, ctx.column);
        if (newValue instanceof NullValue && !(sym.getType() == VarType.NIL)) {
            // Hubo error en asignacion de tipos
            return this.defaultVoid;
        }
        this.symbolTable.assign(ctx.name, newValue, ctx.line, ctx.column);
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(Block.Context ctx) {
        this.symbolTable.pushScope();
        for (ASTNode statement : ctx.statements) {
            visit(statement);
        }
        this.symbolTable.popScope();
        return this.defaultVoid;
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
        this.symbolTable.pushScope();
        visit(ctx.body);
        this.symbolTable.popScope();
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(If.Context ctx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ValueWrapper visit(ImplicitAssign.Context ctx) {
        Symbol sym = this.symbolTable.lookUp(ctx.name, ctx.line, ctx.column);
        if (sym == null) {
            return this.defaultVoid;
        }
        ValueWrapper right = visit(ctx.value);
        ValueWrapper result = this.applyArithmetic(sym.getValue(), ctx.operator.charAt(0) + "", right, ctx.line, ctx.column);
        if (result != null) {
            this.symbolTable.assign(ctx.name, result, ctx.line, ctx.column);
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(ImplicitVarDecl.Context ctx) {
        ValueWrapper value = visit(ctx.value);
        VarType type = this.inferType(value, ctx.line, ctx.column);
        this.symbolTable.declare(ctx.name, type, value, ctx.line, ctx.column);
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(IncDec.Context ctx) {
        Symbol sym = this.symbolTable.lookUp(ctx.name, ctx.line, ctx.column);
        if (sym == null) {
            return this.defaultVoid;
        }
        ValueWrapper val = sym.getValue();
        ValueWrapper result;
        switch (val) {
            case IntValue i ->
                result = ctx.operator.equals("++") ? new IntValue(i.value() + 1, ctx.line, ctx.column) : new IntValue(i.value() - 1, ctx.line, ctx.column);
            case DoubleValue d ->
                result = ctx.operator.equals("++") ? new DoubleValue(d.value() + 1.0, ctx.line, ctx.column) : new DoubleValue(d.value() - 1.0, ctx.line, ctx.column);
            default -> {
                this.addError("El operador \"" + ctx.operator + "\" solo aplica a int o float64",
                        ctx.line, ctx.column);
                return this.defaultVoid;
            }
        }
        this.symbolTable.assign(ctx.name, result, ctx.line, ctx.column);
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(VarDecl.Context ctx) {
        ValueWrapper value;
        if (ctx.hasInitValue()) {
            ValueWrapper rawValue = visit(ctx.initValue);
            value = this.coerce(rawValue, ctx.type, ctx.line, ctx.column);
        } else {
            value = this.defaultValue(ctx.type, ctx.line, ctx.column);
        }
        this.symbolTable.declare(ctx.name, ctx.type, value, ctx.line, ctx.column);
        return this.defaultVoid;
    }

    private ValueWrapper coerce(ValueWrapper value, VarType targetType, int line, int col) {
        if (value instanceof NullValue) {
            return value;
        }

        return switch (targetType) {
            case VarType.INT -> {
                if (value instanceof IntValue) {
                    yield value;
                }
                if (value instanceof CharValue c) {
                    yield new IntValue(c.value(), c.line(), c.column()); // rune → int
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable int",
                        line, col);
                yield new NullValue(line, col);
            }
            case VarType.FLOAT -> {
                if (value instanceof DoubleValue) {
                    yield value;
                }
                if (value instanceof IntValue i) {
                    yield new DoubleValue(Integer.valueOf(i.value()).doubleValue(), i.line(), i.column()); // int → float64 implícito
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable float64",
                        line, col);
                yield new NullValue(line, col);
            }
            case VarType.STRING -> {
                if (value instanceof StringValue) {
                    yield value;
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable string",
                        line, col);
                yield new NullValue(line, col);
            }
            case VarType.BOOL -> {
                if (value instanceof BoolValue) {
                    yield value;
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable bool",
                        line, col);
                yield new NullValue(line, col);
            }
            case VarType.RUNE -> {
                if (value instanceof CharValue) {
                    yield value;
                }
                if (value instanceof IntValue i) {
                    yield new CharValue((char) i.value(), line, col); // int → rune
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable rune",
                        line, col);
                yield new NullValue(line, col);
            }
            case VarType.NIL ->
                value;
        };
    }

    private VarType inferType(ValueWrapper value, int line, int col) {
        if (value instanceof NullValue) {
            return VarType.NIL;
        }
        if (value instanceof IntValue) {
            return VarType.INT;
        }
        if (value instanceof DoubleValue) {
            return VarType.FLOAT;
        }
        if (value instanceof StringValue) {
            return VarType.STRING;
        }
        if (value instanceof BoolValue) {
            return VarType.BOOL;
        }
        if (value instanceof CharValue) {
            return VarType.RUNE;
        }
        this.addError("No se puede inferir el tipo del valor: " + value.getType(), line, col);
        return VarType.NIL;
    }

    private ValueWrapper defaultValue(VarType type, int line, int col) {
        return switch (type) {
            case VarType.INT ->
                new IntValue(0, line, col);
            case VarType.FLOAT ->
                new DoubleValue(0.0, line, col);
            case VarType.STRING ->
                new StringValue("", line, col);
            case VarType.BOOL ->
                new BoolValue(false, line, col);
            case VarType.RUNE ->
                new CharValue('\0', line, col);
            case VarType.NIL ->
                new NullValue(line, col);
        };
    }

    private ValueWrapper applyArithmetic(ValueWrapper left, String op, ValueWrapper right, int line, int col) {
        if (left instanceof NullValue || right instanceof NullValue) {
            return new NullValue(line, col);
        }

        // string + string -> concatenación
        if (op.equals("+") && left instanceof StringValue l && right instanceof StringValue r) {
            return new StringValue(l.value() + r.value(), line, col);
        }

        // Normalizar rune -> int para operaciones
        if (left instanceof CharValue c) {
            left = new IntValue((int) c.value(), c.line(), c.column());
        }
        if (right instanceof CharValue c) {
            right = new IntValue((int) c.value(), c.line(), c.column());
        }

        // Ambos numéricos
        if (this.isNumeric(left) && this.isNumeric(right)) {
            boolean useDouble = (left instanceof DoubleValue) || (right instanceof DoubleValue);
            double l = toDouble(left);
            double r = toDouble(right);

            // División por cero
            if ((op.equals("/") || op.equals("%")) && r == 0) {
                this.addError("División por cero", line, col);
                return new NullValue(line, col);
            }

            if (useDouble) {
                return switch (op) {
                    case "+" ->
                        new DoubleValue(l + r, line, col);
                    case "-" ->
                        new DoubleValue(l - r, line, col);
                    case "*" ->
                        new DoubleValue(l * r, line, col);
                    case "/" ->
                        new DoubleValue(l / r, line, col);
                    case "%" ->
                        new DoubleValue(l % r, line, col);
                    default ->
                        new NullValue(line, col);
                };
            } else {
                int li = (int) l, ri = (int) r;
                return switch (op) {
                    case "+" ->
                        new IntValue(li + ri, line, col);
                    case "-" ->
                        new IntValue(li - ri, line, col);
                    case "*" ->
                        new IntValue(li * ri, line, col);
                    case "/" ->
                        new IntValue(li / ri, line, col);
                    case "%" ->
                        new IntValue(li % ri, line, col);
                    default ->
                        new NullValue(line, col);
                };
            }
        }

        this.addError("Tipos incompatibles para el operador '" + op + "': "
                + left.getType() + " y " + right.getType(), line, col);
        return null;
    }

    private boolean isNumeric(ValueWrapper v) {
        return v instanceof IntValue || v instanceof DoubleValue;
    }

    private double toDouble(ValueWrapper v) {
        if (v instanceof IntValue i) {
            return Integer.valueOf(i.value()).doubleValue();
        }
        if (v instanceof DoubleValue d) {
            return d.value();
        }
        return 0.0;
    }

    private void addError(String description, int line, int col) {
        this.errorCounter++;
        this.semanticErrors.add(new ErrorReport(
                this.errorCounter, description, line, col, ErrorType.SEMANTICO
        ));
    }

}
