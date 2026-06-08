/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.symbols;

import com.mycompany.proyecto_compi1_vj26.models.ErrorReport;
import com.mycompany.proyecto_compi1_vj26.models.ErrorType;
import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value.ValueWrapper;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *
 * @author david
 */
public class SymbolTable {

    private final List<LinkedHashMap<String, Symbol>> scopes;

    private int currentLevel;

    private final List<ErrorReport> semanticErrors;
    private int errorCounter;

    public SymbolTable() {
        this.scopes = new ArrayList<>();
        this.semanticErrors = new ArrayList<>();
        this.currentLevel = -1;
        this.errorCounter = 0;
    }

    public int getCurrentLevel() {
        return currentLevel;
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
            this.addError(
                    "La variable \"" + name + "\" ya fue declarada en este ámbito",
                    line, column
            );
            return false;
        }

        currentScope.put(name, new Symbol(name, type, value, this.currentLevel, line, column));
        return true;
    }

    private LinkedHashMap<String, Symbol> currentScope() {
        if (this.scopes.isEmpty()) {
            throw new IllegalStateException("No hay ningún scope abierto.");
        }
        return this.scopes.get(this.scopes.size() - 1);
    }

    private void addError(String description, int line, int column) {
        this.errorCounter++;
        this.semanticErrors.add(new ErrorReport(
                this.errorCounter, description, line, column, ErrorType.SEMANTICO
        ));
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
        this.semanticErrors.clear();
        this.currentLevel = -1;
        this.errorCounter = 0;
    }

}
