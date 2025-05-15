package gfx;
import java.awt.image.BufferedImage;

public class Animation {
    private String[]images;
    private int[] times;
    
    private int index;
    private double now, lastTime, elapsedTime;
    private String currentImage;    
    public Animation(String[] images,int[] times){
        this.images = images;
        this.times = times;
        index = 0;
        currentImage = images[index];
        lastTime = System.currentTimeMillis();
    }
    public void restart(){
        index = 0;
        
        lastTime = System.currentTimeMillis();
        elapsedTime = 0;
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
        return AssetStorage.images.get(currentImage);
    }
}
