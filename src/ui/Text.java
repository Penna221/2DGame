package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class Text extends UIElement{
    public BufferedImage textImage;
    private String text;
    private int maxWidth;
    private boolean border;
    public boolean fillBg = false;
    public Color overrideColor;
    public Text(String text, int x, int y, int maxWidth, boolean border) {
        super(x, y);
        this.border = border;
        this.maxWidth = maxWidth;
        if(maxWidth==0){
            maxWidth = 400;
        }
        this.text = text;
        textImage = UIFactory.generateText(text, maxWidth);
        if(border){
            textImage = UIFactory.generateBorder(textImage,UIFactory.textData.borderThickness);
        }
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
    }
    public void updateText(String text){
        this.text = text;
        textImage = UIFactory.generateText(text,maxWidth);
        if(border){
            textImage = UIFactory.generateBorder(textImage,UIFactory.textData.borderThickness);
        }
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
    }
    @Override
    public void render(Graphics g) {
        if(fillBg){
            g.setColor(UIFactory.textData.bgColor);
            if(overrideColor!=null){
                g.setColor(overrideColor);
            }
            // g.setColor(Color.black);
            g.fillRect(bounds.x,bounds.y,bounds.width,bounds.height);
        }
        g.drawImage(textImage, bounds.x, bounds.y, null);
        // g.setColor(UIFactory.textData.borderColor);
        // g.drawRect(bounds.x,bounds.y,bounds.width,bounds.height);
    }
    
}
