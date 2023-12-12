package entities;
import java.awt.Graphics;
import java.util.ArrayList;

public class EntityManager {
    private ArrayList<Entity> entities;
    private ArrayList<Entity> toRemove;
    private ArrayList<Entity> toAdd;
    public EntityManager() {
        entities = new ArrayList<Entity>();
        toRemove = new ArrayList<Entity>();
        toAdd = new ArrayList<Entity>();
    }
    public void addEntity(Entity e){
        toAdd.add(e);
    }
    public void removeEntity(Entity e){
        toRemove.add(e);
    }
    public void render(Graphics g){
        for(Entity e : entities){
            if(e.inView)
            e.render(g);
        }
    }
    public void update(){
        
        for(Entity e : entities){
            if(e.inView)
                e.update();
        }
        entities.removeAll(toRemove);
        entities.addAll(toAdd);
        toRemove.clear();
        toAdd.clear();

    }
    public ArrayList<Entity> getEntities(){return entities;}
}
