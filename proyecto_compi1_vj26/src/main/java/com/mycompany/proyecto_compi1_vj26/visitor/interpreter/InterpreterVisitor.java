/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter;

import com.mycompany.proyecto_compi1_vj26.ast.ASTNode;
import com.mycompany.proyecto_compi1_vj26.ast.ProgramNode;
import com.mycompany.proyecto_compi1_vj26.ast.expressions.*;
import com.mycompany.proyecto_compi1_vj26.ast.statements.*;
import com.mycompany.proyecto_compi1_vj26.exceptions.*;
import com.mycompany.proyecto_compi1_vj26.models.*;
import com.mycompany.proyecto_compi1_vj26.symbols.*;
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
    private int loopDepth = 0;

    public InterpreterVisitor() {
        this.semanticErrors = new ArrayList<>();
        this.symbolTable = new SymbolTable(this.semanticErrors);
        this.output = new StringBuilder();
    }

    public String getOutput() {
        return output.toString();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public List<ErrorReport> getSemanticErrors() {
        return semanticErrors;
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
        this.loopDepth = 0;

        for (FuncDecl func : ctx.userFunctions) {
            if (func != null) {
                this.symbolTable.declareFunction(
                        func.getName(), func, func.getLine(), func.getColumn()
                );
            }
        }

        try {
            visit(ctx.mainFunction);
        } catch (ReturnException e) {
            // return en main: termina la ejecucion
        } catch (BreakException e) {
            this.addError("'break' usado fuera de un bucle", -1, -1);
        } catch (ContinueException e) {
            this.addError("'continue' usado fuera de un bucle", -1, -1);
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(Binary.Context ctx) {
        BinaryOperator op = ctx.operator;

        // Cortocircuito para && y ||
        if (op == BinaryOperator.AND) {
            ValueWrapper left = visit(ctx.left);
            if (!(left instanceof BoolValue l)) {
                this.addError("El operador '&&' requiere operandos bool",
                        ctx.line, ctx.column);
                return new NullValue(ctx.line, ctx.column);
            }
            if (!l.value()) {
                return new BoolValue(false, ctx.line, ctx.column);
            }
            ValueWrapper right = visit(ctx.right);
            if (!(right instanceof BoolValue r)) {
                this.addError("El operador '&&' requiere operandos bool",
                        ctx.line, ctx.column);
                return new NullValue(ctx.line, ctx.column);
            }
            return new BoolValue(r.value(), ctx.line, ctx.column);
        }

        if (op == BinaryOperator.OR) {
            ValueWrapper left = visit(ctx.left);
            if (!(left instanceof BoolValue l)) {
                addError("El operador '||' requiere operandos bool",
                        ctx.line, ctx.column);
                return new NullValue(ctx.line, ctx.column);
            }
            if (l.value()) {
                return new BoolValue(true, ctx.line, ctx.column);
            }
            ValueWrapper right = visit(ctx.right);
            if (!(right instanceof BoolValue r)) {
                addError("El operador '||' requiere operandos bool",
                        ctx.line, ctx.column);
                return new NullValue(ctx.line, ctx.column);
            }
            return new BoolValue(r.value(), ctx.line, ctx.column);
        }

        ValueWrapper left = visit(ctx.left);
        ValueWrapper right = visit(ctx.right);

        return switch (op) {
            case BinaryOperator.SUMA, BinaryOperator.RESTA, BinaryOperator.MULTIPLICACION, BinaryOperator.DIVISION, BinaryOperator.MODULO ->
                this.applyArithmetic(left, op, right, ctx.line, ctx.column);
            case BinaryOperator.IGUALDAD, BinaryOperator.NO_IGUAL ->
                this.applyEquality(left, op, right, ctx.line, ctx.column);
            case BinaryOperator.MENOR, BinaryOperator.MAYOR, BinaryOperator.MENOR_IGUAL, BinaryOperator.MAYOR_IGUAL ->
                this.applyRelational(left, op, right, ctx.line, ctx.column);
            default -> {
                this.addError("Operador binario desconocido: \"" + op + "\"",
                        ctx.line, ctx.column);
                yield new NullValue(ctx.line, ctx.column);
            }
        };
    }

    @Override
    public ValueWrapper visit(FuncCall.Context ctx) {
        ValueWrapper value = new NullValue(ctx.line, ctx.column);
        switch (ctx.functionName) {
            case FuncName.RETURN -> {
                ValueWrapper val = ctx.args.isEmpty() ? new NullValue(ctx.line, ctx.column) : visit(ctx.args.get(0));
                throw new ReturnException(val);
            }
            case FuncName.FMT_PRINTLN -> {
                StringBuilder line = new StringBuilder();
                for (ASTNode arg : ctx.args) {
                    ValueWrapper val = visit(arg);
                    line.append(this.formatValue(val));
                    line.append(" ");
                }
                this.output.append(line).append("\n");
            }
            case FuncName.STRCONV_ATOI ->
                value = this.evaluate(ctx); // usado como expresion RETORNAR
            case FuncName.STRCONV_PARSE_FLOAT ->
                value = this.evaluate(ctx); // usado como expresion RETORNAR
            case FuncName.REFLECT_TYPE_OF ->
                value = this.evaluate(ctx); // usado como expresion RETORNAR
            case FuncName.APPEND ->
                value = this.evaluate(ctx);
            case FuncName.LEN ->
                value = this.evaluate(ctx);
            case FuncName.SLICES_INDEX ->
                value = this.evaluate(ctx);
            case FuncName.STRINGS_JOIN ->
                value = this.evaluate(ctx);
        }
        if (!(value instanceof NullValue)) {
            return value;
        }
        return this.defaultVoid;
    }

    private ValueWrapper evaluate(FuncCall.Context ctx) {
        return switch (ctx.functionName) {
            case FuncName.FMT_PRINTLN -> {
                visit(ctx);
                yield new NullValue(ctx.line, ctx.column);
            }
            case FuncName.STRCONV_ATOI -> {
                if (ctx.args.isEmpty()) {
                    this.addError("strconv.Atoi requiere un argumento", ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper arg = visit(ctx.args.get(0));
                if (!(arg instanceof StringValue s)) {
                    this.addError("strconv.Atoi: el argumento debe ser string",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                try {
                    yield new IntValue(Integer.parseInt(s.value().trim()), s.line(), s.column());
                } catch (NumberFormatException e) {
                    this.addError("strconv.Atoi: \"" + s.value() + "\" no es un entero valido",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
            }
            case FuncName.STRCONV_PARSE_FLOAT -> {
                if (ctx.args.isEmpty()) {
                    this.addError("strconv.ParseFloat requiere un argumento",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper arg = visit(ctx.args.get(0));
                if (!(arg instanceof StringValue s)) {
                    this.addError("strconv.ParseFloat: el argumento debe ser string",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                try {
                    yield new DoubleValue(Double.parseDouble(s.value().trim()), s.line(), s.column());
                } catch (NumberFormatException e) {
                    this.addError("strconv.ParseFloat: \"" + s.value() + "\" no es un decimal valido",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
            }
            case FuncName.REFLECT_TYPE_OF -> {
                if (ctx.args.isEmpty()) {
                    addError("reflect.TypeOf requiere un argumento",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper arg = visit(ctx.args.get(0));
                yield new StringValue(arg.getType(), arg.line(), arg.column());
            }
            case FuncName.RETURN -> {
                yield new NullValue(ctx.line, ctx.column);
            }
            case APPEND -> {
                if (ctx.args.size() != 2) {
                    this.addError("append() requiere exactamente 2 argumentos", ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper sliceVal = visit(ctx.args.get(0));
                ValueWrapper elem = visit(ctx.args.get(1));
                if (!(sliceVal instanceof SliceValue sv)) {
                    this.addError("El primer argumento de append() debe ser un slice",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                // append devuelve un nuevo SliceValue
                List<ValueWrapper> newElems = new ArrayList<>(sv.getElements());
                newElems.add(elem);
                yield new SliceValue(newElems, sv.getSliceType(), ctx.line, ctx.column);
            }

            case LEN -> {
                if (ctx.args.size() != 1) {
                    this.addError("len() requiere exactamente 1 argumento", ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper target = visit(ctx.args.get(0));
                switch (target) {
                    case SliceValue sv -> {
                        yield new IntValue(sv.size(), ctx.line, ctx.column);
                    }
                    case StringValue str -> {
                        yield new IntValue(str.value().length(), ctx.line, ctx.column);
                    }
                    default -> {
                        this.addError("len() solo aplica a slices y strings", ctx.line, ctx.column);
                        yield new NullValue(ctx.line, ctx.column);
                    }
                }
            }

            case SLICES_INDEX -> {
                if (ctx.args.size() != 2) {
                    this.addError("slices.Index() requiere exactamente 2 argumentos",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper val = visit(ctx.args.get(0));
                if (!(val instanceof SliceValue sv)) {
                    this.addError("El primer argumento de slices.Index() debe ser un slice",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper target = visit(ctx.args.get(1));
                for (int i = 0; i < sv.size(); i++) {
                    ValueWrapper cmp = applyEquality(sv.getElement(i), BinaryOperator.IGUALDAD,
                            target, ctx.line, ctx.column);
                    if (cmp instanceof BoolValue bv && bv.value()) {
                        yield new IntValue(i, ctx.line, ctx.column);
                    }
                }
                yield new IntValue(-1, ctx.line, ctx.column); // no encontrado
            }

            case STRINGS_JOIN -> {
                if (ctx.args.size() != 2) {
                    this.addError("strings.Join() requiere exactamente 2 argumentos",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper val = visit(ctx.args.get(0));
                if (!(val instanceof SliceValue sv)) {
                    this.addError("El primer argumento de strings.Join() debe ser un slice",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                ValueWrapper str = visit(ctx.args.get(1));
                if (!(str instanceof StringValue sep)) {
                    this.addError("El segundo argumento de strings.Join() debe ser string",
                            ctx.line, ctx.column);
                    yield new NullValue(ctx.line, ctx.column);
                }
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sv.size(); i++) {
                    if (i > 0) {
                        sb.append(sep.value());
                    }
                    sb.append(sv.getElement(i).toString());
                }
                yield new StringValue(sb.toString(), ctx.line, ctx.column);
            }
        };
    }

    @Override
    public ValueWrapper visit(Identifier.Context ctx) {
        Symbol sym = this.symbolTable.lookUp(ctx.name,
                ctx.line, ctx.column);
        return sym != null ? sym.getValue() : new NullValue(ctx.line, ctx.column);
    }

    @Override
    public ValueWrapper visit(IndexAccess.Context ctx) {
        ValueWrapper target = visit(ctx.slice);
        ValueWrapper idx = visit(ctx.index);

        if (!(target instanceof SliceValue sv)) {
            this.addError("Se intento indexar un valor que no es un slice",
                    ctx.line, ctx.column);
            return new NullValue(ctx.line, ctx.column);
        }
        if (!(idx instanceof IntValue iv)) {
            this.addError("El indice de un slice debe ser de tipo int",
                    ctx.line, ctx.column);
            return new NullValue(ctx.line, ctx.column);
        }
        int i = iv.value();
        if (i < 0 || i >= sv.size()) {
            this.addError("Índice " + i + " fuera de rango (tamaño " + sv.size() + ")",
                    ctx.line, ctx.column);
            return new NullValue(ctx.line, ctx.column);
        }
        return sv.getElement(i);
    }

    @Override
    public ValueWrapper visit(Literal.Context ctx) {
        return ctx.value;
    }

    @Override
    public ValueWrapper visit(SliceLiteral.Context ctx) {
        if (ctx.sliceType == null) {
            this.addError("No se pudo inferir el tipo del slice anidado",
                    ctx.line, ctx.column);
            return new NullValue(ctx.line, ctx.column);
        }
        List<ValueWrapper> elements = new ArrayList<>();
        VarType elemType = ctx.sliceType.elementType();
        for (ASTNode elem : ctx.elements) {
            elements.add(this.resolveAndVisitSliceElement(elem, elemType));
        }

        return new SliceValue(elements, ctx.sliceType, ctx.line, ctx.column);
    }

    private ValueWrapper resolveAndVisitSliceElement(ASTNode elem, VarType expectedType) {
        if (expectedType.isSlice() && elem instanceof SliceLiteral sl) {
            SliceLiteral typed = new SliceLiteral(
                    expectedType,
                    sl.getElements(),
                    sl.getLine(),
                    sl.getColumn()
            );
            return typed.accept(this);
        }
        return visit(elem);
    }

    @Override
    public ValueWrapper visit(Unary.Context ctx) {
        ValueWrapper val = visit(ctx.operand);

        return switch (ctx.operator) {
            case "-" -> {
                if (val instanceof IntValue i) {
                    yield new IntValue(-i.value(), i.line(), i.column());
                }
                if (val instanceof DoubleValue d) {
                    yield new DoubleValue(-d.value(), d.line(), d.column());
                }
                this.addError("El operador '-' unario solo aplica a int o float64",
                        ctx.line, ctx.column);
                yield new NullValue(ctx.line, ctx.column);
            }
            case "!" -> {
                if (val instanceof BoolValue b) {
                    yield new BoolValue(!b.value(), b.line(), b.column());
                }
                this.addError("El operador '!' solo aplica a bool",
                        ctx.line, ctx.column);
                yield new NullValue(ctx.line, ctx.column);
            }
            default -> {
                this.addError("Operador unario desconocido: \"" + ctx.operator + "\"",
                        ctx.line, ctx.column);
                yield new NullValue(ctx.line, ctx.column);
            }
        };
    }

    @Override
    public ValueWrapper visit(UserFuncCall.Context ctx) {
        FuncDecl decl = this.symbolTable.lookUpFunction(ctx.name, ctx.line, ctx.column);
        if (decl == null) {
            return new NullValue(ctx.line, ctx.column);
        }
        FuncDecl.Context funcCtx = new FuncDecl.Context(decl);

        // Validar cantidad de argumentos
        if (ctx.args.size() != funcCtx.params.size()) {
            this.addError(
                    "La funcion \"" + ctx.name + "\" espera " + funcCtx.params.size()
                    + " argumento(s), se recibieron " + ctx.args.size(),
                    ctx.line, ctx.column
            );
            return new NullValue(ctx.line, ctx.column);
        }

        // Cargar argumentos para evaluar
        List<ValueWrapper> evaluatedArgs = new ArrayList<>();
        for (ASTNode arg : ctx.args) {
            evaluatedArgs.add(visit(arg));
        }

        // Crear scope para la funcion y declarar parametros
        this.symbolTable.pushScope();
        try {
            for (int i = 0; i < funcCtx.params.size(); i++) {
                String paramName = (String) funcCtx.params.get(i)[0];
                VarType paramType = (VarType) funcCtx.params.get(i)[1];
                ValueWrapper argVal = this.coerce(evaluatedArgs.get(i), paramType, ctx.line, ctx.column);
                this.symbolTable.declare(paramName, paramType, argVal, ctx.line, ctx.column);
            }

            for (ASTNode stmt : funcCtx.body.getStatements()) {
                visit(stmt);
            }

            if (!funcCtx.isVoid()) {
                this.addError(
                        "La funcion \"" + ctx.name + "\" debe retornar un valor de tipo "
                        + funcCtx.returnType,
                        ctx.line, ctx.column
                );
            }
            return this.defaultVoid;

        } catch (ReturnException e) {
            ValueWrapper returnVal = e.getValue();
            if (funcCtx.isVoid()) {
                return this.defaultVoid;
            }
            // Validar tipo de retorno
            returnVal = this.coerce(returnVal, funcCtx.returnType, ctx.line, ctx.column);
            return returnVal;

        } finally {
            this.symbolTable.popScope();
        }
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
        try {
            for (ASTNode statement : ctx.statements) {
                visit(statement);
            }
        } finally {
            this.symbolTable.popScope();
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(Break.Context ctx) {
        if (this.loopDepth == 0) {
            this.addError("'break' usado fuera de un bucle", ctx.line, ctx.column);
            return this.defaultVoid;
        }
        throw new BreakException();
    }

    @Override
    public ValueWrapper visit(Continue.Context ctx) {
        if (this.loopDepth == 0) {
            this.addError("'continue' usado fuera de un bucle", ctx.line, ctx.column);
            return this.defaultVoid;
        }
        throw new ContinueException();
    }

    @Override
    public ValueWrapper visit(For.Context ctx) {
        this.loopDepth++;
        this.symbolTable.pushScope();
        try {
            visit(ctx.init);

            while (true) {
                this.symbolTable.setIsBlockFor(true);
                ValueWrapper cond = visit(ctx.condition);
                if (!(cond instanceof BoolValue c)) {
                    this.addError("La condicion del 'for' debe ser de tipo bool",
                            ctx.line, ctx.column);
                    break;
                }
                if (!c.value()) {
                    break;
                }

                try {
                    for (ASTNode stmt : ctx.body.getStatements()) {
                        visit(stmt);
                    }
                } catch (ContinueException ignored) {
                    // Continuar al incremento
                }

                visit(ctx.increment);
            }
        } catch (BreakException ignored) {
            // Sale del bucle
        } finally {
            this.symbolTable.setIsBlockFor(false);
            this.symbolTable.popScope();
            this.loopDepth--;
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(ForCond.Context ctx) {
        this.loopDepth++;
        try {
            while (true) {
                ValueWrapper cond = visit(ctx.condition);
                if (!(cond instanceof BoolValue c)) {
                    this.addError("La condicion del 'for' debe ser de tipo bool",
                            ctx.line, ctx.column);
                    break;
                }
                if (!c.value()) {
                    break;
                }

                try {
                    visit(ctx.body);
                } catch (ContinueException ignored) {
                    // Continuar al siguiente ciclo
                }
            }
        } catch (BreakException ignored) {
            // Sale del bucle
        } finally {
            this.loopDepth--;
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(ForRange.Context ctx) {
        ValueWrapper iterable = visit(ctx.iterable);

        if (!(iterable instanceof SliceValue sv)) {
            this.addError("'range' solo puede usarse con slices",
                    ctx.line, ctx.column);
            return this.defaultVoid;
        }

        this.loopDepth++;
        try {
            for (int i = 0; i < sv.size(); i++) {
                this.symbolTable.pushScope();
                try {
                    VarType elemType = sv.getSliceType().elementType();
                    this.symbolTable.declare(ctx.index, VarType.INT,
                            new IntValue(i, ctx.line, ctx.column),
                            ctx.line, ctx.column);
                    this.symbolTable.declare(ctx.value, elemType,
                            sv.getElement(i), ctx.line, ctx.column);

                    for (ASTNode stmt : ctx.body.getStatements()) {
                        visit(stmt);
                    }
                } catch (ContinueException e) {
                    // continuar a la siguiente iteracion
                } finally {
                    this.symbolTable.popScope();
                }
            }
        } catch (BreakException e) {
            // salir del for range
        } finally {
            this.loopDepth--;
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(FuncDecl.Context ctx) {
        this.symbolTable.pushScope();
        try {
            visit(ctx.body);
        } finally {
            this.symbolTable.popScope();
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(If.Context ctx) {
        ValueWrapper condVal = visit(ctx.condition);
        if (!(condVal instanceof BoolValue c)) {
            this.addError("La condicion del 'if' debe ser de tipo bool",
                    ctx.line, ctx.column);
            return this.defaultVoid;
        }
        if (c.value()) {
            visit(ctx.thenBlock);
            return this.defaultVoid;
        }

        // else if branches
        List<ASTNode> elseIfConds = ctx.elseIfConditions;
        List<Block> elseIfBlocks = ctx.elseIfBlocks;
        for (int i = 0; i < elseIfConds.size(); i++) {
            ValueWrapper condition = visit(elseIfConds.get(i));
            if (!(condition instanceof BoolValue cond)) {
                this.addError("La condicion del 'else if' debe ser de tipo bool",
                        elseIfConds.get(i).getLine(), elseIfConds.get(i).getColumn());
                return this.defaultVoid;
            }
            if (cond.value()) {
                visit(elseIfBlocks.get(i));
                return this.defaultVoid;
            }
        }

        // else
        if (ctx.hasElse()) {
            visit(ctx.elseBlock);
        }
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(ImplicitAssign.Context ctx) {
        Symbol sym = this.symbolTable.lookUp(ctx.name, ctx.line, ctx.column);
        if (sym == null) {
            return this.defaultVoid;
        }
        ValueWrapper right = visit(ctx.value);
        ValueWrapper result = this.applyArithmetic(sym.getValue(), ctx.operator == BinaryOperator.SUMA_IMPLICITA ? BinaryOperator.SUMA : BinaryOperator.RESTA, right, ctx.line, ctx.column);
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
    public ValueWrapper visit(IndexAssign.Context ctx) {
        ValueWrapper target = visit(ctx.slice);
        ValueWrapper idx = visit(ctx.index);
        ValueWrapper val = visit(ctx.value);

        if (!(target instanceof SliceValue sv)) {
            this.addError("Se intento asignar a un indice de un valor que no es un slice",
                    ctx.line, ctx.column);
            return this.defaultVoid;
        }
        if (!(idx instanceof IntValue iv)) {
            this.addError("El indice de un slice debe ser de tipo int",
                    ctx.line, ctx.column);
            return this.defaultVoid;
        }
        int i = iv.value();
        if (i < 0 || i >= sv.size()) {
            this.addError("Índice " + i + " fuera de rango (tamaño " + sv.size() + ")",
                    ctx.line, ctx.column);
            return this.defaultVoid;
        }

        ValueWrapper current = sv.getElement(i);
        ValueWrapper newVal = switch (ctx.operator) {
            case "=" ->
                val;
            case "+=" ->
                applyArithmetic(current, BinaryOperator.SUMA, val, ctx.line, ctx.column);
            case "-=" ->
                applyArithmetic(current, BinaryOperator.RESTA, val, ctx.line, ctx.column);
            default ->
                val;
        };
        sv.setElement(i, newVal);
        return this.defaultVoid;
    }

    @Override
    public ValueWrapper visit(Switch.Context ctx) {
        ValueWrapper switchVal = visit(ctx.condition);

        this.loopDepth++;
        try {
            for (int i = 0; i < ctx.caseExprs.size(); i++) {
                ValueWrapper caseVal = visit(ctx.caseExprs.get(i));
                ValueWrapper match = this.applyEquality(
                        switchVal, BinaryOperator.IGUALDAD, caseVal,
                        ctx.line, ctx.column);

                if (match instanceof BoolValue b && b.value()) {
                    this.symbolTable.pushScope();
                    try {
                        for (ASTNode stmt : ctx.caseBodies.get(i)) {
                            visit(stmt);
                        }
                    } catch (BreakException ignored) {
                        // break explicito dentro del case -> sale del switch
                    } finally {
                        this.symbolTable.popScope();
                    }
                    return this.defaultVoid; // break implicito: no evalua mas cases
                }
            }

            if (ctx.hasDefault()) {
                this.symbolTable.pushScope();
                try {
                    for (ASTNode stmt : ctx.defaultBody) {
                        visit(stmt);
                    }
                } catch (BreakException ignored) {
                    // break explicito en default
                } finally {
                    this.symbolTable.popScope();
                }
            }
        } finally {
            this.loopDepth--;
        }
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
                    yield new IntValue(c.value(), c.line(), c.column()); // rune -> int
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
                    yield new DoubleValue(Integer.valueOf(i.value()).doubleValue(), i.line(), i.column()); // int -> float64 implicito
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
                    yield new CharValue((char) i.value(), line, col); // int -> rune
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable rune",
                        line, col);
                yield new NullValue(line, col);
            }
            case VarType.NIL ->
                value;
            default -> {
                if (value instanceof SliceValue) {
                    yield value;
                }
                this.addError("No se puede asignar " + value.getType() + " a una variable slice",
                        line, col);
                yield new NullValue(line, col);
            }

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
        if (value instanceof SliceValue sv) {
            return sv.getSliceType();
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
            default ->
                new NullValue(line, col);
        };
    }

    private ValueWrapper applyArithmetic(ValueWrapper left, BinaryOperator op, ValueWrapper right, int line, int col) {
        if (left instanceof NullValue || right instanceof NullValue) {
            return new NullValue(line, col);
        }

        // string + string -> concatenacion
        if (op == BinaryOperator.SUMA && left instanceof StringValue l && right instanceof StringValue r) {
            return new StringValue(l.value() + r.value(), line, col);
        }

        // Normalizar rune -> int para operaciones
        if (left instanceof CharValue c) {
            left = new IntValue((int) c.value(), c.line(), c.column());
        }
        if (right instanceof CharValue c) {
            right = new IntValue((int) c.value(), c.line(), c.column());
        }

        // Ambos numericos
        if (this.isNumeric(left) && this.isNumeric(right)) {
            boolean useDouble = (left instanceof DoubleValue) || (right instanceof DoubleValue);
            double l = toDouble(left);
            double r = toDouble(right);

            // Division por cero
            if ((op == BinaryOperator.DIVISION || op == BinaryOperator.MODULO) && r == 0) {
                this.addError("Division por cero", line, col);
                return new NullValue(line, col);
            }

            if (useDouble) {
                return switch (op) {
                    case BinaryOperator.SUMA ->
                        new DoubleValue(l + r, line, col);
                    case BinaryOperator.RESTA ->
                        new DoubleValue(l - r, line, col);
                    case BinaryOperator.MULTIPLICACION ->
                        new DoubleValue(l * r, line, col);
                    case BinaryOperator.DIVISION ->
                        new DoubleValue(l / r, line, col);
                    case BinaryOperator.MODULO ->
                        new DoubleValue(l % r, line, col);
                    default ->
                        new NullValue(line, col);
                };
            } else {
                int li = (int) l, ri = (int) r;
                return switch (op) {
                    case BinaryOperator.SUMA ->
                        new IntValue(li + ri, line, col);
                    case BinaryOperator.RESTA ->
                        new IntValue(li - ri, line, col);
                    case BinaryOperator.MULTIPLICACION ->
                        new IntValue(li * ri, line, col);
                    case BinaryOperator.DIVISION ->
                        new IntValue(li / ri, line, col);
                    case BinaryOperator.MODULO ->
                        new IntValue(li % ri, line, col);
                    default ->
                        new NullValue(line, col);
                };
            }
        }

        this.addError("Tipos incompatibles para el operador '" + op + "': "
                + left.getType() + " y " + right.getType(), line, col);
        return new NullValue(line, col);
    }

    private ValueWrapper applyEquality(ValueWrapper left, BinaryOperator op, ValueWrapper right, int line, int col) {
        if (left instanceof NullValue && right instanceof NullValue) {
            return new BoolValue(op == BinaryOperator.IGUALDAD, line, col);
        }
        if (left instanceof NullValue || right instanceof NullValue) {
            return new BoolValue(op == BinaryOperator.NO_IGUAL, line, col);
        }

        // Normalizar rune -> int
        if (left instanceof CharValue c) {
            left = new IntValue((int) c.value(), c.line(), c.column());
        }
        if (right instanceof CharValue c) {
            right = new IntValue((int) c.value(), c.line(), c.column());
        }

        // Comparacion numerica con promocion
        if (this.isNumeric(left) && this.isNumeric(right)) {
            boolean eq = this.toDouble(left) == this.toDouble(right);
            return op == BinaryOperator.IGUALDAD ? new BoolValue(eq, line, col) : new BoolValue(!eq, line, col);
        }

        if (left instanceof StringValue sl && right instanceof StringValue sr) {
            boolean eq = sl.value().equals(sr.value());
            return op == BinaryOperator.IGUALDAD ? new BoolValue(eq, line, col) : new BoolValue(!eq, line, col);
        }

        if (left instanceof BoolValue bl && right instanceof BoolValue br) {
            boolean eq = bl.value() == br.value();
            return op == BinaryOperator.IGUALDAD ? new BoolValue(eq, line, col) : new BoolValue(!eq, line, col);
        }

        addError("Tipos incompatibles para '" + op + "': "
                + left.getType() + " y " + right.getType(), line, col);
        return new NullValue(line, col);
    }

    private ValueWrapper applyRelational(ValueWrapper left, BinaryOperator op, ValueWrapper right, int line, int col) {
        if (left instanceof NullValue || right instanceof NullValue) {
            addError("No se puede aplicar '" + op + "' a nil", line, col);
            return new NullValue(line, col);
        }

        // Normalizar rune -> int
        if (left instanceof CharValue c) {
            left = new IntValue((int) c.value(), c.line(), c.column());
        }
        if (right instanceof CharValue c) {
            right = new IntValue((int) c.value(), c.line(), c.column());
        }

        if (isNumeric(left) && isNumeric(right)) {
            double l = toDouble(left), r = toDouble(right);
            return switch (op) {
                case BinaryOperator.MENOR ->
                    new BoolValue(l < r, line, col);
                case BinaryOperator.MAYOR ->
                    new BoolValue(l > r, line, col);
                case BinaryOperator.MENOR_IGUAL ->
                    new BoolValue(l <= r, line, col);
                case BinaryOperator.MAYOR_IGUAL ->
                    new BoolValue(l >= r, line, col);
                default ->
                    new NullValue(line, col);
            };
        }

        if (left instanceof StringValue l && right instanceof StringValue r) {
            int cmp = l.value().compareTo(r.value());
            return switch (op) {
                case BinaryOperator.MENOR ->
                    new BoolValue(cmp < 0, line, col);
                case BinaryOperator.MAYOR ->
                    new BoolValue(cmp > 0, line, col);
                case BinaryOperator.MENOR_IGUAL ->
                    new BoolValue(cmp <= 0, line, col);
                case BinaryOperator.MAYOR_IGUAL ->
                    new BoolValue(cmp >= 0, line, col);
                default ->
                    new NullValue(line, col);
            };
        }

        this.addError("Tipos incompatibles para '" + op + "': "
                + left.getType() + " y " + right.getType(), line, col);
        return new NullValue(line, col);
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

    private String formatValue(ValueWrapper val) {
        if (val instanceof NullValue) {
            return "<nil>";
        }
        if (val instanceof BoolValue b) {
            return b.value() ? "true" : "false";
        }
        if (val instanceof DoubleValue d) {
            // Evitar ".0" innecesario solo si es entero exacto
            if (d.value() == Math.floor(d.value()) && !Double.isInfinite(d.value())) {
                return String.valueOf(Double.valueOf(d.value()).longValue());
            }
            return String.valueOf(Double.valueOf(d.value()));
        }
        if (val instanceof CharValue c) {
            return (int) c.value() + "";
        }
        if (val instanceof SliceValue sv) {
            return sv.toString();
        }
        return String.valueOf(val);
    }

    private void addError(String description, int line, int col) {
        this.semanticErrors.add(new ErrorReport(
                description, line, col, ErrorType.SEMANTICO
        ));
    }

}
