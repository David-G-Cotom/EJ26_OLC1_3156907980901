# Gramática (BNF) del Lenguaje GoLite

## Símbolo Inicial

```bnf
<inicio>
```

---

## Estructura General del Programa

```bnf
<inicio> ::= <funcion_main> <fin_llave>
           | error <fin_llave>

<funcion_main> ::= FUNC MAIN PARENTESIS_ABIERTO PARENTESIS_CERRADO <bloque_codigo>
                 | FUNC MAIN error <bloque_codigo>

<bloque_codigo> ::= LLAVE_ABIERTO <contenido> LLAVE_CERRADO
                  | LLAVE_ABIERTO LLAVE_CERRADO
                  | LLAVE_ABIERTO error LLAVE_CERRADO

<contenido> ::= <contenido> <instruccion>
              | <instruccion>

<fin_llave> ::= PUNTO_COMA
              | ε
```

---

## Instrucciones

```bnf
<instruccion> ::= <variable> PUNTO_COMA
                | <asignacion> PUNTO_COMA
                | <funcion_embebida> PUNTO_COMA
                | <bloque_codigo> <fin_llave>
                | <sentencia_if> <fin_llave>
                | <sentencia_switch>
                | <sentencia_for> <fin_llave>
                | <inc_dec> PUNTO_COMA
                | <transferencia>
                | <struct>
                | error PUNTO_COMA
```

---

## Variables y Asignaciones

```bnf
<variable> ::= VAR ID <tipo_dato> <definicion>
             | ID IGUAL_IMPLICITO <expr>

<tipo_dato> ::= INT
              | FLOAT
              | STRING
              | BOOL
              | RUNE

<definicion> ::= IGUAL <expr>
               | ε

<asignacion> ::= ID SUMA_IMPLICITA <expr>
               | ID RESTA_IMPLICITA <expr>
               | ID IGUAL <expr>

<inc_dec> ::= ID INCREMENTO
            | ID DECREMENTO
```

---

## Sentencias Condicionales

```bnf
<sentencia_if> ::= IF <expr> <bloque_codigo> <fin_if>
                 | IF error <bloque_codigo>

<fin_if> ::= ELSE <continuar_else>
           | ELSE error <bloque_codigo>
           | ε

<continuar_else> ::= <sentencia_if>
                   | <bloque_codigo>
```

---

## Sentencia Switch

```bnf
<sentencia_switch> ::= SWITCH <expr> LLAVE_ABIERTO <bloque_switch> LLAVE_CERRADO
                     | SWITCH error LLAVE_ABIERTO <bloque_switch> LLAVE_CERRADO

<bloque_switch> ::= <bloque_switch> <caso_switch>
                  | <caso_switch>
                  | DEFAULT DOS_PUNTOS <contenido>

<caso_switch> ::= CASE <expr> DOS_PUNTOS <contenido>
                | CASE error DOS_PUNTOS <contenido>
```

---

## Ciclos For

```bnf
<sentencia_for> ::= FOR <condicion_for> <bloque_codigo>
                  | FOR error <bloque_codigo>

<condicion_for> ::= <expr>
                  | <variable> PUNTO_COMA <expr> PUNTO_COMA <inc_dec>
                  | INDICE COMA VALOR IGUAL_IMPLICITO RANGE ID
```

---

## Transferencia de Control

```bnf
<transferencia> ::= BREAK PUNTO_COMA
                  | CONTINUE PUNTO_COMA
                  | RETURN PUNTO_COMA
                  | RETURN <expr> PUNTO_COMA
                  | RETURN error PUNTO_COMA
                  | BREAK error PUNTO_COMA
                  | CONTINUE error PUNTO_COMA
```

---

## Funciones de Slices y Strings

