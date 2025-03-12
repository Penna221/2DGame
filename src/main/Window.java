package main;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import ui.Image;
import world.World;

public class Window {
    public JFrame frame;
    private String title;
    private int width, height;    
    private Canvas canvas;
    private Dimension d;
    public Window(String title, int width, int height){
        this.title = title;
        this.width = width;
        this.height = height;
        d = new Dimension(width,height);
        create();
    }
    private void create(){
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        canvas = new Canvas();
        canvas.setPreferredSize(d);
        // canvas.setMinimumSize(d);
        // canvas.setMaximumSize(d);
        frame.add(canvas);
        frame.pack();
        resize(width, height);
        frame.addComponentListener(new ComponentAdapter() 
		{  
		        public void componentResized(ComponentEvent evt) {
		            resize(frame.getWidth(),frame.getHeight());
		        }
		});
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    


    public void refresh() {
        Insets insets = frame.getInsets();
System.out.println("Insets - Top: " + insets.top + ", Left: " + insets.left +
                   ", Bottom: " + insets.bottom + ", Right: " + insets.right);
		frame.setTitle(title);
		d = new Dimension(width-insets.left-insets.right,height-insets.top-insets.bottom);
		canvas.setPreferredSize(d);
		// canvas.setMinimumSize(d);
		// canvas.setMaximumSize(d);
        if(World.camera!=null){
            World.camera.init();
        }
	}
    public void resize(int width, int height){
        this.width = width;
		this.height = height;
        d = new Dimension(width,height);
		refresh();
    }
    public Canvas getCanvas(){return canvas;}
    public int getWidth(){return (int)d.getWidth();}
    public int getHeight(){return (int)d.getHeight();}
}
