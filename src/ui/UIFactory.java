package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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

    public static void loadUIData()throws Exception{
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
        // System.out.println(bgColor);
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
    public static BufferedImage scaleToHeight(BufferedImage texture, int height){
        float w = texture.getWidth();
        float h = texture.getHeight();
        float factor = 1;
        if(w > h){
            factor = (float)height/w;
            
        }else{
            factor = (float)height/(float)h;
        }
        return AssetStorage.scaleImage(texture,(float)factor);
    }
    
    public static BufferedImage generateText(String text, int maxWidth){
        int len = calculateWidth(text);
        if(len >= maxWidth){
            String[] words = chopText(text,maxWidth);
            Deque<String> remainingWords = new LinkedList<String>();

            for (String aa : words) {
                remainingWords.offer(aa);
            }


            ArrayList<BufferedImage> rows = new ArrayList<BufferedImage>();
            int totalWidth = 0;
            String wordsForRow = "";
            
            
            
            while(true){
                //Take one word to process.
                String currentWord= "";
                currentWord = remainingWords.peek();
                
                if(currentWord==null){
                    //Run out of words. Look if wordsForRow has something. if it does -> add it as lastrow. or if not -> Break from the loop.
                    if(!wordsForRow.isEmpty()){
                        wordsForRow = wordsForRow.substring(0,wordsForRow.length()-1);
                        rows.add(generateImageFromText(wordsForRow));
                    }
                    break;
                }
                //At this point there is something to be processed.

                //calculate the width of the word.
                int l = calculateWidth(currentWord + " ");
                totalWidth += l;
                //Add the length to totalWidth. If total is less than maxWidth: add the currentword to wordsForRow.
                //Then just go to process next word. 
                if(totalWidth<maxWidth){
                    wordsForRow += remainingWords.poll() + " ";
                    continue;
                }
                //BUT if it is more than or equal to maxWidth
                //-> process the wordsForRow. if it is has something in it, add it to rows arraylist.
                // if wordsForRow is empty, currently processed word is toolong, it needs to be chopped into pieces.
                //Add the chopped parts back to the deque.
                //Remove the toolong word from the remaining words.
                else{
                    if(wordsForRow.length()==0){
                        remainingWords.poll();
                        String[] ex = processTooLongWord(currentWord,maxWidth);
                        for(int i = ex.length-1; i>=0; i--){
                            remainingWords.addFirst(ex[i]);
                        }
                        totalWidth = 0;
                        continue;
                    }else{
                        wordsForRow = wordsForRow.trim();
                        rows.add(generateImageFromText(wordsForRow));
                        wordsForRow = "";
                        totalWidth = 0;
                    }
                }
            }
            return combineMultipleRows(rows);
            
            
        }else{
            return generateImageFromText(text);

        }
    }
    private static String[] processTooLongWord(String word, int maxWidth){


        ArrayList<String> pieces = new ArrayList<String>();
        String remaining = word;
        while(true){
            String[] words = splitWord(remaining, maxWidth);
            pieces.add(words[0]);
            remaining = words[1];
            int len = calculateWidth(words[1]);
            if(len<maxWidth){
                pieces.add(words[1]);
                break;
            }
        }
        String[] array = new String[pieces.size()];
        for(int i = 0; i < array.length; i++){
            array[i] = pieces.get(i);
        }

        return array;
    }
    private static BufferedImage combineMultipleRows(ArrayList<BufferedImage> rows){
        BufferedImage wholeImage;
        int height = 0;
        int maxWidth = 0;
        for(BufferedImage row : rows){
            height += row.getHeight();
            int wi = row.getWidth();
            if(wi > maxWidth){
                maxWidth = wi;
            }
        }
        wholeImage = new BufferedImage(maxWidth, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = wholeImage.createGraphics();
        int currentY = 0;
        for(BufferedImage b : rows){
            g.drawImage(b, 0,currentY,null);
            currentY += b.getHeight();
        }
        return wholeImage;
    }
    private static BufferedImage generateImageFromText(String text){
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
    private static String[] chopText(String text, int maxWidth){
        String[] words = text.split(" ");
        if(words.length==1){
            String[] splittedWord = splitWord(text,maxWidth);
            return splittedWord;
        }else{
            
            return words;
        }
    }
    private static String[] splitWord(String word, int maxWidth){
        char[] chars = word.toCharArray();
        int index = -1;
        String word1 = "";
        int lettersTotakeOff = 1;
        for(int i = 0; i < chars.length; i++){
            word1+= chars[i];
            int w = calculateWidth(word1);
            if(w>maxWidth){
                index = i-lettersTotakeOff;
                break;
            } 
        }
        if(index<lettersTotakeOff){
            //Cound fit. but still too damn long.
            //Lets just take 1 letter away...
            String[] words = new String[2];
            words[0] = word.substring(0, word.length()-2)+"-";
            words[1] = word.substring(word.length()-2, word.length());
            return words;
        }
        //Take one more char away.
        String w1 = word.substring(0,index-1) + "-";
        String w2 = word.substring(index-1);
        String[] words = new String[2];
        words[0] = w1;
        words[1] = w2;
        
        return words;
    }
    public static BufferedImage generateBorder(BufferedImage img,int thick){
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage bb = AssetStorage.images.get("border");
        int buffer = 20;
        int nw = w+(buffer*2)+(bb.getWidth()*2*thick);
        int nh = h+(buffer*2)+(bb.getWidth()*2*thick);
        BufferedImage newImage = new BufferedImage(nw,nh,BufferedImage.TYPE_INT_ARGB);
        Graphics g = newImage.createGraphics();
        BufferedImage border = drawBorder(nw,nh,thick);

        g.drawImage(border, 0,0, null);
        g.drawImage(img, buffer+bb.getWidth()*thick, buffer+bb.getHeight()*thick, null);
        return newImage;
    }

    private static BufferedImage drawBorder(int w, int h, int thick){
        BufferedImage img = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        BufferedImage bb = AssetStorage.images.get("border");
        int wr = w/bb.getWidth()+1;
        int hr = h/bb.getWidth()+1;
        for(int y = 0; y < hr; y++){
            for(int x = 0; x < wr; x++){
                g.drawImage(bb, x*bb.getWidth(), y*bb.getHeight(), null);
            }
        }
        int startX = (int)(thick * bb.getWidth());
        int startY = (int)(thick * bb.getHeight());
        int endX = (int)(w - (thick*bb.getWidth()));
        int endY =(int)( h - (thick*bb.getHeight()));
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                img.setRGB(x,y,0);
            }
        }

        return img;
    }


    public static BufferedImage generateIconWithAmount(BufferedImage texture, int amount, int width, int height){
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        
        //Draw Image
        g.setColor(Color.black);
        g.fillRect(0, 0, img.getWidth(),img.getHeight());
        BufferedImage scaledImage = UIFactory.scaleToHeight(texture,img.getHeight());
        
        int centerX = img.getWidth()/2 - scaledImage.getWidth()/2;
        int centerY = img.getHeight()/2 - scaledImage.getHeight()/2;
        g.drawImage(scaledImage, centerX,centerY,null);
        

        //Draw frame
        BufferedImage frame = AssetStorage.images.get("frame");
        BufferedImage scaledFrame = UIFactory.scaleToHeight(frame, img.getWidth());
        g.drawImage(scaledFrame, 0,0,null);

        //Draw Amount
        float percentage = 0.45f;
        BufferedImage i = UIFactory.generateText(""+amount,200);
        int h = img.getHeight();
        int newHeight = (int)(h*percentage);
        BufferedImage i2 = UIFactory.scaleToHeight(i,newHeight);
        int x = img.getWidth() - i2.getWidth();
        int y = img.getHeight()-i2.getHeight();
        g.setColor(new Color(25,0,0,200));
        g.fillRect(x, y, i2.getWidth(), i2.getHeight());
        g.drawImage(i2, x, y, null);
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
            //Take away last spacing.
            width += findWithChar(c).getWidth() + spacing;
        }
        width -= spacing;
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

            case 'a':
                toReturn = alphabetCharacters.get("char_a");
                break;
            case 'b':
                toReturn = alphabetCharacters.get("char_b");
                break;
            case 'c':
                toReturn = alphabetCharacters.get("char_c");
                break;
            case 'd':
                toReturn = alphabetCharacters.get("char_d");
                break;
            case 'e':
                toReturn = alphabetCharacters.get("char_e");
                break;
            case 'f':
                toReturn = alphabetCharacters.get("char_f");
                break;
            case 'g':
                toReturn = alphabetCharacters.get("char_g");
                break;
            case 'h':
                toReturn = alphabetCharacters.get("char_h");
                break;
            case 'i':
                toReturn = alphabetCharacters.get("char_i");
                break;
            case 'j':
                toReturn = alphabetCharacters.get("char_j");
                break;
            case 'k':
                toReturn = alphabetCharacters.get("char_k");
                break;
            case 'l':
                toReturn = alphabetCharacters.get("char_l");
                break;
            case 'm':
                toReturn = alphabetCharacters.get("char_m");
                break;
            case 'n':
                toReturn = alphabetCharacters.get("char_n");
                break;
            case 'o':
                toReturn = alphabetCharacters.get("char_o");
                break;
            case 'p':
                toReturn = alphabetCharacters.get("char_p");
                break;
            case 'q':
                toReturn = alphabetCharacters.get("char_q");
                break;
            case 'r':
                toReturn = alphabetCharacters.get("char_r");
                break;
            case 's':
                toReturn = alphabetCharacters.get("char_s");
                break;
            case 't':
                toReturn = alphabetCharacters.get("char_t");
                break;
            case 'u':
                toReturn = alphabetCharacters.get("char_u");
                break;
            case 'v':
                toReturn = alphabetCharacters.get("char_v");
                break;
            case 'w':
                toReturn = alphabetCharacters.get("char_w");
                break;
            case 'x':
                toReturn = alphabetCharacters.get("char_x");
                break;
            case 'y':
                toReturn = alphabetCharacters.get("char_y");
                break;
            case 'z':
                toReturn = alphabetCharacters.get("char_z");
                break;
            case 'ä':
                toReturn = alphabetCharacters.get("char_ä");
                break;
            case 'ö':
                toReturn = alphabetCharacters.get("char_ö");
                break;
            case '.':
                toReturn = alphabetCharacters.get("char_dot");
                break;
            case ',':
                toReturn = alphabetCharacters.get("char_comma");
                break;
            case '!':
                toReturn = alphabetCharacters.get("char_exclamation");
                break;
            case '?':
                toReturn = alphabetCharacters.get("char_guestion");
                break;
            case '-':
                toReturn = alphabetCharacters.get("char_dash");
                break;
            case '<':
                toReturn = alphabetCharacters.get("char_less");
                break;
            case '>':
                toReturn = alphabetCharacters.get("char_more");
                break;    
            case '0':
                toReturn = alphabetCharacters.get("num_0");
                break;
            case '1':
                toReturn = alphabetCharacters.get("num_1");
                break;
            case '2':
                toReturn = alphabetCharacters.get("num_2");
                break;
            case '3':
                toReturn = alphabetCharacters.get("num_3");
                break;
            case '4':
                toReturn = alphabetCharacters.get("num_4");
                break;
            case '5':
                toReturn = alphabetCharacters.get("num_5");
                break;
            case '6':
                toReturn = alphabetCharacters.get("num_6");
                break;
            case '7':
                toReturn = alphabetCharacters.get("num_7");
                break;
            case '8':
                toReturn = alphabetCharacters.get("num_8");
                break;
            case '9':
                toReturn = alphabetCharacters.get("num_9");
                break;
            case '+':
                toReturn = alphabetCharacters.get("plus");
                break;
            case '=':
                toReturn = alphabetCharacters.get("equal");
                break;
            default:
                toReturn = alphabetCharacters.get("char_guestion");;
                break;
        }
        return toReturn;
    }



}