```bnf
<funcion_slice> ::= <slices_index>
                  | <strings_join>
                  | <len>
                  | <append>

<slices_index> ::= SLICES_INDEX PARENTESIS_ABIERTO ID COMA <expr_primary> PARENTESIS_CERRADO

<strings_join> ::= STRINGS_JOIN PARENTESIS_ABIERTO ID COMA <expr_primary> PARENTESIS_CERRADO

<len> ::= LEN PARENTESIS_ABIERTO ID PARENTESIS_CERRADO

<append> ::= APPEND PARENTESIS_ABIERTO ID COMA <expr_primary> PARENTESIS_CERRADO
```

---

## Funciones Embebidas

```bnf
<funcion_embebida> ::= <fmt_println>
                     | <strconv_atoi>
                     | <strconv_parse_float>
                     | <reflect_type_of>

<fmt_println> ::= FMT_PRINTLN PARENTESIS_ABIERTO <args_println> PARENTESIS_CERRADO
                | FMT_PRINTLN PARENTESIS_ABIERTO PARENTESIS_CERRADO
                | FMT_PRINTLN PARENTESIS_ABIERTO error PARENTESIS_CERRADO

<args_println> ::= <args_println> COMA <expr>
                 | <expr>

<strconv_atoi> ::= STRCONV_ATOI PARENTESIS_ABIERTO <expr> PARENTESIS_CERRADO
                 | STRCONV_ATOI PARENTESIS_ABIERTO error PARENTESIS_CERRADO

<strconv_parse_float> ::= STRCONV_PARSE_FLOAT PARENTESIS_ABIERTO <expr> PARENTESIS_CERRADO
                        | STRCONV_PARSE_FLOAT PARENTESIS_ABIERTO error PARENTESIS_CERRADO

<reflect_type_of> ::= REFLECT_TYPE_OF PARENTESIS_ABIERTO <expr> PARENTESIS_CERRADO
                    | REFLECT_TYPE_OF PARENTESIS_ABIERTO <expr> PARENTESIS_CERRADO PUNTO STRING
                    | REFLECT_TYPE_OF PARENTESIS_ABIERTO error PARENTESIS_CERRADO
```

---

## Expresiones

```bnf
<expr> ::= <expr> OR <expr>
         | <expr_and>

<expr_and> ::= <expr_and> AND <expr_and>
             | <expr_not>

<expr_not> ::= NOT <expr_not>
             | <expr_rel>

<expr_rel> ::= <expr_rel> IGUALDAD <expr_rel>
             | <expr_rel> NO_IGUAL <expr_rel>
             | <expr_rel> MENOR <expr_rel>
             | <expr_rel> MENOR_IGUAL <expr_rel>
             | <expr_rel> MAYOR <expr_rel>
             | <expr_rel> MAYOR_IGUAL <expr_rel>
             | <expr_add>

<expr_add> ::= <expr_add> SUMA <expr_add>
             | <expr_add> RESTA <expr_add>
             | <expr_mul>

<expr_mul> ::= <expr_mul> MULTIPLICACION <expr_mul>
             | <expr_mul> DIVISION <expr_mul>
             | <expr_mod>

<expr_mod> ::= <expr_mod> MODULO <expr_mod>
             | <expr_unary>

<expr_unary> ::= RESTA <expr_unary>
               | <expr_primary>

<expr_primary> ::= PARENTESIS_ABIERTO <expr> PARENTESIS_CERRADO
                 | NUMERO_DECIMAL
                 | NUMERO_ENTERO
                 | ID
                 | TEXTO
                 | CHARACTER
                 | TRUE
                 | FALSE
                 | NIL
                 | <funcion_slice>
                 | <funcion_embebida>
                 | PARENTESIS_ABIERTO error PARENTESIS_CERRADO
```

---

## Notas

- ε representa una producción vacía.
- Los nombres en formato (`NOMBRE_TERMINAL`) son los terminales/tokens de la gramatica.
- Los nombres en formato (`<nombre_no_terminal>`) son los no terminales declarados en la gramatica.
- Los símbolos (`error`) son los no terminales de la gramatica para la recuperacion de errores.
