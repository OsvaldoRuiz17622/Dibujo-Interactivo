package dibujoInteractivo;

import java.awt.Color;

public class PintarFiguras {

  public static final int Rectangulo = 0;
  public static final int Ovalo = 1;
  public static final int Linea = 2;

  public static final Color[] COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA,
      Color.YELLOW, Color.BLACK,Color.ORANGE, Color.pink, Color.darkGray,Color.gray,};

  private int type;
  private boolean isFilled;
  private Color color;

  private int startX, startY;
  private int endX, endY;

  public PintarFiguras(int startX, int startY, int endX, int endY, int type, boolean isFilled, Color color) {

    this.type = type;
    this.isFilled = isFilled;
    this.color = color;
    this.startX = startX;
    this.startY = startY;
    this.endX = endX;
    this.endY = endY;

  }

  public int getType() {
    return this.type;
  }

  public boolean isFilled() {
    return this.isFilled;
  }

  public Color getColor() {
    return this.color;
  }

  public int startX() {
    return this.startX;
  }

  public int startY() {
    return this.startY;
  }

  public int endX() {
    return this.endX;
  }

  public int endY() {
    return this.endY;
  }

}