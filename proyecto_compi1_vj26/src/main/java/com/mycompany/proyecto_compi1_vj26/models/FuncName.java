/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.models;

/**
 *
 * @author david
 */
public enum FuncName {

    RETURN("return"),
    FMT_PRINTLN("fmt.Println"),
    STRCONV_ATOI("strconv.Atoi"),
    STRCONV_PARSE_FLOAT("strconv.ParseFloat"),
    REFLECT_TYPE_OF("reflect.TypeOf"),
    APPEND("append"),
    LEN("len"),
    SLICES_INDEX("slices.Index"),
    STRINGS_JOIN("strings.Join");

    private String name;

    private FuncName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
