package ui;
import java.awt.Color;
public class UIData {
    public Color bgColor, fgColor;
    public Color borderColor;
    public int borderThickness;

    public UIData(Color bg, Color fg, Color borderColor, int borderThickness){
        this.bgColor = bg;
        this.fgColor = fg;
        this.borderColor = borderColor;
        this.borderThickness = borderThickness;
    }
}
