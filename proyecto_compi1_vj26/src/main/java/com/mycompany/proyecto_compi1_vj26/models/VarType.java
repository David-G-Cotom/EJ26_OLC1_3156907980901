/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.models;

/**
 *
 * @author david
 */
public enum VarType {

    INT("int"),
    FLOAT("float64"),
    STRING("string"),
    BOOL("bool"),
    RUNE("rune"),
    NIL("nil"),
    // Slices de una dimension
    SLICE_INT("[]int"),
    SLICE_FLOAT("[]float64"),
    SLICE_STRING("[]string"),
    SLICE_BOOL("[]bool"),
    SLICE_RUNE("[]rune"),
    // Slices de dos dimensiones (matrices)
    SLICE_SLICE_INT("[][]int"),
    SLICE_SLICE_FLOAT("[][]float64"),
    SLICE_SLICE_STRING("[][]string"),
    SLICE_SLICE_BOOL("[][]bool"),
    SLICE_SLICE_RUNE("[][]rune");

    private final String type;

    private VarType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Dado un VarType de slice, devuelve el VarType del elemento. Ej: SLICE_INT
     * -> INT, SLICE_SLICE_INT -> SLICE_INT
     *
     * @return tipo de variable del elemento.
     */
    public VarType elementType() {
        return switch (this) {
            case SLICE_INT ->
                INT;
            case SLICE_FLOAT ->
                FLOAT;
            case SLICE_STRING ->
                STRING;
            case SLICE_BOOL ->
                BOOL;
            case SLICE_RUNE ->
                RUNE;
            case SLICE_SLICE_INT ->
                SLICE_INT;
            case SLICE_SLICE_FLOAT ->
                SLICE_FLOAT;
            case SLICE_SLICE_STRING ->
                SLICE_STRING;
            case SLICE_SLICE_BOOL ->
                SLICE_BOOL;
            case SLICE_SLICE_RUNE ->
                SLICE_RUNE;
            default ->
                throw new IllegalStateException("VarType " + this + " no es un slice");
        };
    }

    /**
     *
     * @return true si este VarType es un slice (de cualquier nivel).
     */
    public boolean isSlice() {
        return this.type.startsWith("[]");
    }

}
