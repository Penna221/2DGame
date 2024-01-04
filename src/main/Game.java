package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import gfx.Animations;
import gfx.AssetStorage;
import io.KeyManager;
import io.MouseManager;
import states.State;
import tiles.Tile;
import ui.UIFactory;
import utils.pennanen.Engine;
import world.Biome;
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
    private void init2() throws Exception{
        System.out.println("Loading Assets");
        status = "Loading Assets";
        AssetStorage.loadImages();

        status = "Laoding animations";
        Animations.loadAnimations();

        status = "Loading Texts";
        AssetStorage.loadTexts();        
        System.out.println("Loading Tile Data");
        status = "Loading Tiles";
        Tile.loadTiles();
        System.out.println("Loading Biome Data");
        status = "Loading Biomes";
        Biome.loadBiomeData();
        
        System.out.println("Creating input handling");
        status = "Loading Input";
        //INPUT
        KeyManager km = new KeyManager();
        w.getCanvas().addKeyListener(km);
        mm = new MouseManager();
        w.getCanvas().addMouseListener(mm);
        w.getCanvas().addMouseMotionListener(mm);
        w.getCanvas().requestFocus();
        
        //UI
        System.out.println("Loading UI Data");
        status = "Loading UI Data";
        UIFactory.loadUIData();
        
        System.out.println("Creating States");
        status = "Loading States";
        State.createStates();
        
        status = "Everything loaded. Happy gaming :)";
        Thread.sleep(2000);
        State.setState(State.menuState);
        loading = false;

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
