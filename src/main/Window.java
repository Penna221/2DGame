package main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

public class Window {
    private JFrame frame;
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
        canvas.setMinimumSize(d);
        canvas.setMaximumSize(d);
        frame.add(canvas);
        frame.pack();
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
		frame.setTitle(title);
		d = new Dimension(width,height);
		canvas.setPreferredSize(d);
		canvas.setMinimumSize(d);
		canvas.setMaximumSize(d);
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
