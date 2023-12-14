package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
public class Text extends UIElement{
    private BufferedImage textImage;
    private String text;
    public Text(String text, int x, int y, int maxWidth) {
        super(x, y);
        textImage = UIFactory.generateText(text);
    }
    public void updateText(String text){
        this.text = text;
        textImage = UIFactory.generateText(text);
    }
    @Override
    public void render(Graphics g) {
        g.drawImage(textImage, x, y, null);
    }
    
}
