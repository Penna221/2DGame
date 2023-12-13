package gfx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
public class Factory {
    public static BufferedImage rotateImage(BufferedImage img,double angle){
        double rads = Math.toRadians(angle+90);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);
        
        BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        
        at.rotate(rads, newWidth/2,newHeight/2);
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);
        //at.rotate(rads, newWidth/2,newHeight/2);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return rotated;
    }
    public static void drawCenteredAt(Graphics g, BufferedImage img, Point p){
        int w = img.getWidth();
        int h = img.getHeight();
        int drawX = (int)(p.getX()-(w/2));
        int drawY = (int)(p.getY()-(h/2));
        g.drawImage(img,drawX,drawY,null);
    }
}
