/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.proyecto_compi1_vj26.frontend;

import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGUniverse;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author david
 */
public class ASTReportFrame extends javax.swing.JFrame {

    private SVGDiagram svgDiagram;
    private double zoomFactor = 1.0;
    private final JLabel imageLabel;
    private final JScrollPane scrollPane;

    /**
     * Creates new form ASTReportFrame
     *
     * @param pngFile
     */
    public ASTReportFrame(File pngFile) {
        setTitle("Reporte de AST");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.setBackground(Color.WHITE);

        // --- Encabezado ---
        JLabel header = new JLabel("Árbol de Sintaxis Abstracta (AST)", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 15));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        main.add(header, BorderLayout.NORTH);

        try {
            SVGUniverse svgUniverse = new SVGUniverse();
            URI uri = svgUniverse.loadSVG(pngFile.toURI().toURL());
            this.svgDiagram = svgUniverse.getDiagram(uri);

            // Forzar que el SVG use un sistema de coordenadas CSS limpio si no tiene dimensiones internas
            if (this.svgDiagram != null) {
                this.svgDiagram.setIgnoringClipHeuristic(true);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo cargar la imagen del AST:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            this.svgDiagram = null;
        }

        this.imageLabel = new JLabel();
        this.imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.updateImageLabel();

        this.scrollPane = new JScrollPane(this.imageLabel);
        this.scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        this.scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        main.add(this.scrollPane, BorderLayout.CENTER);

        // --- Barra inferior: zoom + cerrar ---
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        JPanel zoomPanel = new JPanel();
        zoomPanel.setBackground(Color.WHITE);

        JButton zoomOut = new JButton("－");
        JButton zoomReset = new JButton("100%");
        JButton zoomIn = new JButton("＋");

        zoomOut.addActionListener(e -> this.applyZoom(this.zoomFactor / 1.2));
        zoomIn.addActionListener(e -> this.applyZoom(this.zoomFactor * 1.2));
        zoomReset.addActionListener(e -> this.applyZoom(1.0));

        zoomPanel.add(zoomOut);
        zoomPanel.add(zoomReset);
        zoomPanel.add(zoomIn);

        JButton close = new JButton("Cerrar");
        close.addActionListener(e -> dispose());

        bottom.add(zoomPanel, BorderLayout.WEST);
        bottom.add(close, BorderLayout.EAST);
        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
    }

    private void applyZoom(double newFactor) {
        // Limitar el rango de zoom para evitar tamaños absurdos
        this.zoomFactor = Math.max(0.1, Math.min(newFactor, 5.0));
        this.updateImageLabel();
    }

    private void updateImageLabel() {
        if (this.svgDiagram == null) {
            this.imageLabel.setText("No se pudo generar el gráfico del AST.");
            return;
        }
        // Obtener dimensiones originales directamente del lienzo del archivo SVG
        int originalWidth = (int) this.svgDiagram.getWidth();
        int originalHeight = (int) this.svgDiagram.getHeight();

        // Calcular el nuevo tamaño escalado según el factor de Zoom
        int w = (int) (originalWidth * this.zoomFactor);
        int h = (int) (originalHeight * this.zoomFactor);

        // Prevenir dimensiones en 0 si el zoom es críticamente bajo
        if (w <= 0) {
            w = 1;
        }
        if (h <= 0) {
            h = 1;
        }

        BufferedImage scaled = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = scaled.createGraphics();
        // Activar el suavizado de bordes (Antialiasing) para una visualización perfecta del vector
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Aplicar la escala de zoom en el contexto gráfico antes de dibujar el SVG
        g2.scale(this.zoomFactor, this.zoomFactor);

        try {
            // Renderizar el SVG vectorizado directamente en los gráficos de la imagen escalada
            this.svgDiagram.render(g2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            g2.dispose();
        }

        this.imageLabel.setIcon(new javax.swing.ImageIcon(scaled));
        this.imageLabel.setText(null);
        this.imageLabel.revalidate();
        this.imageLabel.repaint();
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
