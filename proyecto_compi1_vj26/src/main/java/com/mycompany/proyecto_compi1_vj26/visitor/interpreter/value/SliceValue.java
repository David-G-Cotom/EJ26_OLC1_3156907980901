/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value;

import com.mycompany.proyecto_compi1_vj26.models.VarType;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author david
 */
public non-sealed class SliceValue implements ValueWrapper {

    private final List<ValueWrapper> elements;
    private final VarType sliceType;
    private final int line;
    private final int column;

    public SliceValue(List<ValueWrapper> elements, VarType sliceType, int line, int column) {
        this.elements = new ArrayList<>(elements);
        this.sliceType = sliceType;
        this.line = line;
        this.column = column;
    }

    public List<ValueWrapper> getElements() {
        return elements;
    }

    public VarType getSliceType() {
        return sliceType;
    }

    public int size() {
        return this.elements.size();
    }

    public ValueWrapper getElement(int index) {
        return this.elements.get(index);
    }

    public void setElement(int index, ValueWrapper value) {
        this.elements.set(index, value);
    }

    public void append(ValueWrapper value) {
        this.elements.add(value);
    }

    @Override
    public int line() {
        return this.line;
    }

    @Override
    public int column() {
        return this.column;
    }

    @Override
    public String getType() {
        return this.sliceType.getType();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < elements.size(); i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(elements.get(i).toString());
        }
        sb.append("]");
        return sb.toString();
    }

}
