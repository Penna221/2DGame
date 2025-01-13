package world;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Point;

public class Structure {
    public int width, height;
    public int[][] tiles;
    public ArrayList<StructureEntity> entities;
    private Point anchorNorth, anchorSouth, anchorWest, anchorEast;
    public String name;

    public Structure(String name){
        this.name = name;
        entities = new ArrayList<StructureEntity>();
        load(name);
    }
    public void load(String name){
        File f = new File("res/maps/structures/"+name+".csv");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String line = reader.readLine();
            String[] values = line.split(" ");
            width = Integer.parseInt(values[0]);
            height = Integer.parseInt(values[1]);
            String entityFile = reader.readLine();
            tiles = new int[width][height];
            int i = 0;
            while((line=reader.readLine())!=null){
                String[] vals = line.split(",");
                for(int j = 0; j < vals.length; j++){
                    tiles[j][i] = Integer.parseInt(vals[j]);
                }
                i++;
            }

            reader.close();

            BufferedReader reader2 = new BufferedReader(new FileReader(new File("res/maps/structures/"+entityFile)));
            String line2;
            while((line2 = reader2.readLine())!=null){
                String[] vals = line2.split(",");
                int id = Integer.parseInt(vals[0]);
                int x = Integer.parseInt(vals[1]);
                int y = Integer.parseInt(vals[2]);
                if(id==1){
                    //Anchor connection for room.
                    if(y==0){
                        //NORTH
                        anchorNorth = new Point(x,y);
                    }
                    if(y== height-1){
                        //SOUTH
                        anchorSouth = new Point(x,y);
                    }
                    if(x == 0){
                        //WEST
                        anchorWest = new Point(x,y);
                    }
                    if(x == width-1){
                        //EAST
                        anchorEast = new Point(x,y);
                    }
                    continue;
                }
                StructureEntity e = new StructureEntity(id, x, y);
                entities.add(e);
            }
            reader2.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    public Point getNorthConnectionPoint(){
        return anchorNorth;
    }
    public Point getSouthConnectionPoint(){
        return anchorSouth;
    }
    public Point getEastConnectionPoint(){
        return anchorEast;
    }
    public Point getWestConnectionPoint(){
        return anchorWest;
    }

}
