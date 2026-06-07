/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.models;

/**
 *
 * @author david
 */
public class ErrorReport {

    private final int number;
    private final String description;
    private final int line;
    private final int column;
    private final ErrorType type;

    public ErrorReport(int number, String description, int line, int column, ErrorType type) {
        this.number = number;
        this.description = description;
        this.line = line;
        this.column = column;
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public String getDescription() {
        return description;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public ErrorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "ErrorReport{" + "description=" + description + ", line=" + line + ", column=" + column + ", type=" + type + '}';
    }

}
