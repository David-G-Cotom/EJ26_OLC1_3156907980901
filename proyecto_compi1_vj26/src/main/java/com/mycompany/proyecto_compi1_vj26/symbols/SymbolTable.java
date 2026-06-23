/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.symbols;

import com.mycompany.proyecto_compi1_vj26.ast.statements.FuncDecl;
import com.mycompany.proyecto_compi1_vj26.ast.statements.StructDecl;
import com.mycompany.proyecto_compi1_vj26.models.ErrorReport;
import com.mycompany.proyecto_compi1_vj26.models.ErrorType;
import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value.ValueWrapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class SymbolTable {

    private final List<LinkedHashMap<String, Symbol>> scopes;
    private final HashMap<String, FuncDecl> functions;
    private final HashMap<String, StructDecl> structs;

    private int currentLevel;
    private boolean isBlockFor;

    private final List<ErrorReport> semanticErrors;

    public SymbolTable(List<ErrorReport> semanticErrors) {
        this.scopes = new ArrayList<>();
        this.functions = new HashMap<>();
        this.structs = new HashMap<>();
        this.semanticErrors = semanticErrors;
        this.currentLevel = -1;
        this.isBlockFor = false;
    }

    public HashMap<String, FuncDecl> getFunctions() {
        return functions;
    }

    public HashMap<String, StructDecl> getStructs() {
        return structs;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isIsBlockFor() {
        return isBlockFor;
    }

    public void setIsBlockFor(boolean isBlockFor) {
        this.isBlockFor = isBlockFor;
    }

    public List<ErrorReport> getSemanticErrors() {
        return semanticErrors;
    }

    public void pushScope() {
        this.currentLevel++;
        this.scopes.add(new LinkedHashMap<>());
    }

    public void popScope() {
        if (!this.scopes.isEmpty()) {
            this.scopes.remove(scopes.size() - 1);
            this.currentLevel--;
        }
    }

    public boolean declare(String name, VarType type, ValueWrapper value, int line, int column) {
        LinkedHashMap<String, Symbol> currentScope = this.currentScope();

        if (currentScope.containsKey(name)) {
            if (this.isBlockFor && !currentScope.keySet().iterator().next().equals(name)) {
                currentScope.get(name).setValue(value);
                return true;
            } else {
                this.addError(
                        "La variable \"" + name + "\" ya fue declarada en este ámbito",
                        line, column
                );
                return false;
            }
        }

        currentScope.put(name, new Symbol(name, type, value, this.currentLevel, line, column));
        return true;
    }

    public Symbol lookUp(String name, int line, int column) {
        Symbol sym = null;
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            sym = this.scopes.get(i).get(name);
            if (sym != null) {
                break;
            }
        }
        if (sym == null) {
            this.addError("La variable \"" + name + "\" no ha sido declarada", line, column);
        }
        return sym;
    }

    public boolean existsInCurrentScope(String name) {
        return this.currentScope().containsKey(name);
    }

    private LinkedHashMap<String, Symbol> currentScope() {
        if (this.scopes.isEmpty()) {
            throw new IllegalStateException("No hay ningún scope abierto.");
        }
        return this.scopes.get(this.scopes.size() - 1);
    }

    public boolean declareFunction(String name, FuncDecl decl, int line, int column) {
        if (this.functionExists(name)) {
            this.addError(
                    "La función \"" + name + "\" ya fue declarada",
                    line, column
            );
            return false;
        }
        this.functions.put(name, decl);
        return true;
    }

    public FuncDecl lookUpFunction(String name, int line, int column) {
        FuncDecl decl = this.functions.get(name);
        if (decl == null) {
            this.addError(
                    "La función \"" + name + "\" no ha sido declarada",
                    line, column
            );
        }
        return decl;
    }

    public boolean functionExists(String name) {
        return this.functions.containsKey(name);
    }

    public boolean declareStruct(String name, StructDecl decl, int line, int column) {
        if (this.structs.containsKey(name)) {
            this.addError(
                    "El struct \"" + name + "\" ya fue declarado",
                    line, column
            );
            return false;
        }
        this.structs.put(name, decl);
        return true;
    }

    public StructDecl lookUpStruct(String name, int line, int column) {
        StructDecl decl = this.structs.get(name);
        if (decl == null) {
            this.addError(
                    "El struct \"" + name + "\" no ha sido declarado",
                    line, column
            );
        }
        return decl;
    }

    public boolean structExists(String name) {
        return this.structs.containsKey(name);
    }

    public boolean assign(String name, ValueWrapper newValue, int line, int column) {
        for (int i = this.scopes.size() - 1; i >= 0; i--) {
            Symbol sym = this.scopes.get(i).get(name);
            if (sym != null) {
                sym.setValue(newValue);
                return true;
            }
        }
        this.addError("La variable \"" + name + "\" no ha sido declarada", line, column);
        return false;
    }

    private void addError(String description, int line, int column) {
        this.semanticErrors.add(new ErrorReport(
                description, line, column, ErrorType.SEMANTICO
        ));
    }

    public boolean hasErrors() {
        return !this.semanticErrors.isEmpty();
    }

    public List<Symbol> getAllSymbols() {
        List<Symbol> all = new ArrayList<>();
        for (LinkedHashMap<String, Symbol> scope : this.scopes) {
            all.addAll(scope.values());
        }
        return all;
    }

    public void reset() {
        this.scopes.clear();
        this.functions.clear();
        this.semanticErrors.clear();
        this.currentLevel = -1;
        this.isBlockFor = false;
    }

}
