package main;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;

import cards.Card;

import java.awt.Image;
import entities.bows.Bows;
import entities.player.Ability;
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
import world.Biome;
import world.Map;
import world.World;
public class Game extends Engine{

    private int width, height;
    private String title;
    public static Window w;

    private BufferStrategy bs;
    private Graphics g;
    public static MouseManager mm;
    public String status = "Loading";
    private boolean loading = true;
    public Game(int width, int height, String title){
        this.width = width;
        this.height = height;
        this.title = title;
    }
    @Override
    public void init() {
        System.out.println("Creating Window");
        w = new Window(title,width,height);
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
            w.getCanvas().addKeyListener(km);
            mm = new MouseManager();
            w.getCanvas().addMouseListener(mm);
            w.getCanvas().addMouseMotionListener(mm);
            w.getCanvas().addMouseWheelListener(mm);
            w.getCanvas().requestFocus();
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
        
        status = "Loading Abilities";
        try {
            Ability.createAbilities();
        } catch (Exception e) {
            status = "Error occured while loading Abilities";
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
            w.frame.setCursor(customCursor);
        }else{
            System.out.println("Could not set cursor");
        }
    }


    @Override
    public void render() {
        bs = w.getCanvas().getBufferStrategy();
        if(bs == null){
            w.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();
        g.clearRect(0, 0, w.getWidth(), w.getHeight());
        g.setColor(Color.black);
        g.fillRect(0, 0, w.getWidth(), w.getHeight());

        if(State.getState()!=null){
            State.getState().render(g);
        }
        if(loading){
            g.setColor(Color.black);
            g.fillRect(0, 0, w.getWidth(), w.getHeight());
            Font font = new Font("Arial", Font.PLAIN, 25);
            g.setColor(Color.yellow);
            g.setFont(font);
            FontMetrics fontMetrics = g.getFontMetrics(font);
            int stringWidth = fontMetrics.stringWidth(status);
            int stringHeight = fontMetrics.getHeight();
            
            g.drawString(status, w.getWidth()/2-stringWidth/2,w.getHeight()/2-stringHeight/2);
        }
        g.dispose();
        bs.show();
    }

    @Override
    public void update() {
        if(!loading){
            if(State.getState()!=null){
                State.getState().update();
            }
        }
    }
    
}
