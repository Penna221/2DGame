package entities.ai;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import entities.swords.Swords;
import gfx.AssetStorage;
import gfx.Factory;
import tools.Timer;
import ui.Task;
import world.World;

public class ProjectileAI extends AI{
    private AttackBox attackBox;
    private int id;
    private String name;
    private int damage;
    private float speed;
    private int maxDistance;
    private String type;
    private String buff;
    private String texture;
    private double startX, startY;
    private Timer homingTimer;
    private Polygon transformedPolygon;
    private Polygon p1;
    public ProjectileAI(Entity e){
        super(e);
    }
    @Override
    public void lateInit() {
        if(e.projectileInfo==null){
            return;
        }
        id = e.projectileInfo.id;
        name = e.projectileInfo.name;
        damage = e.projectileInfo.damage;
        speed = e.projectileInfo.speed;
        maxDistance = e.projectileInfo.max_distance;
        type = e.projectileInfo.type;
        buff = e.projectileInfo.buff;
        texture = e.projectileInfo.texture;
        e.texture = Factory.rotateImage(AssetStorage.images.get(texture),e.rotation);
        e.name = name;
        e.speed = speed;
        startX = e.x;
        startY = e.y;
        p1 = new Polygon();
        for(int i = 0; i < Swords.cone.npoints; i++){
            p1.addPoint(Swords.cone.xpoints[i], Swords.cone.ypoints[i]);
        }
        homingTimer = new Timer(200,new Task(){public void perform(){redirect();}});
    }
    public void redirect(){
        double scaleFactor = 30.0;
        double translateX = e.bounds.getCenterX(); 
        double translateY = e.bounds.getCenterY();
        double rotationAngle = Math.toRadians(e.rotation-90); 
        AffineTransform transform = new AffineTransform();
        // transform.translate(100, 0);
        transform.translate(translateX, translateY);
        transform.scale(scaleFactor, scaleFactor);
        transform.rotate(rotationAngle);
        
        transformedPolygon = Factory.transformPolygon(p1, transform);
        ArrayList<Entity> entitiesInArea = World.getEntitiesInArea(transformedPolygon);
        entitiesInArea.add(World.player);
        Entity closest = null;
        double distance = 10000;
        for(Entity en : entitiesInArea){
            if(!en.info.type.equals("Creature")){
                continue;
            }
            if(e.source == en){
                continue;
            }
            double dist = World.getDistanceBetweenEntities(en, e);
            if(dist< distance){
                closest = en;
            }
        }
        if(closest!=null && World.lineOfSightBetween(closest, e)){
            float rot = World.getAngleBetween(closest,e);
            e.stop();
            e.rotation = (int)rot;
            e.texture = Factory.rotateImage(AssetStorage.images.get(texture),e.rotation);
            e.giveMomentum(rot,e.projectileInfo.speed);

        }
    }
    @Override
    public void update() {
        e.moveProjectile();
        e.updateBounds();
        int totalDamage = damage;
        if(e.projectileInfo.type.equals("Magic")){
            switch (e.projectileInfo.id) {
                case 4:
                    homingTimer.update();
                    break;
            
                default:
                    break;
            }
            if(e.staffInfo!=null){
                totalDamage+= e.staffInfo.damage;
            }
        }else if(e.projectileInfo.type.equals("Arrow")){
            if(e.bowInfo!=null){
                totalDamage+= e.bowInfo.damage;
            }
        }

        attackBox = new AttackBox(e, totalDamage, e.bounds,e.source,e.rotation);
        EntityManager.addAttackBox(attackBox);
        
    }

    @Override
    public void render(Graphics g) {
        // e.drawBounds(g);
        if(transformedPolygon!=null){
            g.drawPolygon(transformedPolygon);
        }
    }
    
}
