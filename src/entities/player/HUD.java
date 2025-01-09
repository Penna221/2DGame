package entities.player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Entity;
import entities.ai.PlayerAI;
import entities.effects.Effect;
import gfx.AssetStorage;
import gfx.Factory;
import main.Game;
import main.Launcher;
import tiles.Tile;
import ui.UIFactory;
import world.World;
public class HUD {
    public boolean showHealth = true;
    public boolean showCoordinates = true;
    public boolean showInventory = true;
    public boolean showEnergy = true;
    private BufferedImage overlay;
    private Graphics g;
    public boolean showExtra = false;
    public ArrayList<BufferedImage> toDraw = new ArrayList<BufferedImage>();
    public HUD(){
        
        World.overlays.put("hud", overlay);
    }
    public void update(){
        draw();
    }
    public void draw(){
        overlay = Factory.generateNewOverlayImage();
        g = overlay.createGraphics();

        drawBackground();
        if(showCoordinates){
            drawCoordinates();
        }
        if(showHealth){
            drawHealth();
        }
        if(showInventory){
            BufferedImage i = PlayerAI.inv.drawHotBar();
            toDraw.add(i);
        }
        if(showEnergy){
            drawEnergy();
        }
        if(World.player.effects.size()!=0){
            drawEffectIcons();
        }
        
        for(BufferedImage i : toDraw){
            g.drawImage(i, 0, 0, null);
        }
        if(showExtra){
            drawExtra(g);
        }
        toDraw.clear();
        World.overlays.put("hud", overlay);
    }
    private void drawExtra(Graphics g){
        int startY = 100;
        int entityCount = World.entityManager.getEntities().size();
        int visibleCount = World.entityManager.newList.size();
        int fps = Launcher.g.getCurrentFPS();
        int width = Game.w.getWidth();
        int height = Game.w.getHeight();


        g.setFont(new Font("Arial",Font.BOLD,20));
        g.setColor(Color.black);
        g.fillRect(10,startY - 10, 230,400);
        g.setColor(Color.WHITE);
        g.drawString("Total Entitities: " + entityCount,20,startY +25);
        g.drawString("Visible Entities: " + visibleCount,20,startY +50);
        g.drawString("FPS: "+fps, 20, startY + 75);
        g.drawString("Screen Width: "+width, 20, startY + 100);
        g.drawString("Screen Height: "+height, 20, startY + 125);
    }
    private void drawEnergy(){
        BufferedImage i = Factory.generateNewOverlayImage();
        Graphics gg = i.createGraphics();
        int lastX = 10;
        int y = i.getHeight()-30;
        BufferedImage icon1 = AssetStorage.images.get("energy_full");
        BufferedImage icon2 = AssetStorage.images.get("energy_empty");
        BufferedImage scaled1 = AssetStorage.scaleImage(icon1, 1.5f);
        BufferedImage scaled2 = AssetStorage.scaleImage(icon2, 1.5f);
        int buffer = 20;
        for(int count = 0; count < PlayerAI.energy; count++){
            
            gg.drawImage(scaled1, lastX,y,null);
            lastX += scaled1.getWidth() + buffer;
        }
        int remaining = PlayerAI.fullEnergy - PlayerAI.energy;
        for(int count = 0; count < remaining; count++){
            
            gg.drawImage(scaled2, lastX,y,null);
            lastX += scaled1.getWidth() + buffer;
        }
        toDraw.add(i);
    }
    private void drawEffectIcons(){
        BufferedImage i = Factory.generateNewOverlayImage();
        Graphics gg = i.createGraphics();
        int lastX = 20;
        int y = i.getHeight()-150;
        for(Effect e : World.player.effects){
            BufferedImage scaled = AssetStorage.scaleImage(e.icon, 1.5f);
            int seconds = e.timer.remainingSeconds;
            BufferedImage icon = UIFactory.generateIconWithAmount(scaled,seconds,scaled.getWidth(),scaled.getHeight(),false,Color.black);
            gg.drawImage(icon, lastX,y,null);
            lastX += scaled.getWidth() + 10;
        }
        toDraw.add(i);
    }
    public void render(Graphics g2){
        g2.drawImage(overlay, 0, 0, null);
    }
    private void drawBackground(){
        BufferedImage i = Factory.generateNewOverlayImage();
        Graphics gg = i.createGraphics();

        gg.setColor(new Color(63,32,18,140));
        int height = 60;
        gg.fillRect(0, i.getHeight()-height, i.getWidth(), height);

        toDraw.add(i);
    }
    private void drawCoordinates(){
        BufferedImage i = Factory.generateNewOverlayImage();
        Graphics gg = i.createGraphics();
        Entity p = World.player;
        int currentTileX = (int)((p.bounds.x + p.bounds.width/2)/Tile.tileSize);
        int currentTileY = (int)((p.bounds.y + p.bounds.height/2)/Tile.tileSize);
        int xTileWithOffset = currentTileX - World.map.map.length/2;
        int yTileWithOffset = currentTileY - World.map.map[0].length/2;
        gg.setColor(Color.white);
        String s = "coordinates: " + xTileWithOffset +","+ yTileWithOffset;
        Font f = new Font("Arial", Font.PLAIN, 28);
        FontMetrics metrics = g.getFontMetrics(f);
        int h = metrics.getHeight();
        int x = i.getWidth()-130;
        int y = i.getHeight()-2*h;
        gg.drawString(s, x,y);
        toDraw.add(i);
    }
    public void drawHealth(){
        BufferedImage i = Factory.generateNewOverlayImage();
        Graphics gg = i.createGraphics();
        
        Entity player = World.player;
        int health = player.health;
        int maxHealth = player.info.health;
        int hearts = health / 2;
        int half = health%2;
        float scale = 1.5f;
        BufferedImage heart_full = AssetStorage.scaleImage(AssetStorage.images.get("heart_full"), scale);
        BufferedImage heart_full_white = AssetStorage.scaleImage(AssetStorage.images.get("heart_full_white"), scale);
        BufferedImage heart_half = AssetStorage.scaleImage(AssetStorage.images.get("heart_half"), scale);
        BufferedImage heart_half_white = AssetStorage.scaleImage(AssetStorage.images.get("heart_half_white"), scale);

        int totalWidth = 0;
        int xOffset = 10;
        int yOffset = i.getHeight()-60;
        int w = heart_full.getWidth();
        int space = 20;
        int lastX = xOffset+(0*(w+space));
        for(int j = 0; j < hearts; j++){
            lastX = xOffset+(j*(w+space));
            gg.drawImage(heart_full, lastX, yOffset, null);
        }
        if(half!=0){
            if(hearts!=0){
                lastX += w+space;
            }
            gg.drawImage(heart_half, lastX, yOffset, null);
        }
        toDraw.add(i);

    }
}
