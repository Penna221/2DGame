package gfx;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage load(String path){
        try {
            Path p = Paths.get(path);
            return ImageIO.read(p.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static BufferedImage crop(BufferedImage img, int x, int y, int w, int h){
        return img.getSubimage(x, y, w, h);
    }
}
