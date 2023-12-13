package world;

import java.awt.Graphics;
import java.util.Random;

import entities.BrownMushroom;
import entities.EntityManager;
import entities.Player;
import entities.RedMushroom;
import entities.SpruceTree;
import tiles.Tile;

public class World {
    //World contains everything. Entitites, map, player, mobs, everything. Even the Camera.
    
    //Can the map change in world? or are there multiple worlds? each with own map, with own player, with own entitites...
    //Different worlds. each with own data.

    public static final int FOREST = 0;
    public int type;

    public static Map map;
    public static Player player;
    public static EntityManager entityManager;
    public static Camera camera;
    public World(int type){
        this.type = type;
        generate();
    }
    public void generate(){
        camera = new Camera();
        generateMap(type);
        generateEntities(type);
        generateMushrooms(type);
    }
    private void generateMap(int type){
        map = new Map(type,500,500);
    }
    private void generateEntities(int type){
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        entityManager.loadMushroomData();
        player = new Player(400,400);
        Camera.setEntityToCenter(player);

        boolean[][] bm = map.binaryMap;
        for(int y = 0; y < bm[0].length; y++){
            for(int x = 0; x < bm.length; x++){
                
                if(bm[x][y]){
                    Random r = new Random();
                    int rr = r.nextInt(30);
                    if(rr == 1){
                        entityManager.addEntity(new BrownMushroom(x*Tile.tileSize, y*Tile.tileSize));
                    }else if(rr == 2){
                        entityManager.addEntity(new RedMushroom(x*Tile.tileSize, y*Tile.tileSize));
                    }else if(rr == 3){
                        //entityManager.addEntity(new SpruceTree(x*Tile.tileSize, y*Tile.tileSize));
                    }
                }
            }
        }

    }
    private void generateMushrooms(int type){

    }
    public void update(){
        player.update();
        camera.update();
        map.updateVisible(player);
        entityManager.update();
    }
    public void render(Graphics g){
        map.render(g);
        entityManager.render(g);
        player.render(g);
    }
}
