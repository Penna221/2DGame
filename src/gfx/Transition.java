package gfx;
import java.awt.Graphics;
import java.awt.Color;
import main.Game;
public class Transition{
    
    public boolean running = false;
    public boolean finished = false;
    private double now, lastTime, elapsedTime;
    private int duration;
    private Color c;
    private boolean done = false;
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
    }
    public void update(){
        if(running){
            now = System.currentTimeMillis();
            elapsedTime += now-lastTime;
            if(!done){
                if(elapsedTime >= (duration/2)){
                    task();
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
            g.setColor(c);
            g.fillRect(0, 0, Game.w.getWidth(), Game.w.getHeight());
        }
        
    }
}
