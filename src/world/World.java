package world;

import java.awt.Graphics;
import java.util.Random;

import entities.Collectable;
import entities.EntityManager;
import entities.Fox;
import entities.mushrooms.BrownMushroom;
import entities.mushrooms.ChantarelleMushroom;
import entities.mushrooms.MushroomData;
import entities.mushrooms.RedMushroom;
import entities.mushrooms.RussulaPedulosaMushroom;
import entities.mushrooms.SheepPolyporeMushroom;
import entities.player.Player;
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
        //generateMushrooms(type);
    }
    private void generateMap(int type){
        map = new Map(type,500,500);
    }
    private void generateEntities(int type){
        
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        int mapCenterX = (map.map.length/2)*Tile.tileSize;
        int mapCenterY = (map.map[0].length/2)*Tile.tileSize;
        player = new Player(mapCenterX,mapCenterY);
        Camera.setEntityToCenter(player);
        entityManager.addEntity(player);
        Fox fox = new Fox(mapCenterX,mapCenterY);
        entityManager.addEntity(fox);

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
        MushroomData.loadMushrooms();
        int[] availableMushrooms = Biome.biomes.get(biome).mushroomIds;
        boolean[][] bm = map.binaryMap;
        for(int y = 0; y < bm[0].length; y++){
            for(int x = 0; x < bm.length; x++){
                
                if(bm[x][y]){

                    double posX = (x*Tile.tileSize) + Tile.tileSize/2;
                    double posY = (y*Tile.tileSize)+ Tile.tileSize/2;

                    Random r = new Random();
                    int rr = r.nextInt(30);
                    int i = 0;
                    for(int j = 0; j < availableMushrooms.length; j++){
                        if(rr == j){
                            i = j;
                            entityManager.addEntity(createMushroomWithID(availableMushrooms[i],posX, posY));
                            break;
                        }
                    }
                    
                }
            }
        }
    }
    private Collectable createMushroomWithID(int id, double x, double y){
        Collectable c;
        switch (id) {
            case 0:
                c = new BrownMushroom(x, y);
                break;
            case 1:
                c = new RedMushroom(x, y);
                break;
            case 2:
                c = new SheepPolyporeMushroom(x, y);
                break;
            case 3:
                c = new ChantarelleMushroom(x, y);
                break;
            case 4:
                c = new RussulaPedulosaMushroom(x, y);
                break;
            
            default:
                c = new BrownMushroom(x, y);
                break;
        }
        return c;
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
