package gfx;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage load(String path){
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static BufferedImage crop(BufferedImage img, int x, int y, int w, int h){
        return img.getSubimage(x, y, w, h);
    }
}
