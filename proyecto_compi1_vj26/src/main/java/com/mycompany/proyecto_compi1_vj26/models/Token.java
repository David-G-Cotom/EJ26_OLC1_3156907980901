/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.models;

/**
 *
 * @author david
 */
public class Token {
    
    private final String lexeme;
    private final String type;
    private final int line;
    private final int column;

    public Token(String lexeme, String type, int line, int column) {
        this.lexeme = lexeme;
        this.type = type;
        this.line = line;
        this.column = column;
    }

    public String getLexeme() {
        return lexeme;
    }

    public String getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Token{" + "lexeme=" + lexeme + ", type=" + type + ", line=" + line + ", column=" + column + '}';
    }
    
}
