package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import gfx.AssetStorage;
import json.JSON;
import json.KeyValuePair;
public class UIFactory {
    
    public static UIData containerData;
    public static UIData textData, highlightedTextData;
    public static UIData buttonData, highlightedButtonData;

    //Alphabet stuff
    private static int alphabetHeight;
    private static double scale;
    private static int spaceWidth = 25;
    private static int spacing = 5;
    public static HashMap<String,BufferedImage> alphabetCharacters;

    public static void loadUIData(){
        JSON json = new JSON(new File("res\\json\\ui.json"));
        KeyValuePair kv = json.parse("");

        KeyValuePair container = kv.findChild("container");
        containerData = parseData(container);

        KeyValuePair text = kv.findChild("text");
        textData = parseData(text);
        KeyValuePair text_high = kv.findChild("text_highlighted");
        highlightedTextData = parseData(text_high);
        
        KeyValuePair button = kv.findChild("button");
        buttonData = parseData(button);
        KeyValuePair button_high = kv.findChild("button_highlighted");
        highlightedButtonData = parseData(button_high);

        KeyValuePair alphabet = kv.findChild("alphabet");
        loadAlphabet(alphabet);

    }
    private static UIData parseData(KeyValuePair p){
        UIData d;
        String bgColor = p.findChild("bgColor").getString();
        String fgColor = p.findChild("fgColor").getString();
        String borderColor = p.findChild("borderColor").getString();
        int borderThickness = p.findChild("borderThickness").getInteger();
        System.out.println(bgColor);
        Color c1 = parseColor(bgColor);
        Color c2 = parseColor(fgColor);
        Color c3 = parseColor(borderColor);
        d = new UIData(c1,c2,c3,borderThickness);
        return d;
    }
    private static Color parseColor(String s){
        int rgba = (int) Long.parseLong(s, 16);
        return new Color(rgba,true);
    }

    private static void loadAlphabet(KeyValuePair kv){
        ArrayList<KeyValuePair> data = kv.getObject();

        alphabetCharacters = new HashMap<String,BufferedImage>();

        for(KeyValuePair k : data){
            if(k.getKey().equals("height")){
                alphabetHeight = k.getInteger();
                continue;
            }
            if(k.getKey().equals("scale")){
                scale = k.getFloat();
                continue;
            }
            if(k.getKey().equals("spacing")){
                spacing = k.getInteger();
                continue;
            }
            if(k.getKey().equals("space")){
                spaceWidth = k.getInteger();
                continue;
            }
            String name = k.getKey();
            BufferedImage letter = loadCharacter(k);
            alphabetCharacters.put(name, letter);
        }
    }
    private static BufferedImage loadCharacter(KeyValuePair kv){
        String textureAsset = kv.findChild("texture").getString();
        BufferedImage original = AssetStorage.images.get(textureAsset);
        BufferedImage colored = putColorOnImage(original);

        return colored;
    }
    private static BufferedImage putColorOnImage(BufferedImage img){
        BufferedImage newImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < img.getHeight(); y++){
            for(int x = 0; x < img.getWidth(); x++){
                int c = img.getRGB(x, y);
                if(c!=0){
                    newImage.setRGB(x, y, textData.fgColor.getRGB());
                }
            }
        }
        return newImage;
    }

    
    public static BufferedImage generateText(String text){
        int len = calculateWidth(text);
        BufferedImage img = new BufferedImage(len, alphabetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        int currentX = 0;

        char[] charArray = text.toCharArray();
        for(char c : charArray){
            if(c==' '){
                currentX += spaceWidth;
                continue;
            }
            BufferedImage charImg = findWithChar(c);
            g.drawImage(charImg, currentX, 0, null);
            currentX += charImg.getWidth() + spacing;
        }


        return img;
    }
    private static int calculateWidth(String text){
        
        int width = 0;
        char[] charArray = text.toCharArray();
        for(char c : charArray){
            if(c==' '){
                width += spaceWidth;
                continue;
            }
            width += findWithChar(c).getWidth() + spacing;
        }
        return width;
    }
    private static BufferedImage findWithChar(char c){
        BufferedImage toReturn;
        switch (c) {
            case 'A':
                toReturn = alphabetCharacters.get("char_A");
                break;
            case 'B':
                toReturn = alphabetCharacters.get("char_B");
                break;
            case 'C':
                toReturn = alphabetCharacters.get("char_C");
                break;
            case 'D':
                toReturn = alphabetCharacters.get("char_D");
                break;
            case 'E':
                toReturn = alphabetCharacters.get("char_E");
                break;
            case 'F':
                toReturn = alphabetCharacters.get("char_F");
                break;
            case 'G':
                toReturn = alphabetCharacters.get("char_G");
                break;
            case 'H':
                toReturn = alphabetCharacters.get("char_H");
                break;
            case 'I':
                toReturn = alphabetCharacters.get("char_I");
                break;
            case 'J':
                toReturn = alphabetCharacters.get("char_J");
                break;
            case 'K':
                toReturn = alphabetCharacters.get("char_K");
                break;
            case 'L':
                toReturn = alphabetCharacters.get("char_L");
                break;
            case 'M':
                toReturn = alphabetCharacters.get("char_M");
                break;
            case 'N':
                toReturn = alphabetCharacters.get("char_N");
                break;
            case 'O':
                toReturn = alphabetCharacters.get("char_O");
                break;
            case 'P':
                toReturn = alphabetCharacters.get("char_P");
                break;
            case 'Q':
                toReturn = alphabetCharacters.get("char_Q");
                break;
            case 'R':
                toReturn = alphabetCharacters.get("char_R");
                break;
            case 'S':
                toReturn = alphabetCharacters.get("char_S");
                break;
            case 'T':
                toReturn = alphabetCharacters.get("char_T");
                break;
            case 'U':
                toReturn = alphabetCharacters.get("char_U");
                break;
            case 'V':
                toReturn = alphabetCharacters.get("char_V");
                break;
            case 'W':
                toReturn = alphabetCharacters.get("char_W");
                break;
            case 'X':
                toReturn = alphabetCharacters.get("char_X");
                break;
            case 'Y':
                toReturn = alphabetCharacters.get("char_Y");
                break;
            case 'Z':
                toReturn = alphabetCharacters.get("char_Z");
                break;
            case 'Ä':
                toReturn = alphabetCharacters.get("char_Ä");
                break;
            case 'Ö':
                toReturn = alphabetCharacters.get("char_Ö");
                break;
            default:
                toReturn = null;
                break;
        }
        return toReturn;
    }



}
