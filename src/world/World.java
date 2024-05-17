package world;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import entities.Entity;
import entities.EntityManager;
import gfx.Transition;
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
    private static boolean ready = false, readyToUpdate = false;
    private static Transition transition;
    public World(int type){
        this.type = type;
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        generate();
    }
    public World(){
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        camera = new Camera();
        map = new Map();
        // load();
    }
    
    public static void load(String worldName){
        readyToUpdate = false;
        transition = new Transition(2000){
            @Override
            public void task(){
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try {
                            ready = false;
                            String mapName = worldName;
                            String ent = mapName+"_entities";
                            loadEntities(new File("res/maps/"+ent+".csv"));
                            map.loadMap(new File("res/maps/"+mapName+".csv"));
                            ready = true;
                            readyToUpdate = true;
                            update();
                            Thread.sleep(3000);
                            Transition.canContinue2 = true;
                            
                            Transition.canFinish = true;
                            
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
            }
            
        };
        transition.start();
        
    }
    private static void loadEntities(File f){
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
        if(readyToUpdate){
            camera.update();
            map.updateVisible(player);
            entityManager.update();
        }else{
        }
        transition.update();
    }
    public void render(Graphics g){
        if(!ready){
            g.setColor(Color.white);
            g.drawString("Loading...", 250, 25);
        }else{
            map.render(g);
            entityManager.render(g);
        }
        transition.render(g);
    }
}
