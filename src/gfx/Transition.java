package gfx;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Set;

import main.Game;
public class Transition{
    
    public boolean running = false;
    public boolean finished = false;
    private double now, lastTime, elapsedTime;
    private int duration;
    private Color c;
    private boolean done = false;
    private double angle = 0;
    public boolean canContinue = true;
    private BufferedImage logo;
    private double n,l,e;

    public Transition(int duration){
        this.duration = duration;
    }
    public void task(){}
    public void end(){}
    public void start(){
        //Restart
        elapsedTime = 0;
        finished = false;
        lastTime = System.currentTimeMillis();
        c = new Color(0,0,0,0);
        running = true;
        //Logo rotation timer
        e = 0;
        l = System.currentTimeMillis();

//Get random logo
        String[] keys = {"brownMushroom","redMushroom","sheepPolypore","chanterelle","russulaPaludosa"};
        Random random = new Random();
        int r = random.nextInt(keys.length);
        logo = AssetStorage.images.get(keys[r]);
        
    }
    public void update(){
        if(running){
            now = System.currentTimeMillis();
            if(canContinue){
                elapsedTime += now-lastTime;
            }
            if(!done){
                if(elapsedTime >= (duration/2)){
                    canContinue = false;
                    task();
                    now = System.currentTimeMillis();
                    done = true;
                }
            }
            if(elapsedTime >= duration){
                end();
                finished = true;
                running = false;
            }
            double percent = (elapsedTime/duration);
            int mapped = mapToUpDownRange(percent);
            if(mapped < 0){
                mapped = 0;
            }
            if(mapped > 255){
                mapped = 255;
            }
            c = new Color(0,0,0,mapped);
            
            lastTime = now;
        }
    }

    private static int mapToUpDownRange(double normalizedValue) {
        double angle = normalizedValue * Math.PI;
        double sineValue = Math.sin(angle);
        int mappedValue = (int) (sineValue * 255);

        return mappedValue;
    }


    public void render(Graphics g){
        if(running){
            n = System.currentTimeMillis();
            e += n-l;
            if(e>=50){
                angle+= 10;
                e-=50;
            }
            BufferedImage lo = Factory.rotateImage(logo, angle);
            int scale = 4;
            int nw = lo.getWidth()*scale;
            int nh = lo.getHeight()*scale;

            g.setColor(c);
            g.fillRect(0, 0, Game.w.getWidth(), Game.w.getHeight());
            g.setColor(Color.white);
            g.drawString("loading", 100,100);
            g.drawImage(lo, Game.w.getWidth()/2 - nw/2, Game.w.getHeight()/2 - nh/2, nw,nh, null);
            l = n;
        }
        
    }
}
