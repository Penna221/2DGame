package world;

import java.awt.Graphics;

import entities.EntityManager;
import entities.Player;

public class World {
    //World contains everything. Entitites, map, player, mobs, everything. Even the Camera.
    
    //Can the map change in world? or are there multiple worlds? each with own map, with own player, with own entitites...
    //Different worlds. each with own data.

    public static final int FOREST = 0;
    public int type;

    public Map map;
    private Player player;
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
        player = new Player(100,100);
        Camera.setEntityToCenter(player);
    }
    private void generateMushrooms(int type){

    }
    public void update(){
        player.update();
        camera.update();
        map.updateVisible(player);
    }
    public void render(Graphics g){
        map.render(g);
        player.render(g);
    }
}
