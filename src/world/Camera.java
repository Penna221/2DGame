package world;

import entities.Entity;
import main.Game;

public class Camera {
    
    private double xOffset, yOffset;
    private static Entity centeredEntity;
    public Camera(){
        xOffset = 0;
        yOffset = 0;
    }
    public static void setEntityToCenter(Entity e){
        centeredEntity = e;
    }
    private void centerOnEntity(){
        xOffset = centeredEntity.x - Game.w.getWidth()/2 + centeredEntity.bounds.getWidth()/2;
        yOffset = centeredEntity.y - Game.w.getHeight()/2 + centeredEntity.bounds.getHeight()/2;

    }
    public void update(){
        if(centeredEntity!=null){
            centerOnEntity();
        }
    }
    public double getXOffset(){return xOffset;}
    public double getYOffset(){return yOffset;}
}
