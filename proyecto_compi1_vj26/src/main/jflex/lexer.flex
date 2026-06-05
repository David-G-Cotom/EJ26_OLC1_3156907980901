//IMPORTS
package com.mycompany.proyecto_compi1_vj26;

import java_cup.runtime.*;

import java.util.ArrayList;

%% // ---------------------------------------- SECTION SEPARATOR ----------------------------------------

// USER CODE
%{
    // Utils
    private StringBuilder string;

    // Error Handling
    private ArrayList<String> symbols;
    private ArrayList<String> lexicalErrors;

    private void error(String token) {
        this.lexicalErrors.add(token + " -> " + yyline + " -> " + yycolumn + " -> " + "Lexico" + " -> " + "Cadena no existente en el lenguaje");
    }

    public ArrayList<String> getLexicalErrors(){
        return this.lexicalErrors;
    }

    public ArrayList<String> getSymbols(){
        return this.symbols;
    }

    //Parser Code
    private Symbol symbol(int type) {
        this.symbols.add(yytext());
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        this.symbols.add(value.toString());
        return new Symbol(type, yyline, yycolumn, value);
    }
%}

// OPTIONS AND DECLARATIONS
%public
%class Lexer
%line
%column
%char
%unicode
%cup
%init{
    this.string = new StringBuilder();
    this.symbols = new ArrayList<>();
    this.lexicalErrors = new ArrayList<>();
    yyline = 1;
    yycolumn = 1;
%init}

// REGULAR EXPRESSIONS
LineTerminator = \r|\n|\r\n
WhiteSpace = [ \t\f]
InputCharacter = [^\r\n]
Dot = \.
WholeNumber = 0|[1-9][0-9]*
DecimalNumber = {WholeNumber}{Dot}[0-9]+
Letter = [a-zA-Z]
ID = _?{Letter}({Letter}|_|{WholeNumber})*

// STATES
%state TEXT, SIMPLE_COMMENT, MULTIPLE_COMMENT

%% // ---------------------------------------- SECTION SEPARATOR ----------------------------------------

// LEXICAL RULES
// Default State
<YYINITIAL> {
    "+" { return symbol(sym.SUMA); }
    "+=" { return symbol(sym.SUMA_IMPLICITA); }
    "-" { return symbol(sym.RESTA); }
    "-=" { return symbol(sym.RESTA_IMPLICITA); }
    "*" { return symbol(sym.MULTIPLICACION); }
    "/" { return symbol(sym.DIVISION); }
    "%" { return symbol(sym.MODULO); }

    "(" { return symbol(sym.PARENTESIS_ABIERTO); }
    ")" { return symbol(sym.PARENTESIS_CERRADO); }
    "[" { return symbol(sym.CORCHETE_ABIERTO); }
    "]" { return symbol(sym.CORCHETE_CERRADO); }
    "{" { return symbol(sym.LLAVE_ABIERTO); }
    "}" { return symbol(sym.LLAVE_CERRADO); }

    "==" { return symbol(sym.IGUALDAD); }
    "!=" { return symbol(sym.NO_IGUAL); }
    ">"  { return symbol(sym.MAYOR); }
    "<"  { return symbol(sym.MENOR); }
    ">=" { return symbol(sym.MAYOR_IGUAL); }
    "<=" { return symbol(sym.MENOR_IGUAL); }

    "&&" { return symbol(sym.AND); }
    "||" { return symbol(sym.OR); }
    "!"  { return symbol(sym.NOT); }

    "=" { return symbol(sym.IGUAL); }
    ":=" { return symbol(sym.IGUAL_IMPLICITO); }
    ":" { return symbol(sym.DOS_PUNTOS); }
    "," { return symbol(sym.COMA); }
    ";" { return symbol(sym.PUNTO_COMA); }
    "++" { return symbol(sym.INCREMENTO); }
    "--" { return symbol(sym.DECREMENTO); }

    "int" { return symbol(sym.INT); }
    "float64" { return symbol(sym.FLOAT); }
    "string" { return symbol(sym.STRING); }
    "bool" { return symbol(sym.BOOL); }
    "rune" { return symbol(sym.RUNE); }
    "struct" { return symbol(sym.STRUCT); }
    "nil" { return symbol(sym.NIL); }

    "func" { return symbol(sym.FUNC); }
    "var" { return symbol(sym.VAR); }
    "return" { return symbol(sym.RETURN); }

    "if" { return symbol(sym.IF); }
    "else" { return symbol(sym.ELSE); }
    "switch" { return symbol(sym.SWITCH); }
    "case" { return symbol(sym.CASE); }
    "default" { return symbol(sym.DEFAULT); }
    "for" { return symbol(sym.FOR); }
    "range" { return symbol(sym.RANGE); }
    "break" { return symbol(sym.BREAK); }
    "continue" { return symbol(sym.CONTINUE); }

    "slices.Index" { return symbol(sym.SLICES_INDEX); }
    "strings.Join" { return symbol(sym.STRINGS_JOIN); }
    "len" { return symbol(sym.LEN); }
    "append" { return symbol(sym.APPEND); }
    "fmt.Println" { return symbol(sym.FMT_PRINTLN); }
    "strconv.Atoi" { return symbol(sym.STRCONV_ATOI); }
    "strconv.ParserFloat" { return symbol(sym.STRCONV_PARSE_FLOAT); }
    "reflect.TypeOf" { return symbol(sym.REFLECT_TYPE_OF); }

    "//" { this.string.setLength(0); yybegin(SIMPLE_COMMENT); }
    "/*" { this.string.setLength(0); yybegin(MULTIPLE_COMMENT); }

    \" { this.string.setLength(0); yybegin(TEXT); }

    {WholeNumber}   { return symbol(sym.NUMERO_ENTERO, Integer.parseInt(yytext())); }
    {DecimalNumber} { return symbol(sym.NUMERO_DECIMAL, Double.parseDouble(yytext())); }
    {ID}            { return symbol(sym.ID, yytext()); }
    {Dot}           { return symbol(sym.PUNTO); }
    "'"{InputCharacter}"'"           { return symbol(sym.CHARACTER, yytext()); }
}

// Comments states
<SIMPLE_COMMENT> {
    {InputCharacter}+ { this.string.append(yytext()); }
    {LineTerminator} { yybegin(YYINITIAL); }
    <<EOF>> { yybegin(YYINITIAL); }
}
<MULTIPLE_COMMENT> {
    "*/" {
        System.out.println("COMENTARIO MULTILINEA: " + this.string.toString().trim());
        yybegin(YYINITIAL);
    }
    {InputCharacter} { this.string.append(yytext()); }
    {LineTerminator} { /* Ignore */ }
}

// Text state
<TEXT> {
    "\\\""  { this.string.append("\""); }
    "\\\\"  { this.string.append("\\"); }
    "\\n"  { this.string.append("\n"); }
    "\\r"  { this.string.append("\r"); }
    "\\t"  { this.string.append("\t"); }
    \" {
        System.out.println("TEXTO: " + this.string.toString().trim());
        yybegin(YYINITIAL);
        return symbol(sym.TEXTO, this.string.toString().trim());
    }
    {InputCharacter} { this.string.append(yytext()); }
    {LineTerminator} { this.string.append("\n"); }
}

/* Ignored whitespace */
{WhiteSpace} | {LineTerminator} { /* Ignore */ }

/* Error handling */
. { error(yytext()); }
<<EOF>> { return symbol(sym.EOF); }
