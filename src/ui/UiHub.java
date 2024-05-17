package ui;

import java.util.ArrayList;

public class UiHub {
    public static ArrayList<FunctionalElement> buttons = new ArrayList<FunctionalElement>();
    public static ArrayList<FunctionalElement> toAdd = new ArrayList<FunctionalElement>();
    public static ArrayList<FunctionalElement> toRemove = new ArrayList<FunctionalElement>();
    private static boolean clear;
    private static boolean canDelete = false;
    public static void sendClick(){
        
        buttons.removeAll(toRemove);
        toRemove.clear();
        buttons.addAll(toAdd);
        toAdd.clear();
        System.out.println("update buttons size: " + buttons.size());
        canDelete = false;
        for(FunctionalElement e : buttons){
            if(clear){
                System.out.println("break loop");
                break;
            }
            e.click();
        }
        canDelete = true;
    }
    public static void add(FunctionalElement e){
        System.out.println("adding toAdd");
        toAdd.add(e);
    }
    public static void finalStep(){
        // System.out.println("Deleting " + buttons.size()+ " buttons ");
        // buttons.clear();
        // System.out.println("adding " + toAdd.size()+ " buttons ");
        // buttons.addAll(toAdd);
        // toAdd.clear();
        // clear = false;
        // System.out.println("final step");
        // buttons.addAll(toAdd);
        // System.out.println("Buttons in array " + buttons.size());

    }
    public static void sendToggle(boolean b){
        for(FunctionalElement e : buttons){
            e.toggle(b);
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
        toAdd.clear();
        Thread t = new Thread(){
            @Override
            public void run(){
                while(!canDelete){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //toRemove.addAll(buttons);
                buttons.clear();
                clear = true;
            }
        };
        t.start();
    }
}
