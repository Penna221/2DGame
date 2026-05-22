package tools;
import java.io.File;

public class PathChanger {
    public static String changePath(String path){
        if(path == null) return null;
        String os = System.getProperty("os.name");
        if(os != null && os.toLowerCase().contains("win")){
            // on Windows, ensure backslashes
            return path.replace('/', File.separatorChar);
        } else {
            // on Linux/others, ensure forward slashes
            return path.replace('\\', '/');
        }
    }
}
