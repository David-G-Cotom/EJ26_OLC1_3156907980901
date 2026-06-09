/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.frontend;

import com.mycompany.proyecto_compi1_vj26.models.ErrorReport;
import com.mycompany.proyecto_compi1_vj26.models.Token;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Ventana reutilizable para mostrar reportes en una tabla. Soporta dos modos:
 * TOKENS : muestra la tabla de tokens (No, Lexema, Tipo, Línea, Columna) ERRORS
 * : muestra la tabla de errores (No, Descripción, Línea, Columna, Tipo)
 *
 * @author david
 */
public class ReportFrame extends javax.swing.JFrame {

    public enum Mode {
        TOKENS, ERRORS
    }

    public ReportFrame(Mode mode, List<?> data) {
        setTitle(mode == Mode.TOKENS ? "Reporte de Tokens" : "Reporte de Errores");
        setSize(750, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setBackground(Color.WHITE);

        // --- Encabezado ---
        JLabel header = new JLabel(
                mode == Mode.TOKENS ? "Tabla de Tokens" : "Tabla de Errores",
                SwingConstants.CENTER
        );
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        main.add(header, BorderLayout.NORTH);

        // --- Tabla ---
        String[] columns = this.columns(mode);
        Object[][] rows = this.buildRows(mode, data);

        DefaultTableModel model = new DefaultTableModel(rows, columns) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        table.setRowHeight(22);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(210, 230, 255));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(245, 245, 245));

        // Alinear columna "No." al centro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // Colorear filas de error por tipo
        if (mode == Mode.ERRORS) {
            table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object value, boolean selected, boolean focused, int row, int col) {
                    super.getTableCellRendererComponent(t, value, selected, focused, row, col);
                    if (!selected) {
                        String tipo = (String) t.getValueAt(row, 4);
                        setBackground(switch (tipo) {
                            case "Léxico" ->
                                new Color(255, 235, 230);
                            case "Sintáctico" ->
                                new Color(255, 250, 220);
                            case "Semántico" ->
                                new Color(230, 245, 255);
                            default ->
                                Color.WHITE;
                        });
                    }
                    return this;
                }
            });
        }

        // Ancho de columnas
        this.setColumnWidths(table, mode);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        main.add(scroll, BorderLayout.CENTER);

        // --- Barra inferior: contador + botón cerrar ---
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        JLabel count = new JLabel(
                "Total: " + data.size() + " " + (mode == Mode.TOKENS ? "token(s)" : "error(es)")
        );
        count.setFont(new Font("SansSerif", Font.PLAIN, 11));
        count.setForeground(new Color(100, 100, 100));

        JButton close = new JButton("Cerrar");
        close.addActionListener(e -> dispose());

        bottom.add(count, BorderLayout.WEST);
        bottom.add(close, BorderLayout.EAST);
        bottom.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // --- Utilidades ---
    private String[] columns(Mode mode) {
        return mode == Mode.TOKENS
                ? new String[]{"No.", "Lexema", "Tipo", "Línea", "Columna"}
                : new String[]{"No.", "Descripción", "Línea", "Columna", "Tipo"};
    }

    private Object[][] buildRows(Mode mode, List<?> data) {
        Object[][] rows = new Object[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            if (mode == Mode.TOKENS) {
                Token t = (Token) data.get(i);
                rows[i] = new Object[]{t.getNumber(), t.getLexeme(), t.getType(),
                    t.getLine(), t.getColumn()};
            } else {
                ErrorReport e = (ErrorReport) data.get(i);
                rows[i] = new Object[]{e.getNumber(), e.getDescription(), e.getLine(),
                    e.getColumn(), e.getType().getType()};
            }
        }
        return rows;
    }

    private void setColumnWidths(JTable table, Mode mode) {
        if (mode == Mode.TOKENS) {
            int[] widths = {45, 180, 180, 60, 70};
            for (int i = 0; i < widths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        } else {
            int[] widths = {45, 360, 60, 70, 90};
            for (int i = 0; i < widths.length; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
