/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyecto_compi1_vj26;

import com.mycompany.proyecto_compi1_vj26.ast.ProgramNode;
import com.mycompany.proyecto_compi1_vj26.frontend.IDEGoLite;
import com.mycompany.proyecto_compi1_vj26.visitor.interpreter.InterpreterVisitor;
import java.io.StringReader;

/**
 *
 * @author david
 */
public class Proyecto_compi1_vj26 {

    public static void main(String[] args) {
        try {
            /*String texto = "func main(){"
                    + "i:=5;"
                    + "fmt.Println(i);"
                    + "}";
            Lexer s = new Lexer(new StringReader(texto));
            parser p = new parser(s);
            ProgramNode ast = (ProgramNode) p.parse().value;
            InterpreterVisitor interpreter = new InterpreterVisitor();
            interpreter.visit(ast);
            System.out.print(interpreter.getOutput());*/
            
            IDEGoLite ventana = new IDEGoLite();
            ventana.setVisible(true);
            ventana.setLocationRelativeTo(null);
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Hello World!");
    }
}
