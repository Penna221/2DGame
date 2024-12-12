package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.EntityManager;
import main.Game;
import tiles.Tile;

public class Map {
    public int[][] map;
    //Binary map is used to find places where entities can be generated.
    public boolean[][] binaryMap;
    public int width, height;
    public String biomeName;
    public int startX, startY, endX, endY;
    public static HashMap<String,Structure> structures = new HashMap<String,Structure>();
    private ArrayList<Rectangle> structureBounds;
    private boolean updating = false;
    public Map(int type, int width, int height){
        this.width = width;
        this.height = height;
        generate(type);

    }
    public Map(){}
    public static void loadStructures() throws Exception{
        File[] list = new File("res/maps/structures").listFiles();
        ArrayList<File> files = new ArrayList<File>();
        for(File f : list){
            if(f.getName().endsWith("entities.csv")){
                continue;
            }else{
                files.add(f);
            }
        }
        for(File f : files){
            String fileName = f.getName();
            String name = fileName.substring(0,fileName.length()-4);
            // System.out.println(name);
            Structure s = new Structure(name);
            structures.put(name, s);
        }
    }
    public void loadMap(File f){
        // mapLoaded = false;
        String entityFile ="";
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = reader.readLine();
            String[] dimensions = line.split(" ");
            int w = Integer.parseInt(dimensions[0]);
            width = w;
            int h = Integer.parseInt(dimensions[1]);
            height = h;
            int[][] loadedMap = new int[w][h];
            int row = 0;
            entityFile = reader.readLine();
            while((line = reader.readLine())!=null){
                String[] ids = line.split(",");
                for(int i = 0; i < ids.length; i++){

                    int j = Integer.parseInt(ids[i]);
                    loadedMap[i][row] = j;
                }
                row++;
                
            }
            map = loadedMap;
            // mapLoaded = true;
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        generateBinaryMap();
        
    }
    public void generate(int type){
        structureBounds = new ArrayList<Rectangle>();
        switch (type) {
            case World.LVL1:
                generateLVL1();        
                break;
            case World.LVL2:
                generateLVL2();
                break;
            default:
                generateLVL1();
                break;
        }
        
        saveMapAsImage();
    }
    public void saveMapAsImage(){
        System.out.println("Trying to save map");
        BufferedImage i = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < binaryMap[0].length;y++){
            for(int x = 0; x < binaryMap.length;x++){
                
                if(binaryMap[x][y]){
                    i.setRGB(x, y, new Color(0,0,0,0).getRGB());
                }else{
                    i.setRGB(x, y, new Color(0,0,0,255).getRGB());
                }
                
            }
        }
        try {
            File f =new File("res/screenshots/map.png");
            f.getParentFile().mkdirs();
            System.out.println("saving " + f.getAbsolutePath());
            ImageIO.write(i, "png",f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void generateLVL1(){
        Biome b = Biome.biomes.get("dungeon_lvl1");
        biomeName = b.name;
        Tile[] nonSolidTiles = b.nonSolidTiles;
        Tile[] solidTiles = b.solidTiles;
        Tile[] borderTiles = b.borderTiles;
        
        map = new int[width][height];
        
        initMapToZero();
        generateWalls(5);
        cellularAutomata(48,6);
        setNonSolidTiles(nonSolidTiles);
        setSolidTiles(solidTiles,borderTiles);
        generateSpawnArea();

        String[] structureList =  b.structures;
        int size = structureList.length;
        System.out.println("structure list size " + size);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < 1; j++){
                generateStructureAtRandomSpot(structureList[i]);
            }
        }
        generateStructureAtRandomSpot("lvl2_tunnel");
        generateStructureAtRandomSpot("trader_room_v1");
        generateWalls(5);
        generateBinaryMap();
    }
    private void generateLVL2(){
        Biome b = Biome.biomes.get("dungeon_lvl2");
        biomeName = b.name;
        Tile[] nonSolidTiles = b.nonSolidTiles;
        Tile[] solidTiles = b.solidTiles;
        Tile[] borderTiles = b.borderTiles;
        map = new int[width][height];
        
        initMapToZero();
        generateWalls(5);
        cellularAutomata(20,5);
        setNonSolidTiles(nonSolidTiles);
        setSolidTiles(solidTiles,borderTiles);
        generateSpawnArea();
        String[] structureList =  b.structures;
        int size = structureList.length;
        System.out.println("structure list size " + size);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < 4; j++){
                generateStructureAtRandomSpot(structureList[i]);
            }
        }
        for(int j = 0; j < 100; j++){
            generateStructureAtRandomSpot("vertical_tunnel");
        }
        generateStructureAtRandomSpot("trader_room_v1");
        generateStructureAtRandomSpot("boss_door");
        
        generateWalls(5);
        generateBinaryMap();
    }
    private void generateBinaryMap(){
        binaryMap = new boolean[width][height];
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                boolean solid = Tile.getTileByID(map[x][y]).solid;
                if(solid){
                    binaryMap[x][y] = false;
                } else{
                    binaryMap[x][y] = true;
                    
                }
            }
        }
    }

    //init everything to 0
    private void initMapToZero(){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                map[x][y] = 0;
            }
        }
    }
    //Cellular Automata algorithm
    private void cellularAutomata(int percentage, int iterations){
        Random random = new Random();
        int WIDTH = map.length;
        int HEIGHT = map[0].length;
        // Randomly set initial walkable tiles
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(random.nextInt(100)>percentage){
                    map[i][j] = 1; // 0 or 1 (non-walkable or walkable)
                }else{
                    map[i][j] = 0;
                }
                
            }
        }

        // Apply cellular automata rules for each iteration
        for (int iteration = 0; iteration < iterations; iteration++) {
            int[][] newMap = new int[WIDTH][HEIGHT];

            // Apply rules to each tile
            for (int i = 1; i < WIDTH - 1; i++) {
                for (int j = 1; j < HEIGHT - 1; j++) {
                    int neighbors = countWalkableNeighbors(map, i, j);

                    // Apply rules
                    if (neighbors > 4) {
                        newMap[i][j] = 1; // Set as walkable
                    } else {
                        newMap[i][j] = 0; // Set as non-walkable
                    }
                }
            }

            // Update the map with the new values
            map = newMap;
        }
    }

    private static int countWalkableNeighbors(int[][] map, int x, int y) {
        int count = 0;

        // Check the eight neighbors
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                count += map[x + i][y + j];
            }
        }

        return count;
    }

    //change every 1 on map to -> something in nonSolidTiles
    private void setNonSolidTiles(Tile[] nonSolidTiles){
        Random r = new Random();
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]!=0){
                    int rr = r.nextInt(nonSolidTiles.length);
                    int id = nonSolidTiles[rr].id;
                    map[x][y] = id;
                }
            }
        }
    }
    //change every 0 on map to -> something solid.
    private void setSolidTiles(Tile[] solidTiles, Tile[] borderTiles){
        int w = map.length;
        int h = map[0].length;
        int[][] newMap = new int[w][h];
        
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                newMap[x][y] = map[x][y];
            }
        }
        Random r = new Random();
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]==0){
                    if(checkIfSurrounded(x, y)){
                        newMap[x][y] = solidTiles[0].id;
                    }else{
                        int rr = r.nextInt(borderTiles.length);
                        int id = borderTiles[rr].id;
                        newMap[x][y] = id;
                    }
                }
            }
        }
        map = newMap;
    }
    private boolean checkIfSurrounded(int x, int y){
        if(x < 2 || x >= map.length-1 || y < 2 ||y >= map[0].length-1){
            
            return true;
        }else{
            int count = 0;
            int west = map[x-1][y];
            int north = map[x][y-1];
            int east = map[x+1][y];
            int south = map[x][y+1];
            if(west == 0){
                count++;
            }
            if(north == 0){
                count++;
            }
            if(east == 0){
                count++;
            }
            if(south == 0){
                count++;
            }
            if(count==4){
                return true;
            }
        }
        return false;
    }
    private void generateSpawnArea(){
        int centerX = map.length/2;
        int centerY = map[0].length/2;
        Structure spawn = structures.get("spawn_area");
        int w = spawn.width;
        int h = spawn.height;
        generateStructure("spawn_area", centerX-w/2, centerY-h/2);
    }
    
    
    //Generate border
    private void generateWalls(int id){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(x <=2||x >= map.length-3 || y <= 2|| y >= map[0].length-3){
                    map[x][y] = id;
                }

            }
        }
    }
    public void populateWithEnemies(int type){
        Biome biome = Biome.biomes.get("dungeon_lvl1");
        switch (type) {
            case World.LVL1:
                biome = Biome.biomes.get("dungeon_lvl1");       
                break;
            case World.LVL2:
                biome = Biome.biomes.get("dungeon_lvl2");       
                break;
            default:
                biome = Biome.biomes.get("dungeon_lvl1");
                break;
        }
        int[] entities = biome.entities;
        int[] collectables = biome.collectables;
        for(int y = 0; y < binaryMap[0].length; y++){
            for(int x = 0; x < binaryMap.length; x++){
                if(binaryMap[x][y]){
                    //IF cannot spawn entity, try to spawn collectable.
                    if(!generateRandomEntity(entities, x*Tile.tileSize, y*Tile.tileSize)){
                        generateRandomEntity(collectables, x*Tile.tileSize, y*Tile.tileSize);
                    }
                }
            }
        }
    }
    
    public boolean generateRandomEntity(int[] entities, int x, int y){
        Random r = new Random();
        int d = r.nextInt(200);
        //change to spawn enemy.
        if(d ==1){
            int d2 = r.nextInt(entities.length);
            World.entityManager.generateWithID(entities[d2],-1, x, y);
            return true;
        }
        return false;
    }

    private void generateStructureAtRandomSpot(String name){
        boolean b = false;
        int rx = 0;
        int ry = 0;
        Random r = new Random();
        int retrys = 4;
        int counter = 0;
        while(!b){
            rx = 25 + r.nextInt(map.length-50);
            ry = 25 + r.nextInt(map[0].length-50);
            b = generateStructure(name,rx, ry);
            counter++;
            if(counter>=retrys){
                System.out.println("Failed to generate Strucure: " + name);
                break;
            }
        }
    }
    public boolean generateStructure(String name, int x, int y){
        Structure s = structures.get(name);
        if(s==null){
            return false;
        }
        int w = s.width;
        int h = s.height;
        int tiles[][] = s.tiles;
        if(x+w > map.length-2 || y+h >map[0].length-2){
            return false;
        }
        Rectangle r = new Rectangle(x,y,w,h);
        for(Rectangle r2 : structureBounds){
            if(r2.intersects(r)){
                return false;
            }
        }
        for(int j = 0; j < h; j++){
            for(int i = 0; i < w; i++){
                map[x+i][y+j] = tiles[i][j];
            }
        }

        structureBounds.add(r);
        for(StructureEntity e : s.entities){
            World.entityManager.generateWithID(e.id,-1, (e.x + x)*Tile.tileSize, (e.y+y)*Tile.tileSize);
        }
        return true;
    }
    //Can there be added some kind of rayCasting to set viewable area?
    public void updateVisible(Entity e){
        updating = true;
        int centerX = (int)(e.x/Tile.tileSize);
        int centerY = (int)(e.y/Tile.tileSize);
        // System.out.println("Updatevisible: " + centerX + " " + centerY);
        int screenWidth = Game.w.getWidth();
        int screenHeight = Game.w.getHeight();
        int wTiles = screenWidth/Tile.tileSize;
        int hTiles = screenHeight/Tile.tileSize;
        int buffer = 6;
        // System.out.println("UpdateVisible");
        // System.out.println("startX " +startX);
        // System.out.println("startY " +startY);
        startX = (centerX - wTiles/2)-buffer;
        startY = (centerY - hTiles/2)-buffer;

        if(startX <= 0){
            startX = 0;
        }
        if(startY <= 0){
            startY = 0;
        }
        endX = startX + wTiles + buffer*2;
        endY = startY + hTiles+ buffer*2;
        if(endX > map.length){
            endX = map.length;
        }
        if(endY > map[0].length){
            endY = map[0].length;
        }
        // System.out.println("endX " +endX);
        // System.out.println("endY " +endY);
        updating = false;
    }
    public void render(Graphics g){
        
        if(startX>map.length-1){
            startX = 0;
        }
        if(startY>map[0].length-1){
            startY = 0;
        }
        //avoid outOfBounds exception
        if(startX < 0){
            startX = 0;
        }
        if(endX >= map.length){
            endX = map.length;
        }
        if(startY < 0){
            startY = 0;
        }
        if(endY >= map[0].length){
            endY = map[0].length;
        }
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                if(updating){
                    break;
                }
                if(!World.ready){
                    break;
                }
                if(x > map.length){
                    break;
                }
                if(y > map[0].length){
                    break;
                }
                // System.out.println("In Forloop: " + x + " " + y + "  StartX and Y " + startX + " " + startY);
                
                g.drawImage(Tile.getTileByID(map[x][y]).texture, (int)((x*Tile.tileSize) - xOffset), (int)((y*Tile.tileSize) - yOffset),Tile.tileSize,Tile.tileSize, null);
            }
        }
    }
}
