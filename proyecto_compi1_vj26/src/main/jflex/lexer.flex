//IMPORTS
package com.mycompany.proyecto_compi1_vj26;

import java_cup.runtime.*;

import java.util.ArrayList;

import com.mycompany.proyecto_compi1_vj26.models.ErrorReport;
import com.mycompany.proyecto_compi1_vj26.models.ErrorType;
import com.mycompany.proyecto_compi1_vj26.models.Token;

%% // ---------------------------------------- SECTION SEPARATOR ----------------------------------------

// USER CODE
%{
    // Utils
    private StringBuilder string;

    private ArrayList<Token> tokenList;
    private int tokenCounter = 0;

    private ArrayList<ErrorReport> lexicalErrors;
    private int errorCounter = 0;

    private boolean insertSemicolon = false;

    private void error(String token) {
        this.errorCounter++;
        this.lexicalErrors.add(new ErrorReport(
            this.errorCounter,
            "El simbolo '" + token + "' no es aceptado en el lenguaje",
            yyline, yycolumn,
            ErrorType.LEXICO
        ));
    }

    public ArrayList<ErrorReport> getLexicalErrors(){
        return this.lexicalErrors;
    }

    public ArrayList<Token> getTokenList(){
        return this.tokenList;
    }

    private void updateASI(int type) {
        switch (type) {
            case sym.ID:
            case sym.NUMERO_ENTERO:
            case sym.NUMERO_DECIMAL:
            case sym.TEXTO:
            case sym.CHARACTER:
            case sym.TRUE:
            case sym.FALSE:
            case sym.RETURN:
            case sym.BREAK:
            case sym.CONTINUE:
            case sym.INCREMENTO:
            case sym.DECREMENTO:
            case sym.PARENTESIS_CERRADO:
            case sym.CORCHETE_CERRADO:
            case sym.LLAVE_CERRADO:
                this.insertSemicolon = true;
                break;
            default:
                this.insertSemicolon = false;
        }
    }

    //Parser Code
    private Symbol symbol(int type) {
        this.updateASI(type);
        this.tokenCounter++;
        this.tokenList.add(new Token(
            this.tokenCounter,
            yytext(),
            sym.terminalNames[type],
            yyline, yycolumn
        ));
        return new Symbol(type, yyline, yycolumn);
    }

    private Symbol symbol(int type, Object value) {
        this.updateASI(type);
        this.tokenCounter++;
        this.tokenList.add(new Token(
            this.tokenCounter,
            value.toString(),
            sym.terminalNames[type],
            yyline, yycolumn
        ));
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
    this.tokenList = new ArrayList<>();
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
// Rune: carácter simple o secuencia de escape entre comillas simples
RuneLiteral = \'([^\r\n\'\\]|\\[nrt\"\\]|\\u[0-9A-Fa-f]{4})\'

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
    "main" { return symbol(sym.MAIN); }
    "true" { return symbol(sym.TRUE); }
    "false" { return symbol(sym.FALSE); }
    "indice" { return symbol(sym.INDICE); }
    "valor" { return symbol(sym.VALOR); }

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
    "strconv.ParseFloat" { return symbol(sym.STRCONV_PARSE_FLOAT); }
    "reflect.TypeOf" { return symbol(sym.REFLECT_TYPE_OF); }

    "//" { this.string.setLength(0); yybegin(SIMPLE_COMMENT); }
    "/*" { this.string.setLength(0); yybegin(MULTIPLE_COMMENT); }

    \" { this.string.setLength(0); yybegin(TEXT); }

    {DecimalNumber} { return symbol(sym.NUMERO_DECIMAL, Double.parseDouble(yytext())); }
    {WholeNumber}   { return symbol(sym.NUMERO_ENTERO, Integer.parseInt(yytext())); }
    {ID}            { return symbol(sym.ID, yytext()); }
    {Dot}           { return symbol(sym.PUNTO); }
    {RuneLiteral} {
        String raw = yytext();
        char ch;
        if (raw.length() == 3) {
            ch = raw.charAt(1); // Carácter simple: 'A'
        } else {
            // Secuencia de escape: '\n', '\t', etc.
            ch = switch (raw.charAt(2)) {
                case 'n'  -> '\n';
                case 'r'  -> '\r';
                case 't'  -> '\t';
                case '\\' -> '\\';
                case '\'' -> '\'';
                case '"'  -> '"';
                default   -> raw.charAt(2);
            };
        }
        return symbol(sym.CHARACTER, ch);
    }
}

// Comments states
<SIMPLE_COMMENT> {
    {InputCharacter}+ { this.string.append(yytext()); }
    {LineTerminator} {
        yybegin(YYINITIAL);
        if (this.insertSemicolon) {
            this.insertSemicolon = false;
            return new Symbol(sym.PUNTO_COMA, yyline, yycolumn);
        }
    }
    <<EOF>> {
        yybegin(YYINITIAL);
        if (this.insertSemicolon) {
            this.insertSemicolon = false;
            return new Symbol(sym.PUNTO_COMA, yyline, yycolumn);
        }
        return symbol(sym.EOF);
    }
}
<MULTIPLE_COMMENT> {
    "*/" {
        System.out.println("COMENTARIO MULTILINEA: " + this.string.toString().trim());
        yybegin(YYINITIAL);
    }
    {InputCharacter} { this.string.append(yytext()); }
    {LineTerminator} { /* Ignore */ }
    <<EOF>> {
        this.errorCounter++;
        this.lexicalErrors.add(new ErrorReport(
            this.errorCounter,
            "Comentario multilinea no cerrado (falta */)",
            yyline, yycolumn,
            ErrorType.LEXICO
        ));
        return symbol(sym.EOF);
    }
}

// Text state
<TEXT> {
    "\\\""  { this.string.append('"'); }
    "\\\\"  { this.string.append('\\'); }
    "\\n"  { this.string.append('\n'); }
    "\\r"  { this.string.append('\r'); }
    "\\t"  { this.string.append('\t'); }
    \" {
        System.out.println("TEXTO: " + this.string.toString().trim());
        yybegin(YYINITIAL);
        return symbol(sym.TEXTO, this.string.toString().trim());
    }
    {InputCharacter} { this.string.append(yytext()); }
    {LineTerminator} { this.string.append("\n"); }
}

/* Ignored whitespace */
{WhiteSpace} { /* Ignore */ }

{LineTerminator} { 
    if (this.insertSemicolon) {
        this.insertSemicolon = false;
        return new Symbol(sym.PUNTO_COMA, yyline, yycolumn);
    }
}

/* Error handling */
. { error(yytext()); }
<<EOF>> {
    if (this.insertSemicolon) {
        this.insertSemicolon = false;
        return new Symbol(sym.PUNTO_COMA, yyline, yycolumn);
    }
    return symbol(sym.EOF);
}
