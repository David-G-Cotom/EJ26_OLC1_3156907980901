# Gramática (BNF) del Lenguaje GoLite

## Símbolo Inicial

```bnf
<inicio>
```

---

## Estructura General del Programa

```bnf
<inicio> ::= <declaraciones_globales> <funcion_main> <declaraciones_globales>
           | error <fin_llave>

<declaraciones_globales> ::= <declaraciones_globales> <funcion_usuario> <fin_llave>
                           | <declaraciones_globales> <struct>
                           | ε

<fin_llave> ::= PUNTO_COMA
              | ε
```

---

## Funciones

### Funcion main

```bnf
<funcion_main> ::= FUNC MAIN PARENTESIS_ABIERTO PARENTESIS_CERRADO <bloque_codigo> <fin_llave>
                 | FUNC MAIN error <bloque_codigo> <fin_llave>
```

### Funciones de usuario

```bnf
<funcion_usuario> ::= FUNC ID PARENTESIS_ABIERTO <lista_params> PARENTESIS_CERRADO <tipo_dato> <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO <lista_params> PARENTESIS_CERRADO ID <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO <lista_params> PARENTESIS_CERRADO <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO PARENTESIS_CERRADO <tipo_dato> <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO PARENTESIS_CERRADO ID <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO PARENTESIS_CERRADO <bloque_codigo>
                    | FUNC error <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO error PARENTESIS_CERRADO <tipo_dato> <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO error PARENTESIS_CERRADO ID <bloque_codigo>
                    | FUNC ID PARENTESIS_ABIERTO error PARENTESIS_CERRADO <bloque_codigo>
```

### Lista de parametros

```bnf
<lista_params> ::= <lista_params> COMA <param>
                 | <param>

<param> ::= ID <tipo_dato>
          | ID ID       /* el segundo ID es el nombre de una estructura */
```

---

## Structs

```bnf
<struct> ::= STRUCT ID LLAVE_ABIERTO <lista_campos_struct> LLAVE_CERRADO <fin_llave>
           | STRUCT error LLAVE_CERRADO <fin_llave>

<lista_campos_struct> ::= <lista_campos_struct> <campo_struct>
                        | <campo_struct>

<campo_struct> ::= <tipo_dato> ID PUNTO_COMA
                 | ID ID PUNTO_COMA   /* tipo estructura */
                 | error PUNTO_COMA
```

---

## Bloques y Contenido

```bnf
<bloque_codigo> ::= LLAVE_ABIERTO <contenido> LLAVE_CERRADO
                  | LLAVE_ABIERTO LLAVE_CERRADO
                  | LLAVE_ABIERTO error LLAVE_CERRADO

<contenido> ::= <contenido> <instruccion>
              | <instruccion>
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

## Variables y Tipos

### Declaracion de Variables

```bnf
<variable> ::= VAR ID <tipo_dato> <definicion>
             | VAR ID ID <definicion>       /* tipo estructura */
             | ID ID <definicion>           /* tipo estructura (sin VAR) */
             | ID IGUAL_IMPLICITO <expr>

<definicion> ::= IGUAL <expr>
               | ε
```

### Tipos de Datos

```bnf
<tipo_dato> ::= INT
              | FLOAT
              | STRING
              | BOOL
              | RUNE
              | CORCHETE_ABIERTO CORCHETE_CERRADO INT
              | CORCHETE_ABIERTO CORCHETE_CERRADO FLOAT
              | CORCHETE_ABIERTO CORCHETE_CERRADO STRING
              | CORCHETE_ABIERTO CORCHETE_CERRADO BOOL
              | CORCHETE_ABIERTO CORCHETE_CERRADO RUNE
              | CORCHETE_ABIERTO CORCHETE_CERRADO CORCHETE_ABIERTO CORCHETE_CERRADO INT
              | CORCHETE_ABIERTO CORCHETE_CERRADO CORCHETE_ABIERTO CORCHETE_CERRADO FLOAT
              | CORCHETE_ABIERTO CORCHETE_CERRADO CORCHETE_ABIERTO CORCHETE_CERRADO STRING
              | CORCHETE_ABIERTO CORCHETE_CERRADO CORCHETE_ABIERTO CORCHETE_CERRADO BOOL
              | CORCHETE_ABIERTO CORCHETE_CERRADO CORCHETE_ABIERTO CORCHETE_CERRADO RUNE
