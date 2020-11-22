package dibujoInteractivo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Stack;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Vista extends JFrame {

  private final int UPDATE_RATE = 50;
  //SE DECLARAN LAS ELEMENTOS GRAFICOS
  private PanelCanvas canvas;
  private JPanel principalPanel;
  private JPanel Panel;
  private JLabel lblMouse;
  private JButton btnDeshacer, btnLimpiar;
  private JComboBox<String> comboColor, comboFigura;
  private JCheckBox checkRelleno;

//TAMANIO 
  private int width;
  private int height;

 
  private Stack<PintarFiguras> shapes;

  private int startX, startY;
  private int endX, endY;

  private boolean isDrawing = false;

  public Vista(int width, int height) {
    this.width = width;
    this.height = height;

    this.canvas = new PanelCanvas();
    this.principalPanel = new JPanel(new BorderLayout());
    this.Panel = new JPanel();
    this.lblMouse = new JLabel("0, 0");
    this.btnDeshacer= new JButton("Deshacer");
    this.btnLimpiar = new JButton("Limpiar panel");
    this.comboColor = new JComboBox<String>(new String[] { "Rojo", "Verde", "Azul", "Cyan", "Magenta", "Amarillo","Negro","Naranja","Rosa","Gris Oscuro","Gris"});
    
    this.comboFigura = new JComboBox<String>(new String[] { "Rectangulo", "Circulo/Ovalo", "Linea" });
    this.checkRelleno = new JCheckBox("Relleno");

    shapes = new Stack<PintarFiguras>();

    agregarAtributos();
    Escuchador();
    definir();

    this.pack();
    this.setLocationRelativeTo(null);

    startLoop();
  }

  public void agregarAtributos() {
    this.setTitle("Dibujo interactivo");
    this.setVisible(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setResizable(false);
  }

  public void Escuchador() {
    // Undo button.
    btnDeshacer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (shapes.size() > 0)
          shapes.pop();
      }
    });

    // Clear button.
    btnLimpiar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (shapes.size() > 0)
          shapes.clear();
      }
    });

    // Canvas mouse coordinates.
    canvas.addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();

        isDrawing = true;
      }

      public void mouseReleased(MouseEvent e) {
        shapes.push(new PintarFiguras(startX, startY, endX, endY, comboFigura.getSelectedIndex(),
            checkRelleno.isSelected(), PintarFiguras.COLORS[comboColor.getSelectedIndex()]));

        isDrawing = false;
      }
    });

    canvas.addMouseMotionListener(new MouseMotionAdapter() {
      public void mouseMoved(MouseEvent e) {
        lblMouse.setText(e.getX() + ", " + e.getY());
      }

      public void mouseDragged(MouseEvent e) {
        boolean lineSelected = comboFigura.getSelectedIndex() == PintarFiguras.Linea;

        endX = (lineSelected) ? e.getX() : e.getX() - startX;
        endY = (lineSelected) ? e.getY() : e.getY() - startY;

        lblMouse.setText(startX + ", " + startY + "; " + endX + ", " + endY);
      }
    });
  }

  public void definir() {
    this.Panel.add(btnDeshacer);
    this.Panel.add(btnLimpiar);
    this.Panel.add(comboColor);
    this.Panel.add(comboFigura);
    this.Panel.add(checkRelleno);

    this.principalPanel.add(Panel, BorderLayout.WEST);
    this.principalPanel.add(canvas, BorderLayout.NORTH);
    this.principalPanel.add(lblMouse, BorderLayout.EAST);

    this.add(principalPanel);
  }

  public void startLoop() {
    Thread drawLoop = new Thread() {
      public void run() {
        while (true) {
          repaint();

          try {
            Thread.sleep(1000 / 60); // Desired frame rate.
          } catch (InterruptedException ex) {
          }
        }
      }
    }
            ;

    drawLoop.start();
  }

  class PanelCanvas extends JPanel {

    protected void paintComponent(Graphics g) {
      super.paintComponent(g);

      g.setColor(Color.decode("#F4F7F5"));
      g.fillRect(0, 0, width, height);

      shapes.stream().forEach(shape -> {
        g.setColor(shape.getColor());

        switch (shape.getType()) {
          case PintarFiguras.Rectangulo:
            if (shape.isFilled())
              g.fillRect(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            else
              g.drawRect(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            break;
          case PintarFiguras.Ovalo:
            if (shape.isFilled())
              g.fillOval(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            else
              g.drawOval(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            break;
          case PintarFiguras.Linea:
            g.drawLine(shape.startX(), shape.startY(), shape.endX(), shape.endY());
            break;
        }

      });

      if (isDrawing) {
        g.setColor(Color.decode("#BBAADD"));
        switch (comboFigura.getSelectedIndex()) {
          case PintarFiguras.Rectangulo:
            g.drawRect(startX, startY, endX, endY);
            break;
          case PintarFiguras.Ovalo:
            g.drawOval(startX, startY, endX, endY);
            break;
          case PintarFiguras.Linea:
            g.drawLine(startX, startY, endX, endY);
            break;
        }
      }
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(width, height);
    }

  }

}