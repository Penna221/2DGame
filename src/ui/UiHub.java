package ui;

import java.util.ArrayList;

public class UiHub {
    public static ArrayList<FunctionalElement> buttons = new ArrayList<FunctionalElement>();
    
    private static boolean clear;
    public static void sendClick(){
        for(FunctionalElement e : buttons){
            e.click();
        }
        if(clear){
            buttons.clear();
            clear = false;
        }
    }
    public static void remove(FunctionalElement e){
        buttons.remove(e);
    }
    public static void clear(){
        clear = true;
    }
}
