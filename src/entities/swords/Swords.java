package entities.swords;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import entities.AttackBox;
import entities.Entity;
import entities.EntityManager;
import gfx.Factory;
import json.JSON;
import json.KeyValuePair;
import particles.Particle;
import sound.SoundPlayer;
import world.World;

public class Swords {
    public static HashMap<Integer, Sword> swords = new HashMap<Integer,Sword>();
    public static Polygon poke = new Polygon();
    public static Polygon swing = new Polygon();
    public static Polygon cone = new Polygon();

    public static void load() throws Exception{
        File f = new File("res\\json\\swords.json");
        String path = f.getParent();
        System.out.println(path);
        JSON json = new JSON(f);
        json.readFile(false);
        KeyValuePair kv = json.parse("json");
        ArrayList<KeyValuePair> ps = kv.getObject();
        for(KeyValuePair projectile : ps){
            int id = projectile.findChild("id").getInteger();
            String name = projectile.findChild("name").getString();
            int damage = projectile.findChild("damage").getInteger();
            float speed = projectile.findChild("speed").getFloat();
            int reach = projectile.findChild("reach").getInteger();
            String buff = projectile.findChild("buff").getString();
            String texture = projectile.findChild("texture").getString();
            Sword s = new Sword(id,name,damage,speed,reach,buff,texture);
            swords.put(id, s);
        }

        //LOAD ATTACK SHAPES
        File p = new File("res\\csv\\poke.csv");
        BufferedReader reader = new BufferedReader(new FileReader(p));
        String line;
        while((line = reader.readLine())!=null){
            String[] coords = line.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            poke.addPoint(x, y);
        }
        System.out.println("loaded "+ poke.npoints +" points for poke");
        reader.close();
        File p2 = new File("res\\csv\\swing.csv");
        reader = new BufferedReader(new FileReader(p2));
        line = "";
        while((line = reader.readLine())!=null){
            String[] coords = line.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            swing.addPoint(x, y);
        }
        reader.close();
        reader.close();
        File p3 = new File("res\\csv\\cone.csv");
        reader = new BufferedReader(new FileReader(p3));
        line = "";
        while((line = reader.readLine())!=null){
            String[] coords = line.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            cone.addPoint(x, y);
        }
        reader.close();
    }
    public static void createSwordAttack(Entity source, Entity ignore, Polygon s, float direction, int damage, Point2D origin){
        double scaleFactor = 30.0;
        double translateX = origin.getX(); 
        double translateY = origin.getY();
        double rotationAngle = Math.toRadians(direction-90); 
        AffineTransform transform = new AffineTransform();
        // transform.translate(100, 0);
        transform.translate(translateX, translateY);
        transform.scale(scaleFactor, scaleFactor);
        transform.rotate(rotationAngle);
        Polygon transformedPolygon = Factory.transformPolygon(s, transform);
        AttackBox a = new AttackBox(source, damage, transformedPolygon, ignore, direction,AttackBox.MELEE);
        EntityManager.addAttackBox(a);

        double rads = Math.toRadians(direction);
        double unitX = Math.cos(rads);
        double unitY = Math.sin(rads);
        int xOffset = (int)(unitX * 80);
        int yOffset = (int)(unitY * 80);
        Particle p;
        if(s == swing){
            p = new Particle("swing_attack",300,direction);
        }else{
            p = new Particle("poke_attack",300,direction);
        }
        p.setPosition((int)origin.getX()+xOffset,(int)origin.getY()+yOffset);
        World.particleManager.addParticle(p);
        Random r = new Random();
        int r1 = 1+ r.nextInt(3);
        SoundPlayer.playSound("sword_swing_"+r1,true,false);
    }
}