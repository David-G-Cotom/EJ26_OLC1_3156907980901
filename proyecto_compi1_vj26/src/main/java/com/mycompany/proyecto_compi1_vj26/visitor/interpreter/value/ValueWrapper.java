/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.visitor.interpreter.value;

/**
 *
 * @author david
 */
public sealed interface ValueWrapper permits IntValue, DoubleValue, StringValue, BoolValue, CharValue, NullValue, VoidValue {

    int line();

    int column();

    String getType();

}
