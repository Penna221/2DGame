package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import sound.SoundPlayer;
public class Text extends UIElement{
    public BufferedImage textImage;
    private String text;
    private int maxWidth;
    private boolean border;
    public boolean fillBg = false;
    public Color overrideColor;
    public boolean done = false;
    public static boolean typing = false;
    public Text(String text, int x, int y, int maxWidth, boolean border, boolean easeIn) {
        super(x, y);
        this.border = border;
        this.maxWidth = maxWidth;
        if(maxWidth==0){
            this.maxWidth = 400;
        }
        this.text = text;

        textImage = UIFactory.generateText(text, this.maxWidth);
        if(border){
            textImage = UIFactory.generateBorder(textImage,UIFactory.textData.borderThickness);
        }
        bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
        if(easeIn){
            easeIn(text);
        }
    }

    public void easeIn(String tt){
        Random r = new Random();
        String[] options = {"type_1","type_2","type_3","type_4"};
        Thread t = new Thread(){
            public void run(){
                typing = true;
                char[] chars = tt.toCharArray();
                int len = 0;
                while(len <chars.length&&typing){
                    try {
                        len++;
                        String newText = "";
                        for(int i = 0;i < len; i++){
                            newText += chars[i];
                        }
                        String sound = options[r.nextInt(options.length)];
                        SoundPlayer.playSound(sound,true,false);
                        updateText(newText);
                        Thread.sleep(100 + r.nextInt(3)*100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                done = true;
            }
        };
        t.start();
    }


    public void updateText(String text){
        this.text = text;
        textImage = UIFactory.generateText(text,maxWidth);
        if(border){
            textImage = UIFactory.generateBorder(textImage,UIFactory.textData.borderThickness);
        }
        // bounds = new Rectangle(x,y,textImage.getWidth(),textImage.getHeight());
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
