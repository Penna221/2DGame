package world;

import java.awt.Rectangle;
import java.util.ArrayList;

import entities.Entity;
import entities.collision.CollisionBox;
import tiles.Tile;

public class Room {
    public int x, y;
    public Structure structure;
    
    public boolean north = false;
    public boolean south = false;
    public boolean west = false;
    public boolean east = false;
    public Rectangle bounds;
    //Connection points.
    public ArrayList<Entity> entities = new ArrayList<Entity>();
    public ArrayList<Entity> toAdd = new ArrayList<Entity>();
    public ArrayList<Entity> toRemove = new ArrayList<Entity>();

    public Room(int x, int y, Structure s){
        this.x = x;
        this.y = y;
        this.structure = s;
        if(s!=null){
            bounds = new Rectangle(x*Tile.tileSize,y*Tile.tileSize,s.width*Tile.tileSize,s.height*Tile.tileSize);
        }
        
    }
    public ArrayList<Integer> getConnections(){
        ArrayList<Integer> choises = new ArrayList<Integer>();
        if(!east && structure.getEastConnectionPoint()!=null){
            choises.add(0);
        }
        if(!west && structure.getWestConnectionPoint()!=null){
            choises.add(1);
        }
        if(!north && structure.getNorthConnectionPoint()!=null){
            choises.add(2);
        }
        if(!south && structure.getSouthConnectionPoint()!=null){
            choises.add(3);
        }
        return choises;
    }
    public void resetEntityClocks(){
        System.out.println("Resetting cloks");
        for(Entity e : entities){
            e.ai.lateInit();
        }
        for(Entity e :toAdd){
            e.ai.lateInit();
        }
    }
    public void updateCollisionBoxes(){
        for(Entity e : entities){
            boolean solid = true;
            if(e.info.solid){
                solid = true;
            }else{
                solid = false;
            }
            World.entityCollisionBoxes.add(new CollisionBox(e,e.bounds,solid));
        }
    }
    public void updateEntities(){
        entities.addAll(toAdd);
        toAdd.clear();
        //First create collision boxes. Then update the entities.
       
        for(Entity e : entities){
            e.update();
            e.setHomeRoom(this);
        }
        entities.removeAll(toRemove);

        toRemove.clear();
        
    }
    public void clearEntities(){
        toRemove.addAll(entities);
    }
    public void addEntity(Entity e){
        toAdd.add(e);
    }
    public void removeEntity(Entity e){
        toRemove.add(e);
    }
}
