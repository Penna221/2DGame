package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.Entity;
import entities.EntityManager;
import entities.projectiles.Projectile;
import entities.projectiles.Projectiles;
import gfx.Factory;
import gfx.LightMap;
import gfx.LineRectangleIntersection;
import gfx.Transition;
import main.Game;
import particles.ParticleManager;
import sound.SoundPlayer;
import tiles.Tile;

public class World {
    //World contains everything. Entitites, map, player, mobs, everything. Even the Camera.
    
    //Can the map change in world? or are there multiple worlds? each with own map, with own player, with own entitites...
    //Different worlds. each with own data.

    public static final int LVL1 = 0, LVL2 = 1, LVL3 = 2;
    public int type;

    public static Map map;
    public static Entity player;
    public static EntityManager entityManager;
    public static ParticleManager particleManager;
    public static Camera camera;
    public static boolean ready = false, readyToUpdate = false;
    private static Transition transition;
    public static HashMap<String,BufferedImage> overlays = new HashMap<String,BufferedImage>();
    public World(){
        entityManager = new EntityManager();
        entityManager.loadEntityData();
        particleManager = new ParticleManager();
        camera = new Camera();
        map = new Map();
        ready = false;
        // load();
    }

    public static void load(String worldName){
        entityManager.clearEntities();
        SoundPlayer.stopAllSounds();
        readyToUpdate = false;
        transition = new Transition(2000){
            @Override
            public void task(){
                Thread t = new Thread(){
                    @Override
                    public void run(){
                        try {
                            ready = false;
                            if(worldName.startsWith("dungeon")){
                                if(worldName.endsWith("lvl1")){
                                    generate(LVL1);
                                }else if(worldName.endsWith("lvl2")){
                                    generate(LVL2);
                                }else if(worldName.endsWith("lvl3")){
                                    generate(LVL3);
                                }
                            }
                            else{
                                String mapName = worldName;
                                String ent = mapName+"_entities";
                                loadEntities(new File("res/maps/"+ent+".csv"));
                                map.loadMap(new File("res/maps/"+mapName+".csv"));

                            }
                            ready = true;
                            readyToUpdate = true;
                            update();
                            // Thread.sleep(3000);
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
        
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = "";
            while((line = reader.readLine())!=null){
                String[] tokens = line.split(",");
                int id = Integer.parseInt(tokens[0]);
                int x = Integer.parseInt(tokens[1])*Tile.tileSize;
                int y = Integer.parseInt(tokens[2])*Tile.tileSize;
                Entity e = entityManager.generateWithID(id,-1, x, y);
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
    private static void generate(int level){
        entityManager.clearEntities();
        generateMap(level);
        //generateMushrooms(type);
    }
    private static void generateMap(int type){
        map = new Map(type,200,200);
    }
    
    private void generateMushrooms(int type){
        String biome;
        switch (type) {
            case LVL1:
                biome = "dungeon_lvl1";
                break;
        
            default:
                biome = "dungeon_lvl1";
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
            particleManager.update();
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
            particleManager.render(g);
            //CAMERA HAS SHADOW / LIGHT MAP
            camera.render(g);
            
            if(overlays.size()!=0){
                for(BufferedImage oi : overlays.values()){
                    g.drawImage(oi, 0,0,null);
                }
                // overlay.clear();
            }
        }
        transition.render(g);
    }
    private static Rectangle[] getVisibleSolidTilesAsRectangles(){
        boolean[][] bm = map.binaryMap;
        ArrayList<Point> points= new ArrayList<Point>();
        for(int y = map.startY; y < map.endY; y++){
            for(int x = map.startX; x < map.endX; x++){
                if(x > bm.length-1 || y > bm[0].length-1){
                    break;
                }
                if(x<0 || y < 0){
                    break;
                }
                //False = Solid. Store coordinates of solid tile to points arraylist. Point(x,y);
                if(!bm[x][y]){
                    points.add(new Point((int)(x*Tile.tileSize - camera.getXOffset()),(int)(y*Tile.tileSize- camera.getYOffset())));
                }
                //Minimap
                // g.fillRect(20+(x - map.startX)*3, 20+(y - map.startY)*3, 3, 3);

            }
        }

        int size = points.size();
        Rectangle[] boxes = new Rectangle[size];
        for(int i = 0; i < size; i++){
            Rectangle box = new Rectangle((int)points.get(i).getX(),(int)points.get(i).getY(), Tile.tileSize, Tile.tileSize);
            boxes[i] = box;
        }

        return boxes;
    }
    public static LightMap generateLightMap(LightSource e, Line2D.Double[] lines2){

        //Go over all visible area. Map class has startX, startY, endX and endY.
        Rectangle[] boxes = getVisibleSolidTilesAsRectangles();
        
        //Go over all points. Those are solid tiles. Create rectangles there so later on it can be checked if line intersects it.
        
        //Create Lines. Origin is player. Other end is in circular pattern.
        int eX = (int)(e.x-camera.getXOffset());
        int eY = (int)(e.y-camera.getYOffset());
        int radius = e.radius;
        int numberOfLines =51;
        ArrayList<Point2D> polyPoints = new ArrayList<Point2D>();
        
        Line2D.Double[] lines = new Line2D.Double[numberOfLines];
        if(lines2==null){
            for (int i = 0; i < numberOfLines; i++) {
                double angle = i * (2 * Math.PI / numberOfLines); // Calculate angle for each line
                int endX = eX + (int) (radius * Math.cos(angle));
                int endY = eY + (int) (radius * Math.sin(angle));
                lines[i] = new Line2D.Double(eX, eY, endX, endY);
            }
        }else{
            lines = lines2;
        }
        ArrayList<Rectangle> finalBoxes = new ArrayList<Rectangle>();
        //Go through all lines. Check if line intersects any boxes. If it does, add it to boxesToCheck arraylist.
        for(int i = 0; i < lines.length; i++){
            Line2D.Double line = lines[i];
            ArrayList<Rectangle> boxesToCheck = new ArrayList<Rectangle>();
            Point2D closestPoint = new Point((int)line.x2, (int)line.y2);
            for(Rectangle box : boxes){
                if(line.intersects(box)){
                    boxesToCheck.add(box);
                }
            }
            //After checking all the boxes. Find the closest box that was intersected with the line.
            if(boxesToCheck.size()>0){
                Rectangle closestBox = getClosestBox(boxesToCheck, line);
                finalBoxes.add(closestBox);
                // ArrayList<Rectangle> neighbors = getBoxNeighbors(closestBox);
                // if(neighbors!=null){
                //     finalBoxes.addAll(neighbors);
                // }
                List<Point2D> intersectionPoints = LineRectangleIntersection.getIntersectionPoints((Double) line, closestBox);
                //If you check the closest point, it will not show the wall tile. It will be in the dark.
                closestPoint = getSmallestDistance(intersectionPoints,new Point((int)(line.x1),(int)line.y1));
                
                //Can we get the boxes backside
                // closestPoint = getBiggestDistance(intersectionPoints,new Point((int)(line.x1),(int)line.y1));
                
                
            }
            polyPoints.add(closestPoint);
            
        }
        int[] xPoints = new int[polyPoints.size()];
        int[] yPoints = new int[polyPoints.size()];
        for(int i = 0; i < polyPoints.size(); i++){
            
            
            xPoints[i] = (int)(polyPoints.get(i).getX());
            yPoints[i] = (int)(polyPoints.get(i).getY());
        }
        Polygon poly = new Polygon(xPoints, yPoints, polyPoints.size());
        Set<Rectangle> uniqueRectanglesSet = new HashSet<>(finalBoxes);
        ArrayList<Rectangle> uniqueRectangles = new ArrayList<>(uniqueRectanglesSet);
        LightMap l = new LightMap(poly, uniqueRectangles);

        return l;
    }
    private static ArrayList<Rectangle> getBoxNeighbors(Rectangle r){
        boolean b = checkIfOutOfBounds(r);
        if(b){
            return null;
        }
        ArrayList<Rectangle> rs = new ArrayList<Rectangle>();
        int x = (int)r.getX();
        int y = (int)r.getY();
        int w = (int)r.getWidth();
        int h = (int)r.getHeight();
        Rectangle left = new Rectangle(x+w,y,w,h);
        Rectangle right = new Rectangle(x-w,y,w,h);
        Rectangle up = new Rectangle(x,y-h,w,h);
        Rectangle down = new Rectangle(x,y+h,w,h);
        if(!map.binaryMap[(int)(left.getCenterX())/Tile.tileSize][(int)(left.getCenterY())/Tile.tileSize]){
            rs.add(left);
        }
        if(!map.binaryMap[(int)(right.getCenterX())/Tile.tileSize][(int)(right.getCenterY())/Tile.tileSize]){
            rs.add(right);
        }
        if(!map.binaryMap[(int)(up.getCenterX())/Tile.tileSize][(int)up.getCenterY()/Tile.tileSize]){
            rs.add(up);
        }
        if(!map.binaryMap[(int)(down.getCenterX())/Tile.tileSize][(int)down.getCenterY()/Tile.tileSize]){
            rs.add(down);
        }
        return rs;
    }
    private static boolean checkIfOutOfBounds(Rectangle r){
        int x = (int)r.getCenterX()/Tile.tileSize;
        int y = (int)r.getCenterY()/Tile.tileSize;
        int w = map.binaryMap.length-1;
        int h = map.binaryMap[0].length-1;
        if(x-1 <0){
            return true;
        }
        if(x+1 > w){
            return true;
        }
        if(y-1 <0){
            return true;
        }
        if(y+1 > h){
            return true;
        }
        return false;
    }
    private static Rectangle getClosestBox(ArrayList<Rectangle> boxes, Line2D.Double line){
        Rectangle closestBox = boxes.get(0);
        double x = line.x1;
        double y = line.y1;
        double minDist = 1000000;
        for(Rectangle box : boxes){
            double centerX = box.getCenterX();
            double centerY = box.getCenterY();
            double distX = Math.abs(x-centerX);
            double distY = Math.abs(y-centerY);
            double dist = Math.sqrt(distX*distX + distY*distY);
            
            if(dist < minDist){
                minDist = dist;
                closestBox= box;
            }
        }


        
        return closestBox;
    }
    private static Point2D getSmallestDistance(List<Point2D> intersectionPoints, Point p){
        double x = p.getX();
        double y = p.getY();
        double minDist = 1000;
        Point2D minPoint = intersectionPoints.get(0);
        for(Point2D pp : intersectionPoints){
            double x1 = pp.getX();
            double y1 = pp.getY();
            double xDist = Math.abs(x-x1);
            double yDist = Math.abs(y-y1);
            double dist = Math.sqrt(xDist*xDist + yDist*yDist);
            if(dist < minDist){
                minDist = dist;
                minPoint = pp;
            }
        }
        return minPoint;
    }
    private static Point2D getBiggestDistance(List<Point2D> intersectionPoints, Point p){
        double x = p.getX();
        double y = p.getY();
        double maxDist = 0;
        Point2D maxPoint = intersectionPoints.get(0);
        for(Point2D pp : intersectionPoints){
            double x1 = pp.getX();
            double y1 = pp.getY();
            double xDist = Math.abs(x-x1);
            double yDist = Math.abs(y-y1);
            double dist = Math.sqrt(xDist*xDist + yDist*yDist);
            if(dist > maxDist){
                maxDist = dist;
                maxPoint = pp;
            }
        }
        return maxPoint;
    }
    public static double getDistanceBetweenEntities(Entity a, Entity b){
        double x1 = a.x;
        double y1 = a.y;
        double x2 = b.x;
        double y2 = b.y;
        double xDist = Math.abs(x1-x2);
        double yDist = Math.abs(y1-y2);
        double dist = Math.sqrt(xDist*xDist + yDist*yDist);
        return dist;
    }
    public static boolean lineOfSightBetween(Entity a, Entity b){
        Rectangle[] boxes = getVisibleSolidTilesAsRectangles();
        Point2D p1 = new Point((int)(a.bounds.getCenterX()-camera.getXOffset()),(int)(a.bounds.getCenterY()-camera.getYOffset()));
        Point2D p2 = new Point((int)(b.bounds.getCenterX()-camera.getXOffset()),(int)(b.bounds.getCenterY()-camera.getYOffset()));
        Line2D.Double line = new Line2D.Double(p1,p2);
        for(Rectangle box : boxes){
            if(line.intersects(box)){
                return false;
            }
        }
        return true;
    }
    public static float getAngleBetween(Entity a, Entity b){
        Point2D p1 = new Point((int)a.bounds.getCenterX(),(int)a.bounds.getCenterY());
        Point2D p2 = new Point((int)b.bounds.getCenterX(),(int)b.bounds.getCenterY());
        return getAngleBetweenPoints(p1, p2);
    }
    public static float getAngleBetweenPoints(Point2D p1, Point2D p2){
        
        float angle = (float) Math.atan2(p1.getY() - p2.getY(), p1.getX() - p2.getX());
        float rot = (float)Math.toDegrees(angle);
        return rot;
    }
    public static Entity generateProjectile(int id, float heading, Point2D origin, Entity source){
        Projectile info = Projectiles.projectiles.get(id);
        if(info==null){
            System.out.println("Projectile not found with id: ("+id+")" );
            return null;
        }
        Entity e = entityManager.generateProjectile(info, (int)origin.getX(), (int)origin.getY(),(int)heading);
        float speed = info.speed;
        e.giveMomentum(heading, speed);
        e.setSource(source);
        return e;
    }
    public static float getPlayerRotationToCursor(){
        int x1 = (int)(player.bounds.getCenterX() - World.camera.getXOffset());
        int y1 = (int)(player.bounds.getCenterY() - World.camera.getYOffset());
        Point2D p1 = new Point(x1,y1);
        int x2 = (int)(Game.mm.mouseX);
        int y2 = (int)(Game.mm.mouseY);
        Point2D p2 = new Point(x2,y2);
        return getAngleBetweenPoints(p2, p1);
    }
    public static ArrayList<Entity> getEntitiesInArea(Polygon area){
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for(Entity e : entityManager.newList){
            if(area.intersects(e.bounds)){
                entities.add(e);
            }
        }
        System.out.println(entities.size());
        return entities;
    }
}
