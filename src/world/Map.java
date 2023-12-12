package world;

import java.awt.Graphics;
import java.util.Random;

import tiles.Tile;

public class Map {
    public int[][] map;
    public int width, height;
    public String biomeName;
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
        
        map = new int[width][height];
        
        step1();
        step2(4);
        step3(nonSolidTiles);
        step4();
        lastStep(solidTiles);
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
    //Generate some patches of interesting stuff
    private void step4(){

    }
    //Generate border
    private void lastStep(Tile[] solidTiles){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(x <2||x > map.length-3 || y < 2|| y > map[0].length-3){
                    map[x][y] = solidTiles[0].id;
                }

            }
        }
    }




    public void render(Graphics g){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                g.drawImage(Tile.getTileByID(map[x][y]).texture, x*Tile.tileSize, y*Tile.tileSize,Tile.tileSize,Tile.tileSize, null);
            }
        }
    }
}
