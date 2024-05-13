package world;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import entities.Entity;
import entities.EntityManager;
import tiles.Tile;

public class World {
    //World contains everything. Entitites, map, player, mobs, everything. Even the Camera.
    
    //Can the map change in world? or are there multiple worlds? each with own map, with own player, with own entitites...
    //Different worlds. each with own data.

    public static final int FOREST = 0;
    public int type;

    public static Map map;
    public static Entity player;
    public static EntityManager entityManager;
    public static Camera camera;
    public World(int type){
        this.type = type;
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        generate();
    }
    public World(){
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        load();
    }
    public void load(){
        camera = new Camera();
        map = new Map();
        map.loadMap(new File("res/maps/map.csv"));
        loadEntities(new File("res/maps/map_entities.csv"));
    }
    public void loadEntities(File f){
        //Load Entities
        entityManager.clearEntities();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = "";
            while((line = reader.readLine())!=null){
                String[] tokens = line.split(",");
                int id = Integer.parseInt(tokens[0]);
                int x = Integer.parseInt(tokens[1])*Tile.tileSize;
                int y = Integer.parseInt(tokens[2])*Tile.tileSize;
                Entity e = entityManager.generateWithID(id, x, y);
                if(id==0){
                    player = e;
                    Camera.setEntityToCenter(e);
                }
                
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void generate(){
        camera = new Camera();
        generateMap(type);
        generateEntities(type);
        //generateMushrooms(type);
    }
    private void generateMap(int type){
        map = new Map(type,500,500);
    }
    private void generateEntities(int type){
        
        
        int mapCenterX = (map.map.length/2)*Tile.tileSize;
        int mapCenterY = (map.map[0].length/2)*Tile.tileSize;
        player = entityManager.generateWithID(0, mapCenterX, mapCenterY);
        entityManager.generateWithID(1, mapCenterX, mapCenterY);
        System.out.println(player);
        Camera.setEntityToCenter(player);

    }
    private void generateMushrooms(int type){
        String biome;
        switch (type) {
            case FOREST:
                biome = "forest";
                break;
        
            default:
                biome = "forest";
                break;
        }
        // MushroomData.loadMushrooms();
        // int[] availableMushrooms = Biome.biomes.get(biome).mushroomIds;
        // boolean[][] bm = map.binaryMap;
        // for(int y = 0; y < bm[0].length; y++){
        //     for(int x = 0; x < bm.length; x++){
                
        //         if(bm[x][y]){

        //             double posX = (x*Tile.tileSize) + Tile.tileSize/2;
        //             double posY = (y*Tile.tileSize)+ Tile.tileSize/2;

        //             Random r = new Random();
        //             int rr = r.nextInt(30);
        //             int i = 0;
        //             for(int j = 0; j < availableMushrooms.length; j++){
        //                 if(rr == j){
        //                     i = j;
        //                     entityManager.addEntity(createMushroomWithID(availableMushrooms[i],posX, posY));
        //                     break;
        //                 }
        //             }
                    
        //         }
        //     }
        // }
    }
    
    public void update(){
        
        camera.update();
        map.updateVisible(player);
        entityManager.update();
    }
    public void render(Graphics g){
        map.render(g);
        entityManager.render(g);
    }
}
