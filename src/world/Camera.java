package world;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.util.ArrayList;

import entities.Entity;
import main.Game;
public class Camera {
    private Color shadowColor;
    private VolatileImage shadowMap;
    private int screenWidth = 800;
    private int screenHeight = 600;
    private GraphicsConfiguration gc;
    private int lightRadius = 100;

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

        g2d.setComposite(AlphaComposite.DstOut);

        ArrayList<LightSource> lights = findLights();
        for(LightSource p : lights){
            Color c = p.color;
            double tra = p.transparency;
            Color newColor = new Color(c.getRed(),c.getGreen(),c.getBlue(),(int)(tra*255));
            
            Polygon poly = World.generatePolygon(p);
            g2d.setColor(newColor); // Transparent color to clear shadow
            g2d.fillPolygon(poly);
        }
        
        g2d.dispose();
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
