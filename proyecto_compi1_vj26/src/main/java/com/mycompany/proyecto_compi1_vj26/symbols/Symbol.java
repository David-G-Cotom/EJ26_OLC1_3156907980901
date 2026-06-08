/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.symbols;

import com.mycompany.proyecto_compi1_vj26.models.VarType;
import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value.ValueWrapper;

/**
 *
 * @author david
 */
public class Symbol {

    private final String name;
    private VarType type;
    private ValueWrapper value;
    private final int scope;
    private final int line;
    private final int column;

    public Symbol(String name, VarType type, ValueWrapper value, int scope, int line, int column) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.scope = scope;
        this.line = line;
        this.column = column;
    }

    public String getName() {
        return name;
    }

    public VarType getType() {
        return type;
    }

    public void setType(VarType type) {
        this.type = type;
    }

    public ValueWrapper getValue() {
        return value;
    }

    public void setValue(ValueWrapper value) {
        this.value = value;
    }

    public int getScope() {
        return scope;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Symbol{" + "name=" + name + ", type=" + type + ", value=" + value + ", scope=" + scope + ", line=" + line + ", column=" + column + '}';
    }

}