```

---

## Asignaciones e Incremento/Decremento

```bnf
<asignacion> ::= <l_value> SUMA_IMPLICITA <expr>
               | <l_value> RESTA_IMPLICITA <expr>
               | <l_value> IGUAL <expr>

<inc_dec> ::= ID INCREMENTO
            | ID DECREMENTO
```

### L-Value (Lado Izquierdo)

```bnf
<l_value> ::= ID
            | <l_value> CORCHETE_ABIERTO <expr> CORCHETE_CERRADO
            | <l_value> PUNTO ID
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
<sentencia_switch> ::= SWITCH <expr> LLAVE_ABIERTO <bloque_switch> <default_switch> LLAVE_CERRADO
                     | SWITCH error LLAVE_ABIERTO <bloque_switch> <default_switch> LLAVE_CERRADO

<bloque_switch> ::= <bloque_switch> <caso_switch>
                  | <caso_switch>

<caso_switch> ::= CASE <expr> DOS_PUNTOS <contenido>
                | CASE error DOS_PUNTOS <contenido>

<default_switch> ::= DEFAULT DOS_PUNTOS <contenido>
                   | ε
```

---

## Ciclos For

```bnf
<sentencia_for> ::= FOR <condicion_for> <bloque_codigo>
                  | FOR error <bloque_codigo>
                  | FOR ID COMA ID IGUAL_IMPLICITO RANGE <expr> <bloque_codigo>
                  | FOR ID COMA ID IGUAL_IMPLICITO RANGE error <bloque_codigo>

<condicion_for> ::= <expr>
                  | <variable> PUNTO_COMA <expr> PUNTO_COMA <inc_dec>
                  | <variable> PUNTO_COMA <expr> PUNTO_COMA <asignacion>
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

## Llamada a Funciones (Usuario y Embebidas)

### Llamada a Funcion de Usuario

```bnf
<llamada_funcion> ::= ID PARENTESIS_ABIERTO <args_println> PARENTESIS_CERRADO
                    | ID PARENTESIS_ABIERTO PARENTESIS_CERRADO
```

### Funciones de Slices y Strings

```bnf
<funcion_slice> ::= <slices_index>
                  | <strings_join>
                  | <len>
                  | <append>

<slices_index> ::= SLICES_INDEX PARENTESIS_ABIERTO <expr> COMA <expr> PARENTESIS_CERRADO

<strings_join> ::= STRINGS_JOIN PARENTESIS_ABIERTO <expr> COMA <expr> PARENTESIS_CERRADO

<len> ::= LEN PARENTESIS_ABIERTO <expr> PARENTESIS_CERRADO

<append> ::= APPEND PARENTESIS_ABIERTO <expr> COMA <expr> PARENTESIS_CERRADO
```

### Funciones Embebidas

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
                 | TEXTO
                 | CHARACTER
                 | TRUE
                 | FALSE
                 | NIL
                 | <l_value>
                 | <llamada_funcion>
                 | <funcion_slice>
                 | <funcion_embebida>
                 | PARENTESIS_ABIERTO error PARENTESIS_CERRADO
                 | <tipo_dato> LLAVE_ABIERTO <lista_elementos_slice> <fin_llave> LLAVE_CERRADO
                 | LLAVE_ABIERTO <lista_valores_struct> <fin_llave> LLAVE_CERRADO
                 | LLAVE_ABIERTO error LLAVE_CERRADO
```

---

## Literales de Slice y Structs

### Elementos de un Slice

```bnf
<lista_elementos_slice> ::= <lista_elementos_slice> COMA <elemento_slice>
                          | <elemento_slice>

<elemento_slice> ::= <expr>
                   | <literal_slice_anidado>

<literal_slice_anidado> ::= LLAVE_ABIERTO <lista_elementos_slice> <fin_llave> LLAVE_CERRADO
                          | LLAVE_ABIERTO LLAVE_CERRADO
```

### Literales de Structs

```bnf
<lista_valores_struct> ::= <lista_valores_struct> COMA <valor_campo_struct>
                         | <valor_campo_struct>

<valor_campo_struct> ::= ID DOS_PUNTOS <expr>
```

---

## Notas

- ε representa una producción vacía.
- Los nombres en formato (`NOMBRE_TERMINAL`) son los terminales/tokens de la gramatica.
- Los nombres en formato (`<nombre_no_terminal>`) son los no terminales declarados en la gramatica.
- Los símbolos (`error`) son los no terminales de la gramatica para la recuperacion de errores.
