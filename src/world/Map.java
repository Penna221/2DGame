package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.collision.CollisionBox;
import main.Game;
import tiles.Tile;

public class Map {
    public int[][] map;
    //Binary map is used to find places where entities can be generated. AND to check entity movement
    public boolean[][] binaryMap;
    public int width, height;
    public String biomeName;
    public int startX, startY, endX, endY;
    public static HashMap<String,Structure> structures = new HashMap<String,Structure>();
    private ArrayList<Rectangle> structureBounds;
    private boolean updating = false;

    public ArrayList<Structure> withEastConnection;
    public ArrayList<Structure> withWestConnection;
    public ArrayList<Structure> withNorthConnection;
    public ArrayList<Structure> withSouthConnection;


    public Map(String wn, int width, int height){
        this.width = width;
        this.height = height;
        generate(wn);
        populateWithEnemies(Biome.biomes.get(wn));     
    }
    public Map(int length){
        this.width = 1000;
        this.height = 1000;
        map = new int[width][height];
        
        structureBounds = new ArrayList<Rectangle>();
        initMapTo(41);
        generateDungeon(length);
        generateWalls(5);
        generateBinaryMap();
        saveMapAsImage();
    }
    public Map(){}
    public static void loadStructures() throws Exception{
        File[] list = new File("res/maps/structures").listFiles();
        ArrayList<File> files = new ArrayList<File>();
        for(File f : list){
            if(f.getName().endsWith("entities.csv")){
                continue;
            }else{
                files.add(f);
            }
        }
        for(File f : files){
            String fileName = f.getName();
            String name = fileName.substring(0,fileName.length()-4);
            // System.out.println(name);
            Structure s = new Structure(name);
            structures.put(name, s);
        }
    }
    public void loadMap(File f){
        // mapLoaded = false;
        String entityFile ="";
        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String line = reader.readLine();
            String[] dimensions = line.split(" ");
            int w = Integer.parseInt(dimensions[0]);
            width = w;
            int h = Integer.parseInt(dimensions[1]);
            height = h;
            int[][] loadedMap = new int[w][h];
            int row = 0;
            entityFile = reader.readLine();
            while((line = reader.readLine())!=null){
                String[] ids = line.split(",");
                for(int i = 0; i < ids.length; i++){

                    int j = Integer.parseInt(ids[i]);
                    loadedMap[i][row] = j;
                }
                row++;
                
            }
            map = loadedMap;
            // mapLoaded = true;
            reader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        generateBinaryMap();
        
    }
    public void generate(String wn){
        structureBounds = new ArrayList<Rectangle>();
        switch (wn) {
            case "cave_spider":
                generateSpiderCave();        
                break;
            case "cave_goblin":
                generateGoblinCave();
                break;
            case "cave_skeleton":
                generateSkeletonCave();
                break;
            default:
                generateSpiderCave();
                break;
        }
        generateWalls(5);
        generateBinaryMap();
        saveMapAsImage();
    }
    public void saveMapAsImage(){
        System.out.println("Trying to save map");
        BufferedImage i = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < binaryMap[0].length;y++){
            for(int x = 0; x < binaryMap.length;x++){
                
                if(binaryMap[x][y]){
                    i.setRGB(x, y, new Color(0,0,0,0).getRGB());
                }else{
                    i.setRGB(x, y, new Color(0,0,0,255).getRGB());
                }
                
            }
        }
        try {
            File f =new File("res/screenshots/map.png");
            f.getParentFile().mkdirs();
            System.out.println("saving " + f.getAbsolutePath());
            ImageIO.write(i, "png",f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void generateSpiderCave(){
        Biome b = Biome.biomes.get("cave_spider");
        basicGeneration(b);
        //BIOME SPECIFIC STRUCTURES THAT ONLY NEED TO SPAWN ONE TIME
        // generateStructureAtRandomSpot("lvl2_tunnel");
        // generateStructureAtRandomSpot("lvl2_tunnel");
        // generateStructureAtRandomSpot("trader_room_v1");
        
    }
    private void generateGoblinCave(){
        Biome b = Biome.biomes.get("cave_goblin");
        basicGeneration(b);

        //BIOME SPECIFIC STRUCTURES THAT ONLY NEED TO SPAWN ONE TIME
        generateStructureAtRandomSpot("trader_room_v1");
        generateStructureAtRandomSpot("boss_door");
    }
    private void generateSkeletonCave(){
        Biome b = Biome.biomes.get("cave_skeleton");
        basicGeneration(b);

        //BIOME SPECIFIC STRUCTURES THAT ONLY NEED TO SPAWN ONE TIME
        generateStructureAtRandomSpot("trader_room_v1");
        generateStructureAtRandomSpot("boss_door");
    }

    private void basicGeneration(Biome b){
        biomeName = b.name;
        Tile[] nonSolidTiles = b.nonSolidTiles;
        Tile[] solidTiles = b.solidTiles;
        Tile[] borderTiles = b.borderTiles;
        
        map = new int[width][height];
        
        //Neccesary
        initMapToZero();
        generateWalls(5);

        //Custom
        cellularAutomata(48,6);
        setNonSolidTiles(nonSolidTiles);
        setSolidTiles(solidTiles,borderTiles);
        // generateDungeon(5);
        generateSpawnArea();
        
        //Styling
        
        //Last steps.
        String[] structureList =  b.structures;
        int size = structureList.length;
        System.out.println("structure list size " + size);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < 10; j++){
                generateStructureAtRandomSpot(structureList[i]);
            }
        }
    }

