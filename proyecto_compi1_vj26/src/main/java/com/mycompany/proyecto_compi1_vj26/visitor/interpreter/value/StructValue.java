/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value;

import java.util.LinkedHashMap;

/**
 *
 * @author david
 */
public non-sealed class StructValue implements ValueWrapper {

    private final String structName;
    private final LinkedHashMap<String, ValueWrapper> fields; // preserva orden de declaracion
    private final int line;
    private final int column;

    public StructValue(String structName, LinkedHashMap<String, ValueWrapper> fields, int line, int column) {
        this.structName = structName;
        this.fields = fields;
        this.line = line;
        this.column = column;
    }

    public String getStructName() {
        return structName;
    }

    public LinkedHashMap<String, ValueWrapper> getFields() {
        return fields;
    }

    public ValueWrapper getField(String name) {
        return fields.get(name);
    }

    public void setField(String name, ValueWrapper value) {
        fields.put(name, value);
    }

    public boolean hasField(String name) {
        return fields.containsKey(name);
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
        return this.structName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(structName).append("{");
        int i = 0;
        for (var entry : fields.entrySet()) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(": ").append(entry.getValue().toString());
            i++;
        }
        sb.append("}");
        return sb.toString();
    }

}
