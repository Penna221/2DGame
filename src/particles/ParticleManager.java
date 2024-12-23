package particles;

import java.awt.Graphics;
import java.util.ArrayList;

public class ParticleManager {
    public ArrayList<Particle> particles,toAdd,toRemove;
    private boolean cleaning;
    public ParticleManager(){
        particles = new ArrayList<Particle>();
        toAdd = new ArrayList<Particle>();
        toRemove = new ArrayList<Particle>();
    }
    public void addParticle(Particle p){
        toAdd.add(p);
    }
    public void removeParticle(Particle p){
        toRemove.add(p);
    }
    public void render(Graphics g){
        for(Particle p : particles){
            if(cleaning){
                break;
            }
            p.render(g);
        }
    }
    public void update(){
        cleaning = true;
        particles.removeAll(toRemove);
        particles.addAll(toAdd);
        toAdd.clear();
        toRemove.clear();
        cleaning = false;
        for(Particle p : particles){
            p.update();
        }
    }
}