    private void generateDungeon(int length){

        int centerX = map.length/2;
        int centerY = map[0].length/2;
        Structure s1 = structures.get("spawn_area");
        Stack<Room> roomStack = new Stack<Room>();
        //Spawn
        Room spawn = new Room(centerX, centerY,s1);
        generateStructure(spawn.structure, spawn.x,spawn.y);
        roomStack.add(spawn);

        //Load rooms that have connections.
        loadConnections("");
        for(Structure s : withEastConnection){
            System.out.println(s.name);
        }
        for(Structure s : withSouthConnection){
            System.out.println(s.name);
        }
        for(Structure s : withWestConnection){
            System.out.println(s.name);
        }
        for(Structure s : withNorthConnection){
            System.out.println(s.name);
        }
        
        Room lastRoom = spawn;
        for(int i = 0; i < length; i++){
            System.out.println("Processing "+ i);
            boolean bool = false;
            int tries = 0;
            Room b = null;
            ArrayList<Integer> choises = lastRoom.getConnections();
            if(choises.size()==0){
                break;
            }
            while(!bool && tries<3){

                if(choises.size()==0){
                    break;
                }
                Random r = new Random();
                int dir = r.nextInt(choises.size());
                int ans = choises.get(dir);
                b = connectRandomRoom(lastRoom, ans);
                if(b!=null){
                    if(i < length && b.getConnections().size()<1){
                        // tries++;
                        continue;
                    }
                    bool = generateStructure(b.structure,b.x,b.y);
                }
                if(!bool){
                    choises.remove(dir);
                }
                tries++;
            }
            //Room generated properly
            if(b!=null && bool){
                roomStack.add(b);
                lastRoom = b;
                if(lastRoom.structure.name.startsWith("d_")){
                    // spawnEnemies(lastRoom, World.dungeonLevel);
                }
            }else{
                //Room did not generate properly.
                //Trace back to previous rooms.
                //If available connections, try to continue there.
                //If failure, trace even further back.
                //If at spawn and fails, stop.
                lastRoom = getRoomWithConnectionsFromStack(roomStack);
            }
            
        }
        while(true){
            boolean success = false;
            lastRoom = getRoomWithConnectionsFromStack(roomStack);
            ArrayList<Integer> connections = lastRoom.getConnections();
            for(int i : connections){
                Room f = connectRoom(lastRoom,structures.get("exit"), i);
                if(f!=null){
                    success = generateStructure(f.structure, f.x,f.y);
                    if(success){
                        break;
                    }
                }
            }
            if(success){
                break;
            }
        }
        
        // Room pipe1 = connectRoomEast(spawn, pipe_h);
        // generateStructure(pipe1.structure,pipe1.x,pipe1.y);

        
    }
    private void spawnEnemies(Room r, int lvl){
        Random random = new Random();
        int amount = random.nextInt(10);
        ArrayList<Point> points = new ArrayList<Point>();

        Point randomPoint;
        for(int i = 0; i < amount; i++){
            while(true){
                randomPoint = getRandomSpotInRoom(r);
                boolean notFound = true;
                for(Point p : points){
                    if(p.x == randomPoint.x && p.y == randomPoint.y){
                        notFound = false;
                        break;
                    }
                }
                if(notFound){
                    points.add(randomPoint);
                    break;
                }
            }
            if(randomPoint!=null){
                World.entityManager.spawnEntity(15, -1, randomPoint.getX()*Tile.tileSize,randomPoint.getY()*Tile.tileSize);
            }
        }

        


    }
    private Point getRandomSpotInRoom(Room r){
        int width = r.structure.width;
        int height = r.structure.height;
        Random random = new Random();
        
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int tileID = r.structure.tiles[x][y];
            boolean solid = Tile.getTileByID(tileID).solid;

            if(!solid){
                return new Point(r.x+x,r.y+y);
            }
        }
    }
    private Room getRoomWithConnectionsFromStack(Stack<Room> stack){
        Room rr = null;
        while(stack.size()!=0){
            Room r = stack.pop();
            if(r.getConnections().size()>0){
                rr = r;
                break;
            }
        }
        return rr;

    }
    //Loads rooms that you want to be in dungeon. If prefix is not empty, player can only get for example lvl1_rooms or lvl2_rooms
    private void loadConnections(String prefix){
        withEastConnection = new ArrayList<Structure>();
        withWestConnection = new ArrayList<Structure>();
        withNorthConnection = new ArrayList<Structure>();
        withSouthConnection = new ArrayList<Structure>();
        for(Structure s : structures.values()){
            if(prefix!=""){
                if(!s.name.startsWith(prefix)){
                    continue;
                }
            }
            if(s.name.equals("spawn_area")||s.name.equals("exit")){
                continue;
            }
            if(s.getEastConnectionPoint()!=null){
                withEastConnection.add(s);
            }
            if(s.getWestConnectionPoint()!=null){
                withWestConnection.add(s);
            }
            if(s.getSouthConnectionPoint()!=null){
                withSouthConnection.add(s);
            }
            if(s.getNorthConnectionPoint()!=null){
                withNorthConnection.add(s);
            }
        }
    }
    private Room connectRandomRoom(Room lastRoom, int side){
        switch (side) {
            case 0:
                return connectRoomEast(lastRoom, getRandomStructureFromList(withWestConnection));
            case 1:
                return connectRoomWest(lastRoom, getRandomStructureFromList(withEastConnection));
            case 2:
                return connectRoomNorth(lastRoom, getRandomStructureFromList(withSouthConnection));
            case 3:
                return connectRoomSouth(lastRoom, getRandomStructureFromList(withNorthConnection));
            default:
                return null;
        }
    }
    private Room connectRoom(Room lastRoom, Structure newRoom, int side){
        switch (side) {
            case 0:
                return connectRoomEast(lastRoom, newRoom);
            case 1:
                return connectRoomWest(lastRoom, newRoom);
            case 2:
                return connectRoomNorth(lastRoom, newRoom);
            case 3:
                return connectRoomSouth(lastRoom, newRoom);
            default:
                return null;
        }
    }
    private Structure getRandomStructureFromList(ArrayList<Structure> list){
        int i = list.size();
        Random r = new Random();
        int random = r.nextInt(i);
        Structure s = list.get(random);
        return s;
    }
    private Room connectRoomEast(Room a, Structure b){
        Point connectionA = a.structure.getEastConnectionPoint();

        Point connectionB = b.getWestConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX+connectionB.getX()+1);
            int offsetY = (int)(startY-connectionB.getY());

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.west = true;
            a.east = true;
            return roomb;
        }

        return null;
    }
    private Room connectRoomWest(Room a, Structure b){
        Point connectionA = a.structure.getWestConnectionPoint();

        Point connectionB = b.getEastConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX-connectionB.getX()-1);
            int offsetY = (int)(startY-connectionB.getY());

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.east = true;
            a.west = true;
            return roomb;
        }

        return null;
    }
    private Room connectRoomSouth(Room a, Structure b){
        Point connectionA = a.structure.getSouthConnectionPoint();

        Point connectionB = b.getNorthConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX-connectionB.getX());
            int offsetY = (int)(startY+connectionB.getY()+1);

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.north = true;
            a.south = true;
            return roomb;
        }

        return null;
    }
    private Room connectRoomNorth(Room a, Structure b){
        Point connectionA = a.structure.getNorthConnectionPoint();

        Point connectionB = b.getSouthConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX-connectionB.getX());
            int offsetY = (int)(startY-connectionB.getY()-1);

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.south = true;
            a.north = true;
            return roomb;
        }

        return null;
    }
    private void generateBinaryMap(){
        binaryMap = new boolean[width][height];
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                boolean solid = Tile.getTileByID(map[x][y]).solid;
                if(solid){
                    binaryMap[x][y] = false;
                } else{
                    binaryMap[x][y] = true;
                    
                }
            }
        }
    }

    public boolean checkIfSolid(int x, int y){
        if(x < 0 || y < 0 || x > binaryMap.length-1 || y > binaryMap[0].length-1){
            return false;
        }
        return !binaryMap[x][y];
    }
    //init everything to 0
    private void initMapToZero(){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                map[x][y] = 0;
            }
        }
    }
    private void initMapTo(int i){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                map[x][y] = i;
            }
        }
    }
    //Cellular Automata algorithm
    private void cellularAutomata(int percentage, int iterations){
        Random random = new Random();
        int WIDTH = map.length;
        int HEIGHT = map[0].length;
        // Randomly set initial walkable tiles
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(random.nextInt(100)>percentage){
                    map[i][j] = 1; // 0 or 1 (non-walkable or walkable)
                }else{
                    map[i][j] = 0;
                }
                
            }
        }

        // Apply cellular automata rules for each iteration
        for (int iteration = 0; iteration < iterations; iteration++) {
            int[][] newMap = new int[WIDTH][HEIGHT];

            // Apply rules to each tile
            for (int i = 1; i < WIDTH - 1; i++) {
                for (int j = 1; j < HEIGHT - 1; j++) {
                    int neighbors = countWalkableNeighbors(map, i, j);

                    // Apply rules
                    if (neighbors > 4) {
                        newMap[i][j] = 1; // Set as walkable
                    } else {
                        newMap[i][j] = 0; // Set as non-walkable
                    }
                }
            }

            // Update the map with the new values
            map = newMap;
        }
    }

    private static int countWalkableNeighbors(int[][] map, int x, int y) {
        int count = 0;

        // Check the eight neighbors
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                count += map[x + i][y + j];
            }
        }

        return count;
    }

    //change every 1 on map to -> something in nonSolidTiles
    private void setNonSolidTiles(Tile[] nonSolidTiles){
        Random r = new Random();
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]!=0){
                    int rr = r.nextInt(nonSolidTiles.length);
                    int id = nonSolidTiles[rr].id;
                    map[x][y] = id;
                }
            }
        }
    }
    //change every 0 on map to -> something solid.
    private void setSolidTiles(Tile[] solidTiles, Tile[] borderTiles){
        int w = map.length;
        int h = map[0].length;
        int[][] newMap = new int[w][h];
        
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                newMap[x][y] = map[x][y];
            }
        }
        Random r = new Random();
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]==0){
                    if(checkIfSurrounded(x, y)){
                        newMap[x][y] = solidTiles[0].id;
                    }else{
                        int rr = r.nextInt(borderTiles.length);
                        int id = borderTiles[rr].id;
                        newMap[x][y] = id;
                    }
                }
            }
        }
        map = newMap;
    }
    private boolean checkIfSurrounded(int x, int y){
        if(x < 2 || x >= map.length-1 || y < 2 ||y >= map[0].length-1){
            
            return true;
        }else{
            int count = 0;
            int west = map[x-1][y];
            int north = map[x][y-1];
            int east = map[x+1][y];
            int south = map[x][y+1];
            if(west == 0){
                count++;
            }
            if(north == 0){
                count++;
            }
            if(east == 0){
                count++;
            }
            if(south == 0){
                count++;
            }
            if(count==4){
                return true;
            }
        }
        return false;
    }
    private void generateSpawnArea(){
        int centerX = map.length/2;
        int centerY = map[0].length/2;
        Structure spawn = structures.get("spawn_area");
        int w = spawn.width;
        int h = spawn.height;
        generateStructure(spawn, centerX-w/2, centerY-h/2);
    }
    
    
    //Generate border
    private void generateWalls(int id){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(x <=2||x >= map.length-3 || y <= 2|| y >= map[0].length-3){
                    map[x][y] = id;
                }

            }
        }
    }
    public void populateWithEnemies(Biome biome){
        if(biome==null){
            return;
        }
        int[] entities = biome.entities;
        int[] collectables = biome.collectables;
        for(int y = 0; y < binaryMap[0].length; y++){
            for(int x = 0; x < binaryMap.length; x++){
                if(binaryMap[x][y]){
                    //IF cannot spawn entity, try to spawn collectable.
                    if(!generateRandomEntity(entities, x*Tile.tileSize, y*Tile.tileSize)){
                        generateRandomEntity(collectables, x*Tile.tileSize, y*Tile.tileSize);
                    }
                }
            }
        }
    }
    
    public boolean generateRandomEntity(int[] entities, int x, int y){
        Random r = new Random();
        int d = r.nextInt(100);
        //change to spawn enemy.
        if(d ==1){
            int d2 = r.nextInt(entities.length);
            World.entityManager.spawnEntity(entities[d2],-1, x, y);
            return true;
        }
        return false;
    }

    private void generateStructureAtRandomSpot(String name){
        boolean b = false;
        int rx = 0;
        int ry = 0;
        Random r = new Random();
        int retrys = 4;
        int counter = 0;
        Structure s = structures.get(name);
        while(!b){
            rx = 25 + r.nextInt(map.length-50);
            ry = 25 + r.nextInt(map[0].length-50);
            b = generateStructure(s,rx, ry);
            counter++;
            if(counter>=retrys){
                System.out.println("Failed to generate Strucure: " + name);
                break;
            }
        }
    }
    
    public boolean generateStructure(Structure s, int x, int y){
        if(s==null){
            return false;
        }
        int w = s.width;
        int h = s.height;
        int tiles[][] = s.tiles;
        if(x+w > map.length-2 || y+h >map[0].length-2 || x < 2 || y < 2){
            return false;
        }
        
        Rectangle r = new Rectangle(x,y,w,h);
        for(Rectangle r2 : structureBounds){
            if(r2.intersects(r)){
                return false;
            }
        }
        for(int j = 0; j < h; j++){
            for(int i = 0; i < w; i++){
                map[x+i][y+j] = tiles[i][j];
            }
        }

        structureBounds.add(r);
        for(StructureEntity e : s.entities){
            World.entityManager.spawnEntity(e.id,-1, (e.x + x)*Tile.tileSize, (e.y+y)*Tile.tileSize);
        }
        return true;
    }
    //Can there be added some kind of rayCasting to set viewable area?
    public void updateVisible(Entity e){
        updating = true;
        int centerX = (int)(e.x/Tile.tileSize);
        int centerY = (int)(e.y/Tile.tileSize);
        // System.out.println("Updatevisible: " + centerX + " " + centerY);
        int screenWidth = Game.w.getWidth();
        int screenHeight = Game.w.getHeight();
        int wTiles = screenWidth/Tile.tileSize;
        int hTiles = screenHeight/Tile.tileSize;
        int buffer = 6;
        // System.out.println("UpdateVisible");
        // System.out.println("startX " +startX);
        // System.out.println("startY " +startY);
        startX = (centerX - wTiles/2)-buffer;
        startY = (centerY - hTiles/2)-buffer;

        if(startX <= 0){
            startX = 0;
        }
        if(startY <= 0){
            startY = 0;
        }
        endX = startX + wTiles + buffer*2;
        endY = startY + hTiles+ buffer*2;
        if(endX > map.length){
            endX = map.length;
        }
        if(endY > map[0].length){
            endY = map[0].length;
        }
        ArrayList<CollisionBox> boxes = new ArrayList<CollisionBox>();
        //Generate Collision Rectangles
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                Rectangle r = new Rectangle(x*Tile.tileSize, y*Tile.tileSize,Tile.tileSize,Tile.tileSize);
                boolean solid = !binaryMap[x][y];
                CollisionBox b = new CollisionBox(null,r,solid);
                if(solid)
                    boxes.add(b);
            }
        }
        World.collisionBoxes = boxes;

        // System.out.println("endX " +endX);
        // System.out.println("endY " +endY);
        updating = false;
    }
    public void render(Graphics g){
        
        if(startX>map.length-1){
            startX = 0;
        }
        if(startY>map[0].length-1){
            startY = 0;
        }
        //avoid outOfBounds exception
        if(startX < 0){
            startX = 0;
        }
        if(endX >= map.length){
            endX = map.length;
        }
        if(startY < 0){
            startY = 0;
        }
        if(endY >= map[0].length){
            endY = map[0].length;
        }
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                if(updating){
                    break;
                }
                if(!World.ready){
                    break;
                }
                if(x > map.length){
                    break;
                }
                if(y > map[0].length){
                    break;
                }
                // System.out.println("In Forloop: " + x + " " + y + "  StartX and Y " + startX + " " + startY);
                
                g.drawImage(Tile.getTileByID(map[x][y]).texture, (int)((x*Tile.tileSize) - xOffset), (int)((y*Tile.tileSize) - yOffset),Tile.tileSize,Tile.tileSize, null);
            }
        }
    }
}
