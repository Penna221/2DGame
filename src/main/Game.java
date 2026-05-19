package main;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;

import cards.Card;
import entities.bows.Bows;
import entities.potions.Potions;
import entities.projectiles.Projectiles;
import entities.staves.Staves;
import entities.swords.Swords;
import gfx.Animations;
import gfx.AssetStorage;
import gfx.Transition;
import io.KeyManager;
import io.MouseManager;
import loot.LootTables;
import loot.Market;
import save.SavedGame;
import sound.SoundPlayer;
import states.State;
import tiles.Tile;
import ui.PauseMenu;
import ui.UIFactory;
import utils.pennanen.Engine;
import utils.pennanen.GameInstance;
import world.Biome;
import world.Map;
public class Game extends Engine{


    public static MouseManager mm;
    public String status = "Loading";
    private boolean loading = true;
    public static Font smallFont;
    public static Font mediumFont;
    public static Font largeFont;
    public Game(int width, int height, String title){
    }
    @Override
    public void init() {
        System.out.println("Creating Window");

        smallFont = loadFont("C:\\Code\\fonts\\cairovixel Font\\Cairopixel.ttf", 20f);
        mediumFont = loadFont("C:\\Code\\fonts\\cairovixel Font\\Cairopixel.ttf", 40f);
        largeFont = loadFont("C:\\Code\\fonts\\cairovixel Font\\Cairopixel.ttf", 70f);
        Thread t = new Thread(){
            @Override
            public void run(){
                try {
                    init2();
                    
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
    public static Font loadFont(String path, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font.deriveFont(size);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) size);
        }
    }
    private void goAway(){
        try {
            System.err.println("Error occured. Exiting program.");
            Thread.sleep(3000);
            stop();
            System.exit(1);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void init2(){
        System.out.println("Loading Assets");
        status = "Loading Assets";
        try {
            AssetStorage.loadImages();
        } catch (Exception e) {
            status = "Error occured while loading Images";
            goAway();
        }


        status = "Loading animations";
        try {
            Animations.loadAnimations();
        } catch (Exception e) {
            status = "Error occured while loading Animations";
            goAway();
        }
        
        


        status = "Loading sounds";
        try {
            AssetStorage.loadSounds();
        } catch (Exception e) {
            status = "Error occured while loading sounds";
            goAway();
        }


        status = "Loading Texts";
        try {
            AssetStorage.loadTexts();
        } catch (Exception e) {
            status = "Error occured while loading Texts";
            goAway();
        }        


        System.out.println("Loading Tile Data");
        status = "Loading Tiles";
        try {
            Tile.loadTilesV2();
        } catch (Exception e) {
            status = "Error occured while loading Tiles";
            goAway();
        }

        System.out.println("Loading Projectile Data");
        status = "Loading Projectiles";
        try {
            Projectiles.load();
        } catch (Exception e) {
            status = "Error occured while loading Projectiles";
            goAway();
        }

        System.out.println("Loading Sword Data");
        status = "Loading Swords";
        try {
            Swords.load();
        } catch (Exception e) {
            status = "Error occured while loading Swords";
            goAway();
        }

        System.out.println("Loading Staff Data");
        status = "Loading Staves";
        try {
            Staves.load();
        } catch (Exception e) {
            status = "Error occured while loading Staves";
            goAway();
        }
        System.out.println("Loading Bow Data");
        status = "Loading Bows";
        try {
            Bows.load();
        } catch (Exception e) {
            status = "Error occured while loading Bows";
            goAway();
        }
        System.out.println("Loading Potion Data");
        status = "Loading Potions";
        try {
            Potions.load();
        } catch (Exception e) {
            status = "Error occured while loading Potions";
            goAway();
        }

        status = "Loading Market prices";
        try{
            Market.load();
        }catch(Exception e){
            e.printStackTrace();
            status = "Error occured while loading Market Prices";
            goAway();
        }
        System.out.println("Loading Biome Data");
        status = "Loading Biomes";
        try {
            Biome.loadBiomeData();
        } catch (Exception e) {
            status = "Error occured while loading Biomes";
            goAway();
        }
        

        System.out.println("Creating input handling");
        status = "Loading Input";
        try {
            KeyManager km = new KeyManager();
            GameInstance.window.canvas.addKeyListener(km);
            mm = new MouseManager();
            GameInstance.window.canvas.addMouseListener(mm);
            GameInstance.window.canvas.addMouseMotionListener(mm);
            GameInstance.window.canvas.addMouseWheelListener(mm);
            GameInstance.window.canvas.requestFocus();
        } catch (Exception e) {
            status = "Error occured while loading IO";
            goAway();
        }

        //UI
        System.out.println("Loading UI Data");
        status = "Loading UI Data";
        try {
            UIFactory.loadUIData();
        } catch (Exception e) {
            status = "Error occured while loading UI Data";
            goAway();
        }

        

        
        

        System.out.println("Creating States");
        status = "Loading States";
        try {
            State.createStates();
        } catch (Exception e) {
            status = "Error occured while loading States";
            goAway();
        }
        

        System.out.println("Loading Pause Menus");
        status = "Loading Pause Menus";
        try{
            PauseMenu.loadPauseMenus();
        }catch(Exception e){
            e.printStackTrace();
            status = "Error occured while loading Pause Menus";
            goAway();
        }

        status = "Loading Structures";
        System.out.println("Loading Structures");
        try {
            Map.loadStructures();
        } catch (Exception e) {
            status = "Error occured while loading Structures";
            goAway();
        }
        status = "Loading Loot Tables";
        System.out.println("Loading Loot tables");
        try {
            LootTables.loadLootTables();
        } catch (Exception e) {
            status = "Error occured while loading Loot Tables";
            goAway();
        }
        
        status = "Loading Cards";
        System.out.println("Loading Cards");
        try {
            Card.loadCards();
        } catch (Exception e) {
            status = "Error occured while loading cards.";
            goAway();
        }

        
        
        status = "Loading Saved Games";
        try {
            SavedGame.loadSavedGames();
        } catch (Exception e) {
            status = "Error occured while Saved Games";
            goAway();
        }
        
        updateCursor("default_cursor");


        status = "Everything loaded. Happy gaming :)";
        SoundPlayer.playSound("allDone",true,false);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        State.setState(State.menuState,true);
        loading = false;
        Transition.canFinish= true;
        Transition.canContinue2= true;
        
    }

    public static void updateCursor(String name){
        // Load the cursor image
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        File cursorFile = new File("res/cursor/"+name+".png");
        if(cursorFile.exists()){
            Image cursorImage = toolkit.getImage(cursorFile.getAbsolutePath());
            // Create the custom cursor (hotspot at (0, 0))
            int x = 0;
            int y = 0;
            switch (name) {
                case "default_cursor":
                    x = 0;
                    y = 0;
                    break;
                case "write_cursor":
                    x = 0;
                    y = 0;
                case "pointer_cursor":
                    x=7;
                    y =0;
                default:
                    x = 0;
                    y = 0;
                    break;
            }
            Cursor customCursor = toolkit.createCustomCursor(cursorImage, new Point(x,y), "Custom Cursor");
            // Set the cursor for the frame
            GameInstance.window.frame.setCursor(customCursor);
        }else{
            System.out.println("Could not set cursor");
        }
    }

    @Override
    public void update() {
        if(!loading){
            if(State.getState()!=null){
                State.getState().update();
            }
        }
    }
    @Override
    public void render(Graphics g) {
        
        g.clearRect(0, 0, GameInstance.window.width, GameInstance.window.height);
        g.setColor(Color.black);
        g.fillRect(0, 0, GameInstance.window.width, GameInstance.window.height);

        if(State.getState()!=null){
            State.getState().render(g);
        }
        if(loading){
            g.setColor(Color.black);
            g.fillRect(0, 0, GameInstance.window.width, GameInstance.window.height);

            g.setColor(Color.yellow);
            g.setFont(largeFont);
            FontMetrics fontMetrics = g.getFontMetrics(largeFont);
            int stringWidth = fontMetrics.stringWidth(status);
            int stringHeight = fontMetrics.getHeight();
            
            g.drawString(status, GameInstance.window.width/2-stringWidth/2, GameInstance.window.height/2-stringHeight/2);
        }
        
    }
    
}
