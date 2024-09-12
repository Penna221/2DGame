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
    public static void drawCenteredAt(Graphics g, BufferedImage img, Point p, double scale){
        int w = (int)(img.getWidth()*scale);
        int h = (int)(img.getHeight()*scale);
        int drawX = (int)(p.getX()-(w/2));
        int drawY = (int)(p.getY()-(h/2));
        g.drawImage(img,drawX,drawY,w,h,null);
    }
    public static BufferedImage highlightEdges(BufferedImage img){
        BufferedImage img2 = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                int c = img.getRGB(x, y);
                if(c!=0){
                    continue;
                }
                int lowX = x-1;
                int highX = x+1;
                int lowY = y-1;
                int highY = y+1;
                if(lowX < 0 || highX > img.getWidth()-1||lowY<0 || highY > img.getHeight()-1){
                    continue;
                }
                int cUp = img.getRGB(x, y-1);
                int cDown = img.getRGB(x, y+1);
                int cLeft = img.getRGB(x-1, y);
                int cRight = img.getRGB(x+1, y);
                if(cUp!=0||cDown!=0||cLeft!=0||cRight!=0){
                    img2.setRGB(x, y, Color.white.getRGB());
                }
            }
        }
        return img2;
    }
}
