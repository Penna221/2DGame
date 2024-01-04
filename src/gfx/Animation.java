package gfx;
import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[]images;
    private int[] times;
    
    private int index;
    private double now, lastTime, elapsedTime;
    private BufferedImage currentImage;    
    public Animation(BufferedImage[] images,int[] times){
        this.images = images;
        this.times = times;
        index = 0;
        currentImage = images[index];
        lastTime = System.currentTimeMillis();
    }
    public void restart(){
        index = 0;
        lastTime = System.currentTimeMillis();
    }
    public void animate(){
        now = System.currentTimeMillis();
        elapsedTime += now-lastTime;
        if(elapsedTime >= times[index]){
            elapsedTime-=times[index];
            index++;
            if(index >=images.length){
                index = 0;
            }
            currentImage = images[index];
        }
        lastTime = now;
    }
    public BufferedImage getFrame(){
        return currentImage;
    }
}
