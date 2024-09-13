package world;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.geom.Line2D;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Comparator;

import entities.Entity;
import gfx.ExpandPolygon;
import gfx.LightMap;
import main.Game;
public class Camera {
    private Color shadowColor;
    private VolatileImage shadowMap;
    private int screenWidth = 800;
    private int screenHeight = 600;
    private GraphicsConfiguration gc;

    private Graphics2D g2;
    private double xOffset, yOffset;
    private static Entity centeredEntity;
    public Camera(){
        xOffset = 0;
        yOffset = 0;
        init();
    }
    public void init(){

        int r = 0;
        int g = 0;
        int b = 0;
        int a = 255;
        screenWidth = Game.w.getWidth();
        screenHeight = Game.w.getHeight();
        shadowColor = new Color(r,g,b,a);
        gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        shadowMap = gc.createCompatibleVolatileImage(screenWidth, screenHeight, Transparency.TRANSLUCENT);
    }
    public static void setEntityToCenter(Entity e){
        centeredEntity = e;
    }
    private void centerOnEntity(){
        xOffset = centeredEntity.x - Game.w.getWidth()/2 + centeredEntity.bounds.getWidth()/2;
        yOffset = centeredEntity.y - Game.w.getHeight()/2 + centeredEntity.bounds.getHeight()/2;
        // System.out.println(xOffset + "  " + yOffset);

    }
    public void update(){
        if(centeredEntity!=null){
            // System.out.println("Centering on entity");
            centerOnEntity();
        }
    }
    public double getXOffset(){return xOffset;}
    public double getYOffset(){return yOffset;}

    //SHADER / LIGHT
    public void render(Graphics g){
        
        // g2.setColor(new Color(0));
        // g2.fillRect(0,0,shadowMap.getWidth(),shadowMap.getHeight());
        fillShadowMap();
        
        g.drawImage(shadowMap, 0, 0,null);
    }
    private void fillShadowMap() {
        // Check if VolatileImage is valid
        if (shadowMap.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE) {
            createVolatileImage(); // Recreate if incompatible
        }

        Graphics2D g2d = shadowMap.createGraphics();

        // Step 2: Fill the shadow map with a semi-transparent black
        g2d.setComposite(AlphaComposite.Src);
        g2d.setColor(shadowColor);
        g2d.fillRect(0, 0, screenWidth, screenHeight);

        ArrayList<LightSource> lights = findLights();
        LightMap playerLight = generatePlayerLight(40,15,400);
        ArrayList<LightSource> sortedLights = sortLights(lights);
        drawLight(g2d,playerLight,new Color(0,0,0,255),0.2f);
        for(LightSource p : sortedLights){
            LightMap lightMap = World.generateLightMap(p, null);
            drawLight(g2d,lightMap,p.color,(float)p.transparency);
        }
        


        
        g2d.dispose();
    }
    private LightMap generatePlayerLight(int maxDegrees, int smooth, int l){
        //Player needs to be seperated from other lights.
        
        int px = (int)(World.player.bounds.getCenterX() - xOffset);
        int py = (int)(World.player.bounds.getCenterY() - yOffset);
        int mx = Game.mm.mouseX;
        int my = Game.mm.mouseY;
        double dx = mx - px;
        double dy = my - py;
        double length = Math.sqrt(dx * dx + dy * dy);
        double angleRadians = Math.atan2(my - py, mx - px);
        double angleDegrees = Math.toDegrees(angleRadians);
        
        ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();

        double angle2= angleDegrees - 110;
        double rads2 = Math.toRadians(angle2);
        int nx2 = (int)(px + 100 * Math.cos(rads2));
        int ny2 = (int)(py + 100 * Math.sin(rads2));
        lines.add(new Line2D.Double(px,py,nx2, ny2));

        for(int i = 0; i < smooth; i++){
            double angle= angleDegrees-((smooth-i)*(maxDegrees/smooth));
            double rads = Math.toRadians(angle);
            int nx = (int)(px + l * Math.cos(rads));
            int ny = (int)(py + l * Math.sin(rads));
            lines.add(new Line2D.Double(px,py,nx, ny));
        }
        int nx3 = (int)(px + l * Math.cos(angleRadians));
        int ny3 = (int)(py + l * Math.sin(angleRadians));
        
        lines.add(new Line2D.Double(px,py,nx3, ny3));

        for(int i = 0; i < smooth; i++){
            double angle = angleDegrees+(i*(maxDegrees/smooth));
            double rads = Math.toRadians(angle);
            int nx = (int)(px + l * Math.cos(rads));
            int ny = (int)(py + l * Math.sin(rads));
            lines.add(new Line2D.Double(px,py,nx, ny));
        }
        
        double angle1= angleDegrees + 110;
        double rads1 = Math.toRadians(angle1);
        int nx1 = (int)(px + 100 * Math.cos(rads1));
        int ny1 = (int)(py + 100 * Math.sin(rads1));
        lines.add(new Line2D.Double(px,py,nx1, ny1));
        
        double angle4= angleDegrees + 180;
        double rads4 = Math.toRadians(angle4);
        int nx4 = (int)(px + 100 * Math.cos(rads4));
        int ny4 = (int)(py + 100 * Math.sin(rads4));
        lines.add(new Line2D.Double(px,py,nx4, ny4));
        


        Line2D.Double[] lineArray = new Line2D.Double[lines.size()];
        for(int i = 0; i < lines.size();i++){
            lineArray[i] = lines.get(i);
        }
        LightSource l1 = new LightSource(px, py, Color.WHITE ,0.5, 200);
        LightMap lightMap = World.generateLightMap(l1, lineArray);
        return lightMap;
    }
    private void drawLight(Graphics2D g2d, LightMap p, Color c, float tra){
        
        Color newColor = new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(tra*255));
        
        Polygon poly = p.p;
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)tra);
        g2d.setComposite(ac);
        g2d.setColor(c); // Transparent color to clear shadow
        g2d.fillPolygon(poly);
        g2d.setComposite(AlphaComposite.DstOut);
        g2d.setColor(newColor); // Transparent color to clear shadow
        g2d.fillPolygon(poly);

        ArrayList<Rectangle> boxes = p.boxes;
        for(Rectangle r : boxes){
            g2d.fillRect(r.x,r.y,r.width,r.height);
        }
    }
    private ArrayList<LightSource> sortLights(ArrayList<LightSource> lights){
        lights.sort(Comparator.comparingDouble(light -> light.transparency));
        return lights;
    }
    public ArrayList<LightSource> findLights(){

        ArrayList<LightSource> points = new ArrayList<LightSource>();
        //Check every entity that is on screen.
        for(Entity e : World.entityManager.inView){
            if(e.info.light_source){
                int x = (int)(e.x + e.bounds.width/2);
                int y = (int)(e.y + e.bounds.height/2);
                LightSource l = new LightSource(x, y, e.info.light_color,e.info.light_transparency, e.info.light_radius);
                points.add(l);
            }
        }
        return points;
    }
    private void createVolatileImage() {
        // Use GraphicsConfiguration to create a hardware-accelerated VolatileImage
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
        shadowMap = gc.createCompatibleVolatileImage(screenWidth, screenHeight, Transparency.TRANSLUCENT);
    }
}
