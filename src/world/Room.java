package world;

import java.util.ArrayList;

public class Room {
    public int x, y;
    public Structure structure;
    
    public boolean north = false;
    public boolean south = false;
    public boolean west = false;
    public boolean east = false;

    //Connection points.


    public Room(int x, int y, Structure s){
        this.x = x;
        this.y = y;
        this.structure = s;
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
}
