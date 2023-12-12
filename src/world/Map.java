package world;

import java.awt.Graphics;
import java.util.Random;

import entities.Entity;
import main.Game;
import tiles.Tile;

public class Map {
    public int[][] map;
    //Binary map is used to find places where entities can be generated.
    public boolean[][] binaryMap;
    public int width, height;
    public String biomeName;
    public int startX, startY, endX, endY;
    public Map(int type, int width, int height){
        this.width = width;
        this.height = height;
        generate(type);

    }
    private void generate(int type){
        switch (type) {
            case World.FOREST:
                generateForest();        
                break;
        
            default:
                generateForest();
                break;
        }

    }
    private void generateForest(){
        Biome b = Biome.biomes.get("forest");
        biomeName = b.name;
        Tile[] nonSolidTiles = b.nonSolidTiles;
        Tile[] solidTiles = b.solidTiles;
        Tile[] borderTiles = b.borderTiles;
        
        map = new int[width][height];
        
        step1();
        step2(5);
        binaryMap = new boolean[width][height];
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]==0){
                    binaryMap[x][y] = false;
                } else{
                    binaryMap[x][y] = true;
                    
                }
            }
        }
        step3(nonSolidTiles);
        step4(solidTiles,borderTiles);
        step5();
        lastStep(5);
    }

    //init everything to 0
    private void step1(){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                map[x][y] = 0;
            }
        }
    }
    //Cellular Automata algorithm
    private void step2(int iterations){
        Random random = new Random();
        int WIDTH = map.length;
        int HEIGHT = map[0].length;
        // Randomly set initial walkable tiles
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                map[i][j] = random.nextInt(2); // 0 or 1 (non-walkable or walkable)
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
    private void step3(Tile[] nonSolidTiles){
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
    private void step4(Tile[] solidTiles, Tile[] borderTiles){
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

    //Generate some patches of interesting stuff
    private void step5(){

    }
    //Generate border
    private void lastStep(int id){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(x <2||x > map.length-3 || y < 2|| y > map[0].length-3){
                    map[x][y] = id;
                }

            }
        }
    }



    //Can there be added some kind of rayCasting to set viewable area?
    public void updateVisible(Entity e){
        int centerX = (int)(e.x/Tile.tileSize);
        int centerY = (int)(e.y/Tile.tileSize);
        int screenWidth = Game.w.getWidth();
        int screenHeight = Game.w.getHeight();
        int wTiles = screenWidth/Tile.tileSize;
        int hTiles = screenHeight/Tile.tileSize;
        startX = centerX - wTiles/2 - 1;
        startY = centerY - hTiles/2 - 1;
        endX = centerX + wTiles/2 +2;
        endY = centerY + hTiles/2 +2;
    }
    public void render(Graphics g){
        

        //avoid outOfBounds exception
        if(startX < 0){
            startX = 0;
        }
        if(endX > map.length){
            endX = map.length;
        }
        if(startY < 0){
            startY = 0;
        }
        if(endY > map[0].length){
            endY = map[0].length;
        }
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                
                g.drawImage(Tile.getTileByID(map[x][y]).texture, (int)((x*Tile.tileSize) - xOffset), (int)((y*Tile.tileSize) - yOffset),Tile.tileSize,Tile.tileSize, null);
            }
        }
    }
}
